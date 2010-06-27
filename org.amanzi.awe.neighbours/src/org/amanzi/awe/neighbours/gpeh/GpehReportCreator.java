/* AWE - Amanzi Wireless Explorer
 * http://awe.amanzi.org
 * (C) 2008-2009, AmanziTel AB
 *
 * This library is provided under the terms of the Eclipse Public License
 * as described at http://www.eclipse.org/legal/epl-v10.html. Any use,
 * reproduction or distribution of the library constitutes recipient's
 * acceptance of this agreement.
 *
 * This library is distributed WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.amanzi.awe.neighbours.gpeh;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.amanzi.awe.neighbours.gpeh.GpehReportModel.CellEcNoAnalisis;
import org.amanzi.awe.neighbours.gpeh.GpehReportModel.CellRscpAnalisis;
import org.amanzi.awe.neighbours.gpeh.GpehReportModel.CellRscpEcNoAnalisis;
import org.amanzi.awe.neighbours.gpeh.GpehReportModel.CellUeTxPowerAnalisis;
import org.amanzi.awe.neighbours.gpeh.GpehReportModel.InterFrequencyICDM;
import org.amanzi.awe.neighbours.gpeh.GpehReportModel.IntraFrequencyICDM;
import org.amanzi.awe.statistic.CallTimePeriods;
import org.amanzi.awe.statistic.IStatisticElement;
import org.amanzi.awe.statistic.IStatisticElementNode;
import org.amanzi.awe.statistic.IStatisticHandler;
import org.amanzi.awe.statistic.IStatisticStore;
import org.amanzi.awe.statistic.StatisticByPeriodStructure;
import org.amanzi.awe.statistic.StatisticNeoService;
import org.amanzi.awe.statistic.TimePeriodStructureCreator;
import org.amanzi.neo.core.INeoConstants;
import org.amanzi.neo.core.database.nodes.SpreadsheetNode;
import org.amanzi.neo.core.enums.GeoNeoRelationshipTypes;
import org.amanzi.neo.core.enums.NetworkRelationshipTypes;
import org.amanzi.neo.core.enums.NodeTypes;
import org.amanzi.neo.core.enums.gpeh.Events;
import org.amanzi.neo.core.utils.GpehReportUtil;
import org.amanzi.neo.core.utils.NeoArray;
import org.amanzi.neo.core.utils.NeoUtils;
import org.amanzi.neo.core.utils.Pair;
import org.amanzi.neo.core.utils.GpehReportUtil.CellReportsProperties;
import org.amanzi.neo.core.utils.GpehReportUtil.MatrixProperties;
import org.amanzi.neo.core.utils.GpehReportUtil.ReportsRelations;
import org.amanzi.splash.swing.Cell;
import org.amanzi.splash.utilities.NeoSplashUtil;
import org.amanzi.splash.utilities.SpreadsheetCreator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.geotools.geometry.jts.JTS;
import org.hsqldb.lib.StringUtil;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.index.IndexHits;
import org.neo4j.index.lucene.LuceneIndexService;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * <p>
 * Create different gpeh reports
 * </p>
 * .
 * 
 * @author tsinkel_a
 * @since 1.0.0
 */
public class GpehReportCreator {

    /** The LOGGER. */
    public static Logger LOGGER = Logger.getLogger(org.amanzi.awe.neighbours.gpeh.GpehReportCreator.class);
    /** The service. */
    private final GraphDatabaseService service;

    /** The model. */
    private final GpehReportModel model;

    /** The lucene service. */
    private final LuceneIndexService luceneService;

    /** The network. */
    private final Node network;

    /** The gpeh. */
    private final Node gpeh;

    /** The monitor. */
    private IProgressMonitor monitor;

    /** The count row. */
    private int countRow;
    // private CellRscpAnalisis rspAnalyse;
    private CellRscpEcNoAnalisis rspEcNoAnalyse;
    private CellUeTxPowerAnalisis ueTxPAnalyse;
    private Pair<Long, Long> minMax;

    // private CellEcNoAnalisis ecNoAnalyse;
    /**
     * Instantiates a new gpeh report creator.
     * 
     * @param network the network
     * @param gpeh the gpeh
     * @param service the service
     * @param luceneService the lucene service
     */
    public GpehReportCreator(Node network, Node gpeh, GraphDatabaseService service, LuceneIndexService luceneService) {
        this.network = network;
        this.gpeh = gpeh;
        this.service = service;
        this.luceneService = luceneService;
        monitor = new NullProgressMonitor();
        model = new GpehReportModel(network, gpeh, service);
    }

    /**
     * Gets the report model.
     * 
     * @return the report model
     */
    public GpehReportModel getReportModel() {
        if (model.getRoot() == null) {
            Transaction tx = service.beginTx();
            try {
                createReportModel();
                tx.success();
            } finally {
                tx.finish();
            }
        }
        return model;
    }

    /**
     * Creates the report model.
     */
    private void createReportModel() {
        if (model.getRoot() != null) {
            return;
        }
        assert !"main".equals(Thread.currentThread().getName());
        Node reports = service.createNode();
        model.getGpeh().createRelationshipTo(reports, ReportsRelations.REPORTS);
        model.getNetwork().createRelationshipTo(reports, ReportsRelations.REPORTS);
        model.findRootNode();
    }

    public void createEcNoCellReport(CallTimePeriods periods) {
        if (model.getCellEcNoAnalisis(periods) != null) {
            return;
        }
        assert !"main".equals(Thread.currentThread().getName());
        Transaction tx = service.beginTx();
        try {
            createMatrix();
            Node parentNode = service.createNode();
            parentNode.setProperty(CellReportsProperties.PERIOD_ID, periods.getId());
            model.getRoot().createRelationshipTo(parentNode, periods.getPeriodRelation(CellEcNoAnalisis.ECNO_PRFIX));
            CellRscpEcNoAnalisis sourceModel = model.getCellRscpEcNoAnalisis(CallTimePeriods.HOURLY);
            Node sourceMainNode = sourceModel.getMainNode();
            Pair<Long, Long> minMax = NeoUtils.getMinMaxTimeOfDataset(gpeh, service);
            for (Relationship rel : sourceMainNode.getRelationships(Direction.OUTGOING)) {
                String bestCellId = StatisticNeoService.getBestCellId(rel.getType().name());
                if (bestCellId != null) {
                    StatisticByPeriodStructure sourceStruc = new StatisticByPeriodStructure(rel.getOtherNode(sourceMainNode),
                            service);
                    GPEHStatisticHandler handler = new GPEHStatisticHandler(sourceStruc);
                    GPEHEcNoStorer store = new GPEHEcNoStorer();
                    TimePeriodStructureCreator creator = new TimePeriodStructureCreator(parentNode, bestCellId, minMax.getLeft(),
                            minMax.getRight(), periods, handler, store, service);
                    creator.createStructure();
                }
            }
            model.findCellEcNoAnalisis(periods);
            tx.success();
        } finally {
            tx.finish();
        }
    }

    public void createRscpEcNoCellReport(CallTimePeriods periods) {
        if (model.getCellRscpEcNoAnalisis(periods) != null) {
            return;
        }
        assert !"main".equals(Thread.currentThread().getName());
        Transaction tx = service.beginTx();
        try {
            createMatrix();
            Node parentNode = service.createNode();
            parentNode.setProperty(CellReportsProperties.PERIOD_ID, periods.getId());
            model.getRoot().createRelationshipTo(parentNode, periods.getPeriodRelation(CellEcNoAnalisis.ECNO_PRFIX));
            CellRscpEcNoAnalisis sourceModel = model.getCellRscpEcNoAnalisis(CallTimePeriods.HOURLY);
            Node sourceMainNode = sourceModel.getMainNode();
            Pair<Long, Long> minMax = NeoUtils.getMinMaxTimeOfDataset(gpeh, service);
            for (Relationship rel : sourceMainNode.getRelationships(Direction.OUTGOING)) {
                String bestCellId = StatisticNeoService.getBestCellId(rel.getType().name());
                if (bestCellId != null) {
                    StatisticByPeriodStructure sourceStruc = new StatisticByPeriodStructure(rel.getOtherNode(sourceMainNode),
                            service);
                    GPEHStatisticHandler handler = new GPEHStatisticHandler(sourceStruc);
                    GPEHRscpEcNoStorer store = new GPEHRscpEcNoStorer();
                    TimePeriodStructureCreator creator = new TimePeriodStructureCreator(parentNode, bestCellId, minMax.getLeft(),
                            minMax.getRight(), periods, handler, store, service);
                    creator.createStructure();
                }
            }
            model.findCellRscpEcNoAnalisis(periods);
            tx.success();
        } finally {
            tx.finish();
        }
    }
    /**
     * Creates the rscp cell report.
     * 
     * @param periods the periods
     */
    public void createRSCPCellReport(CallTimePeriods periods) {
        if (model.getCellRscpAnalisis(periods) != null) {
            return;
        }
        assert !"main".equals(Thread.currentThread().getName());
        Transaction tx = service.beginTx();
        try {
            createMatrix();
            Node parentNode = service.createNode();
            parentNode.setProperty(CellReportsProperties.PERIOD_ID, periods.getId());
            model.getRoot().createRelationshipTo(parentNode, periods.getPeriodRelation(CellRscpAnalisis.RSCP_PRFIX));
            CellRscpEcNoAnalisis sourceModel = model.getCellRscpEcNoAnalisis(CallTimePeriods.HOURLY);
            Node sourceMainNode = sourceModel.getMainNode();
            Pair<Long, Long> minMax = NeoUtils.getMinMaxTimeOfDataset(gpeh, service);
            for (Relationship rel : sourceMainNode.getRelationships(Direction.OUTGOING)) {
                String bestCellId = StatisticNeoService.getBestCellId(rel.getType().name());
                if (bestCellId != null) {
                    StatisticByPeriodStructure sourceStruc = new StatisticByPeriodStructure(rel.getOtherNode(sourceMainNode),
                            service);
                    GPEHStatisticHandler handler = new GPEHStatisticHandler(sourceStruc);
                    GPEHRSCPStorer store = new GPEHRSCPStorer();
                    TimePeriodStructureCreator creator = new TimePeriodStructureCreator(parentNode, bestCellId, minMax.getLeft(),
                            minMax.getRight(), periods, handler, store, service);
                    creator.createStructure();
                }
            }
            model.findCellRscpAnalisis(periods);
            tx.success();
        } finally {
            tx.finish();
        }
    }

    /**
     * Creates the matrix.
     */
    public void createMatrix() {
        if (model.getIntraFrequencyICDM() != null) {
            return;
        }
        LOGGER.setLevel(Level.ERROR);
        assert !"main".equals(Thread.currentThread().getName());
        Transaction tx = service.beginTx();
        try {
            createReportModel();
            Node intraFMatrix = service.createNode();
            Node interFMatrix = service.createNode();
            Node iRATMatrix = service.createNode();
            model.getRoot().createRelationshipTo(intraFMatrix, ReportsRelations.ICDM_INTRA_FR);
            model.getRoot().createRelationshipTo(interFMatrix, ReportsRelations.ICDM_INTER_FR);
            model.getRoot().createRelationshipTo(iRATMatrix, ReportsRelations.ICDM_IRAT);
            minMax = NeoUtils.getMinMaxTimeOfDataset(gpeh, service);
            // RSCP_ECNO hour node
            Node parentNode = service.createNode();
            parentNode.setProperty(CellReportsProperties.PERIOD_ID, CallTimePeriods.HOURLY.getId());
            model.getRoot().createRelationshipTo(parentNode, CallTimePeriods.HOURLY.getPeriodRelation(CellRscpEcNoAnalisis.PRFIX));
            // UeTxPower hour node
            parentNode = service.createNode();
            parentNode.setProperty(CellReportsProperties.PERIOD_ID, CallTimePeriods.HOURLY.getId());
            model.getRoot().createRelationshipTo(parentNode, CallTimePeriods.HOURLY.getPeriodRelation(CellUeTxPowerAnalisis.PRFIX));

            rspEcNoAnalyse = model.findCellRscpEcNoAnalisis(CallTimePeriods.HOURLY);
            rspEcNoAnalyse.setUseCache(true);
            ueTxPAnalyse = model.findCellUeTxPowerAnalisis(CallTimePeriods.HOURLY);
            ueTxPAnalyse.setUseCache(true);
            // final String id = GpehReportUtil.getMatrixLuceneIndexName(model.getNetworkName(),
            // model.getGpehEventsName(), GpehReportUtil.MR_TYPE_INTRAF);

            String eventIndName = NeoUtils.getLuceneIndexKeyByProperty(model.getGpeh(), INeoConstants.PROPERTY_NAME_NAME,
                    NodeTypes.GPEH_EVENT);
            String scrCodeIndName = NeoUtils.getLuceneIndexKeyByProperty(model.getNetwork(), GpehReportUtil.PRIMARY_SCR_CODE,
                    NodeTypes.SECTOR);
            long countEvent = 0;
            countRow = 0;
            long time = System.currentTimeMillis();
            long countTx = 0;
            for (Node eventNode : luceneService.getNodes(eventIndName, Events.RRC_MEASUREMENT_REPORT.name())) {
                long timeEv = System.currentTimeMillis();
                countEvent++;
                Set<Node> activeSet = getActiveSet(eventNode);
                Set<RrcMeasurement> measSet = getRncMeasurementSet(eventNode);
                String type = (String)eventNode.getProperty(GpehReportUtil.MR_TYPE, "");
                MeasurementCell bestCell = getBestCell(activeSet, measSet, type);
                if (bestCell == null) {
                    LOGGER.debug(String.format("Event node: %s, not found best cell", eventNode));
                    continue;
                }
                Node tableRoot;
                if (type.equals(GpehReportUtil.MR_TYPE_INTERF)) {
                    tableRoot = interFMatrix;
                } else if (type.equals(GpehReportUtil.MR_TYPE_INTRAF)) {
                    tableRoot = intraFMatrix;
                } else if (type.equals(GpehReportUtil.MR_TYPE_IRAT)) {
                    tableRoot = iRATMatrix;
                    // TODO remove after
                    LOGGER.debug("Event node " + eventNode + " with type " + type + " was passed");
                    continue;
                } else {
                    LOGGER.debug("Event node " + eventNode + " with type " + type + " was passed");
                    continue;
                }
                for (RrcMeasurement measurement : measSet) {
                    if (measurement.getScrambling() == null
                            || (bestCell.getMeasurement() != null && measurement.getScrambling().equals(
                                    bestCell.getMeasurement().getScrambling())) || measurement.getEcNo() == null) {
                        continue;
                    }
                    MeasurementCell sector = findClosestSector(bestCell, measurement, scrCodeIndName);
                    if (sector == null || sector.getCell().equals(bestCell.getCell())) {
                        LOGGER.debug("Sector not found for PSC " + measurement.getScrambling());
                        continue;
                    }
                    Node tableNode = findOrCreateTableNode(bestCell, sector, tableRoot, type);
                    tableNode.createRelationshipTo(eventNode, ReportsRelations.SOURCE_MATRIX_EVENT);
                    handleTableNode(tableNode, type, bestCell, sector, eventNode);
                    if (++countTx > 2000) {
                        countTx = 0;
                        tx.success();
                        tx.finish();
                        tx = service.beginTx();
                    }
                }
                timeEv = System.currentTimeMillis() - timeEv;
                LOGGER.info("time\t" + timeEv);
                long time2 = System.currentTimeMillis() - time;
                monitor.setTaskName(String.format("Handle %s events, create table rows %s, ttotal time: %s, average time: %s",
                        countEvent, countRow, time2, time2 / countEvent));
            }
            tx.success();
            model.findMatrixNodes();
        } finally {
            tx.finish();
        }

    }

    /**
     * Handle table node.
     * 
     * @param tableNode the table node
     * @param type the type
     * @param bestCell the best cell
     * @param sector the sector
     * @param eventNode the event node
     */
    private void handleTableNode(Node tableNode, String type, MeasurementCell bestCell, MeasurementCell sector, Node eventNode) {
        if (type.equals(GpehReportUtil.MR_TYPE_INTERF)) {
            handleInterFrTableNode(tableNode, bestCell, sector);
            return;
        } else if (type.equals(GpehReportUtil.MR_TYPE_INTRAF)) {
            handleIntraFrTableNode(tableNode, bestCell, sector);
            analyseBestCell(tableNode, bestCell, sector, eventNode);
            return;
        } else if (type.equals(GpehReportUtil.MR_TYPE_IRAT)) {
            // TODO implement
            LOGGER.error("Not handled Irat event");
            return;
        } else {
            throw new IllegalArgumentException();
        }

    }

    /**
     * Analyse best cell.
     * 
     * @param tableNode the table node
     * @param bestCell the best cell
     * @param sector the sector
     * @param eventNode the event node
     */
    private void analyseBestCell(Node tableNode, MeasurementCell bestCell, MeasurementCell sector, Node eventNode) {

        long logTime = System.currentTimeMillis();
        String bestCellName = String.valueOf(bestCell.getCell().getId());
        StatisticByPeriodStructure statisticStructure = rspEcNoAnalyse.getStatisticStructure(bestCellName);
        if (statisticStructure == null) {
            GPEHFakeStatHandler handler = new GPEHFakeStatHandler();
            TimePeriodStructureCreator creator = new TimePeriodStructureCreator(rspEcNoAnalyse.getMainNode(), bestCellName, minMax
                    .getLeft(), minMax.getRight(), CallTimePeriods.HOURLY, handler, handler, service);
            statisticStructure = creator.createStructure();
        }
        Long time = NeoUtils.getNodeTime(eventNode);
        IStatisticElementNode node = statisticStructure.getStatisticNode(time);
        Integer rscpValue = bestCell.getMeasurement().getRscp();
        // for others reports we should store store information about all events;
        // if (rscpValue == null) {
        // rscpValue = -1;
        // }
        Integer ecNoValue = bestCell.getMeasurement().getEcNo();
        // if (ecNoValue == null) {
        // ecNoValue = -1;
        // }
        NeoArray neoArray = new NeoArray(node.getNode(), CellRscpEcNoAnalisis.ARRAY_NAME, 2, service);
        Node arrNode = neoArray.findOrCreateNode(rscpValue, ecNoValue);
        Integer count = (Integer)neoArray.getValueFromNode(arrNode);
        count = count == null ? 1 : count + 1;
        neoArray.setValueToNode(arrNode, count);
        arrNode.createRelationshipTo(eventNode, ReportsRelations.SOURCE_RSCP_ECNO_EVENT);
        statisticStructure = ueTxPAnalyse.getStatisticStructure(bestCellName);
        if (statisticStructure == null) {
            GPEHFakeStatHandler handler = new GPEHFakeStatHandler();
            TimePeriodStructureCreator creator = new TimePeriodStructureCreator(ueTxPAnalyse.getMainNode(), bestCellName, minMax
                    .getLeft(), minMax.getRight(), CallTimePeriods.HOURLY, handler, handler, service);
            statisticStructure = creator.createStructure();
        }
        Integer uepwr = bestCell.getMeasurement().getUeTxPower();
        // TODO testing!
        if (uepwr != null) {
            node = statisticStructure.getStatisticNode(time);
            neoArray = new NeoArray(node.getNode(), CellUeTxPowerAnalisis.ARRAY_NAME, 1, service);
            arrNode = neoArray.findOrCreateNode(uepwr);
            count = (Integer)neoArray.getValueFromNode(arrNode);
            count = count == null ? 1 : count + 1;
            neoArray.setValueToNode(arrNode, count);
            arrNode.createRelationshipTo(eventNode, ReportsRelations.SOURCE_UE_TX_POWER_EVENT);

        } else {
            LOGGER.info("uePwr NotFound " + bestCellName);
        }
        logTime = System.currentTimeMillis() - logTime;
        if (logTime > 5) {
            LOGGER.info("STAT CREATE TIME " + logTime);
        }

    }

    /**
     * Handle inter fr table node.
     * 
     * @param tableNode the table node
     * @param bestCell the best cell
     * @param sector the sector
     */
    private void handleInterFrTableNode(Node tableNode, MeasurementCell bestCell, MeasurementCell sector) {
        Transaction tx = service.beginTx();
        try {
            // Physical distance in meters
            if (!tableNode.hasProperty(MatrixProperties.DISTANCE) && model.getCrs() != null) {
                double dist = JTS.orthodromicDistance(bestCell.getCoordinate(), sector.getCoordinate(), model.getCrs());
                tableNode.setProperty(MatrixProperties.DISTANCE, dist);
            }
            // Defined NBR TRUE when Interfering Cell is defined neighboring cell,
            // FALSE when Interfering Cell is not defined as neighboring cell

            if (!tableNode.hasProperty(MatrixProperties.DEFINED_NBR)) {
                Set<Relationship> relations = NeoUtils.getRelations(bestCell.getCell(), sector.getCell(),
                        NetworkRelationshipTypes.NEIGHBOUR, service);
                boolean def = !relations.isEmpty();
                tableNode.setProperty(MatrixProperties.DEFINED_NBR, def);
            }
            // Tier Distance - not created

            // # of MR for best cell
            // can find - calculate count of relation

            // # of MR for Interfering cell
            // can find - calculate count of relation

            Integer ecNo = sector.getMeasurement().getEcNo();

            if (ecNo >= 37) {// >=-6dB
                updateCounter(tableNode, MatrixProperties.EC_NO_PREFIX + 1);
            }
            if (ecNo >= 31) {// >=-9
                updateCounter(tableNode, MatrixProperties.EC_NO_PREFIX + 2);

            }
            if (ecNo >= 25) {// >=-12
                updateCounter(tableNode, MatrixProperties.EC_NO_PREFIX + 3);

            }
            if (ecNo >= 19) {// >=-15
                updateCounter(tableNode, MatrixProperties.EC_NO_PREFIX + 4);

            }
            if (ecNo >= 13) {// >=-18
                updateCounter(tableNode, MatrixProperties.EC_NO_PREFIX + 5);
            }
            if (sector.getMeasurement().getRscp() != null) {
                Integer rscp = sector.getMeasurement().getRscp();
                if (ecNo > 21) {// >-14
                    if (rscp < 11) {// <-105
                        updateCounter(tableNode, "RSCP1_14");// MatrixProperties.getRSCPECNOPropertyName(1,
                        // 14));
                    }
                    if (rscp < 21) {// <-95
                        updateCounter(tableNode, "RSCP2_14");// MatrixProperties.getRSCPECNOPropertyName(2,
                        // 14));
                    }
                    if (rscp < 31) {// <-85
                        updateCounter(tableNode, "RSCP3_14");// MatrixProperties.getRSCPECNOPropertyName(3,
                        // 14));
                    }
                    if (rscp < 41) {// <-75
                        updateCounter(tableNode, "RSCP4_14");// MatrixProperties.getRSCPECNOPropertyName(4,
                        // 14));
                    }
                    if (rscp >= 41) {// >=-75
                        updateCounter(tableNode, "RSCP5_14");// MatrixProperties.getRSCPECNOPropertyName(5,
                        // 14));
                    }
                }
                if (ecNo > 29) {// >-10
                    if (rscp < 11) {// <-105
                        updateCounter(tableNode, "RSCP1_10");// MatrixProperties.getRSCPECNOPropertyName(1,
                        // 10));
                    }
                    if (rscp < 21) {// <-95
                        updateCounter(tableNode, "RSCP2_10");// MatrixProperties.getRSCPECNOPropertyName(2,
                        // 10));
                    }
                    if (rscp < 31) {// <-85
                        updateCounter(tableNode, "RSCP3_10");// MatrixProperties.getRSCPECNOPropertyName(3,
                        // 10));
                    }
                    if (rscp < 41) {// <-75
                        updateCounter(tableNode, "RSCP4_10");// MatrixProperties.getRSCPECNOPropertyName(4,
                        // 10));
                    }
                    if (rscp >= 41) {// >=-75
                        updateCounter(tableNode, "RSCP5_10");// MatrixProperties.getRSCPECNOPropertyName(5,
                        // 10));
                    }
                }
            } else {
                LOGGER.error("No found rscp" + bestCell + "\t" + sector);
            }
            tx.success();
        } catch (Exception e) {
            // TODO Handle FactoryException
            throw (RuntimeException)new RuntimeException().initCause(e);
        } finally {
            tx.finish();
        }

    }

    /**
     * Handle intra fr table node.
     * 
     * @param tableNode the table node
     * @param bestCell the best cell
     * @param sector the sector
     */
    private void handleIntraFrTableNode(Node tableNode, MeasurementCell bestCell, MeasurementCell sector) {
        Transaction tx = service.beginTx();
        try {
            // Physical distance in meters
            if (!tableNode.hasProperty(MatrixProperties.DISTANCE) && model.getCrs() != null) {
                double dist = JTS.orthodromicDistance(bestCell.getCoordinate(), sector.getCoordinate(), model.getCrs());
                tableNode.setProperty(MatrixProperties.DISTANCE, dist);
            }
            // Defined NBR TRUE when Interfering Cell is defined neighboring cell,
            // FALSE when Interfering Cell is not defined as neighboring cell

            if (!tableNode.hasProperty(MatrixProperties.DEFINED_NBR)) {
                Set<Relationship> relations = NeoUtils.getRelations(bestCell.getCell(), sector.getCell(),
                        NetworkRelationshipTypes.NEIGHBOUR, service);
                boolean def = !relations.isEmpty();
                tableNode.setProperty(MatrixProperties.DEFINED_NBR, def);
            }
            // Tier Distance - not created

            // # of MR for best cell
            // can find - calculate count of relation

            // # of MR for Interfering cell
            // can find - calculate count of relation

            double deltaDbm = (double)Math.abs(bestCell.getMeasurement().getEcNo() - sector.getMeasurement().getEcNo()) / 2;
            if (deltaDbm <= 3) {
                updateCounter(tableNode, MatrixProperties.EC_NO_DELTA_PREFIX + 1);
            }
            if (deltaDbm <= 6) {
                updateCounter(tableNode, MatrixProperties.EC_NO_DELTA_PREFIX + 2);

            }
            if (deltaDbm <= 9) {
                updateCounter(tableNode, MatrixProperties.EC_NO_DELTA_PREFIX + 3);

            }
            if (deltaDbm <= 12) {
                updateCounter(tableNode, MatrixProperties.EC_NO_DELTA_PREFIX + 4);

            }
            if (deltaDbm <= 15) {
                updateCounter(tableNode, MatrixProperties.EC_NO_DELTA_PREFIX + 5);
            }
            if (bestCell.getMeasurement().getRscp() != null && sector.getMeasurement().getRscp() != null) {
                double deltaRscp = (double)Math.abs(bestCell.getMeasurement().getRscp() - sector.getMeasurement().getRscp()) / 1;
                if (deltaRscp <= 3) {
                    updateCounter(tableNode, MatrixProperties.RSCP_DELTA_PREFIX + 1);
                }
                if (deltaRscp <= 6) {
                    updateCounter(tableNode, MatrixProperties.RSCP_DELTA_PREFIX + 2);
                }
                if (deltaRscp <= 9) {
                    updateCounter(tableNode, MatrixProperties.RSCP_DELTA_PREFIX + 3);
                }
                if (deltaRscp <= 12) {
                    updateCounter(tableNode, MatrixProperties.RSCP_DELTA_PREFIX + 4);
                }
                if (deltaRscp <= 15) {
                    updateCounter(tableNode, MatrixProperties.RSCP_DELTA_PREFIX + 5);
                }
            } else {
                LOGGER.warn("No found rscp" + bestCell + "\t" + sector);
            }
            int deltaPosition = sector.getMeasurement().getPosition() - bestCell.getMeasurement().getPosition();
            if (deltaPosition < 0) {
                LOGGER.warn("wrong best cell position: " + bestCell.getMeasurement().getPosition());
                deltaPosition = -deltaPosition;
            }
            if (sector.getMeasurement().getPosition() == 1) {
                if (deltaDbm <= 6) {
                    updateCounter(tableNode, MatrixProperties.POSITION_PREFIX + 1);
                } else {
                    LOGGER.info("found sector with position 2 but wrong delta" + deltaDbm);
                }
            } else if (sector.getMeasurement().getPosition() == 2) {
                if (deltaDbm <= 6) {
                    updateCounter(tableNode, MatrixProperties.POSITION_PREFIX + 2);
                } else {
                    LOGGER.info("found sector with position 3 but wrong delta" + deltaDbm);
                }
            } else if (sector.getMeasurement().getPosition() == 3) {
                if (deltaDbm <= 8) {
                    updateCounter(tableNode, MatrixProperties.POSITION_PREFIX + 3);
                } else {
                    LOGGER.info("found sector with position 3 but wrong delta" + deltaDbm);
                }
            } else if (sector.getMeasurement().getPosition() == 4) {
                if (deltaDbm <= 8) {
                    updateCounter(tableNode, MatrixProperties.POSITION_PREFIX + 4);
                } else {
                    LOGGER.info("found sector with position 3 but wrong delta" + deltaDbm);
                }
            } else if (sector.getMeasurement().getPosition() == 5) {
                if (deltaDbm <= 8) {
                    updateCounter(tableNode, MatrixProperties.POSITION_PREFIX + 5);
                } else {
                    LOGGER.info("found sector with position 3 but wrong delta" + deltaDbm);
                }
            }
            tx.success();
        } catch (Exception e) {
            // TODO Handle FactoryException
            throw (RuntimeException)new RuntimeException().initCause(e);
        } finally {
            tx.finish();
        }

    }

    /**
     * Update counter.
     * 
     * @param tableNode the table node
     * @param propertyName the property name
     */
    private void updateCounter(Node tableNode, String propertyName) {
        Integer c = (Integer)tableNode.getProperty(propertyName, 0);
        tableNode.setProperty(propertyName, ++c);
    }

    /**
     * Find or create table node.
     * 
     * @param bestCell the best cell
     * @param sector the sector
     * @param tableRoot the table root
     * @param type the type
     * @return the node
     */
    private Node findOrCreateTableNode(MeasurementCell bestCell, MeasurementCell sector, Node tableRoot, String type) {
        String id = GpehReportUtil.getTableId(String.valueOf(bestCell.getCell().getId()), String.valueOf(sector.getCell().getId()));
        String indexName = GpehReportUtil.getMatrixLuceneIndexName(model.getNetworkName(), model.getGpehEventsName(), type);
        Transaction tx = service.beginTx();
        try {
            Node result = luceneService.getSingleNode(indexName, id);
            if (result == null) {
                assert !"main".equals(Thread.currentThread().getName());
                result = service.createNode();
                tableRoot.createRelationshipTo(result, GeoNeoRelationshipTypes.CHILD);
                Relationship rel = result.createRelationshipTo(bestCell.getCell(), ReportsRelations.BEST_CELL);
                rel.setProperty(GpehReportUtil.REPORTS_ID, indexName);
                rel = result.createRelationshipTo(sector.getCell(), ReportsRelations.SECOND_SELL);
                rel.setProperty(GpehReportUtil.REPORTS_ID, indexName);
                luceneService.index(result, indexName, id);
                countRow++;
                tx.success();
            }
            return result;
        } finally {
            tx.finish();
        }
    }

    /**
     * Find closest sector.
     * 
     * @param bestCell the best cell
     * @param measurement the measurement
     * @param scrCodeIndName the PSC
     * @return the measurement cell
     */
    private MeasurementCell findClosestSector(MeasurementCell bestCell, RrcMeasurement measurement, String scrCodeIndName) {
        if (bestCell.getLat() == null || bestCell.getLon() == null) {
            LOGGER.debug("bestCell " + bestCell.getCell() + " do not have location");
            return null;
        }
        MeasurementCell result = null;
        IndexHits<Node> nodes = luceneService.getNodes(scrCodeIndName, String.valueOf(measurement.getScrambling()));
        for (Node sector : nodes) {
            if (result == null) {
                result = new MeasurementCell(sector, measurement);
                if (setupLocation(result)) {
                    result.setDistance(calculateDistance(bestCell, result));
                } else {
                    LOGGER.debug("sector " + result.getCell() + " do not have location");
                    result = null;
                }
            } else {
                MeasurementCell candidate = new MeasurementCell(sector, measurement);
                if (setupLocation(candidate)) {
                    candidate.setDistance(calculateDistance(bestCell, candidate));
                    if (candidate.getDistance() < result.getDistance()) {
                        result = candidate;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Calculate distance.
     * 
     * @param bestCell the best cell
     * @param candidate the candidate
     * @return the distance between sectors
     */
    private Double calculateDistance(MeasurementCell bestCell, MeasurementCell candidate) {
        return Math.sqrt(Math.pow(bestCell.getLat() - candidate.getLat(), 2) + Math.pow(bestCell.getLon() - candidate.getLon(), 2));
    }

    /**
     * Gets the best cell.
     * 
     * @param activeSet the active set
     * @param measSet the meas set
     * @param type the type
     * @return the best cell
     */
    private MeasurementCell getBestCell(Set<Node> activeSet, Set<RrcMeasurement> measSet, String type) {
        if (type.equals(GpehReportUtil.MR_TYPE_INTERF) || type.equals(GpehReportUtil.MR_TYPE_IRAT)) {
            Iterator<Node> iterator = activeSet.iterator();
            MeasurementCell bestCell = iterator.hasNext() ? new MeasurementCell(iterator.next()) : null;
            setupLocation(bestCell);
            return bestCell;
        }
        MeasurementCell bestCell = null;
        if (activeSet.isEmpty()) {
            return bestCell;
        }
        for (RrcMeasurement meas : measSet) {
            if (meas.getScrambling() == null || meas.getEcNo() == null) {
                continue;
            }
            if (bestCell == null || bestCell.getMeasurement().getEcNo() < meas.getEcNo()) {
                Node cell = findInActiveSet(activeSet, meas);
                if (cell != null) {
                    bestCell = new MeasurementCell(cell, meas);
                }
            }
        }
        setupLocation(bestCell);
        return bestCell;
    }

    /**
     * Setup location.
     * 
     * @param cell the cell
     * @return true, if successful
     */
    private boolean setupLocation(MeasurementCell cell) {
        if (cell != null) {
            // define location
            Relationship rel = cell.getCell().getSingleRelationship(GeoNeoRelationshipTypes.CHILD, Direction.INCOMING);
            if (rel != null) {
                Node site = rel.getOtherNode(cell.getCell());
                Double lat = (Double)site.getProperty(INeoConstants.PROPERTY_LAT_NAME, null);
                Double lon = (Double)site.getProperty(INeoConstants.PROPERTY_LON_NAME, null);
                cell.setLat(lat);
                cell.setLon(lon);
                return lat != null && lon != null;
            }
        }
        return false;
    }

    /**
     * Find in active set.
     * 
     * @param activeSet the active set
     * @param meas the meas
     * @return the node
     */
    private Node findInActiveSet(Set<Node> activeSet, RrcMeasurement meas) {
        if (meas.getScrambling() == null) {
            return null;
        }
        for (Node node : activeSet) {
            if (node.getProperty(GpehReportUtil.PRIMARY_SCR_CODE, "").equals(String.valueOf(meas.getScrambling()))) {
                return node;
            }
        }
        return null;
    }

    /**
     * Gets the rnc measurement set.
     * 
     * @param eventNode the event node
     * @return the rnc measurement set
     */
    private Set<RrcMeasurement> getRncMeasurementSet(Node eventNode) {
        Set<RrcMeasurement> result = new TreeSet<RrcMeasurement>(new Comparator<RrcMeasurement>() {

            @Override
            public int compare(RrcMeasurement o1, RrcMeasurement o2) {
                if (o1.getEcNo() == null) {
                    return 1;
                }
                if (o2.getEcNo() == null) {
                    return -1;
                };
                return o2.getEcNo().compareTo(o1.getEcNo());
            }
        });
        int id = 0;
        String psc;
        Integer rscp;
        Integer ecNo;
        Integer bsic;
        Integer ueTxPower;
        while (true) {
            id++;
            psc = (String)eventNode.getProperty(GpehReportUtil.GPEH_RRC_SCRAMBLING_PREFIX + id, null);
            rscp = (Integer)eventNode.getProperty(GpehReportUtil.GPEH_RRC_MR_RSCP_PREFIX + id, null);
            ecNo = (Integer)eventNode.getProperty(GpehReportUtil.GPEH_RRC_MR_ECNO_PREFIX + id, null);
            bsic = (Integer)eventNode.getProperty(GpehReportUtil.GPEH_RRC_MR_BSIC_PREFIX + id, null);
            ueTxPower = (Integer)eventNode.getProperty(GpehReportUtil.GPEH_RRC_MR_UE_TX_POWER_PREFIX + id, null);
            if (psc != null || rscp != null || ecNo != null || bsic != null || ueTxPower != null) {
                result.add(new RrcMeasurement(psc, rscp, ecNo, bsic, ueTxPower));
            } else {
                break;
            }
        }
        int i = 0;
        for (RrcMeasurement meas : result) {
            meas.setPosition(++i);
        }
        return result;
    }

    /**
     * Gets the active set.
     * 
     * @param eventNode the event node
     * @return the active set
     */
    private Set<Node> getActiveSet(Node eventNode) {
        Set<Node> result = new LinkedHashSet<Node>();
        for (int id = 1; id <= 4; id++) {
            Integer ci = (Integer)eventNode.getProperty("EVENT_PARAM_C_ID_" + id, null);
            Integer rnc = (Integer)eventNode.getProperty("EVENT_PARAM_RNC_ID_" + id, null);
            if (ci == null || rnc == null) {
                continue;
            }
            Node asNode = NeoUtils.findSector(model.getNetwork(), ci, String.valueOf(rnc), luceneService, service);
            if (asNode != null) {
                result.add(asNode);
            }
        }
        return result;
    }

    /**
     * Sets the monitor.
     * 
     * @param monitor the new monitor
     */
    public void setMonitor(IProgressMonitor monitor) {
        assert monitor != null;
        this.monitor = monitor;
    }

    /**
     * Creates the inta idcm spread sheet.
     * 
     * @param spreadsheetName the spreadsheet name
     * @return the spreadsheet node
     */
    public SpreadsheetNode createIntraIDCMSpreadSheet(String spreadsheetName) {
        createMatrix();
        GpehReportModel mdl = getReportModel();
        IntraFrequencyICDM matrix = mdl.getIntraFrequencyICDM();
        Transaction tx = service.beginTx();
        try {
            SpreadsheetCreator creator = new SpreadsheetCreator(NeoSplashUtil.configureRubyPath(GpehReportUtil.RUBY_PROJECT_NAME),
                    spreadsheetName);
            int column = 0;
            monitor.subTask("create header");
            Cell cellToadd = new Cell(0, column, "", "Serving cell name", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "Serving PSC", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "Interfering cell name", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "Interfering PSC", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "Defined NBR", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "Distance", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "Tier Distance", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "# of MR for best cell", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "# of MR for Interfering cell", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "EcNo Delta1", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "EcNo Delta2", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "EcNo Delta3", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "EcNo Delta4", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "EcNo Delta 5", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "RSCP Delta1", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "RSCP Delta2", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "RSCP Delta3", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "RSCP Delta4", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "RSCP Delta5", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "Position1", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "Position2", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "Position3", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "Position4", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "Position5", null);
            creator.saveCell(cellToadd);
            column++;
            int row = 1;
            int saveCount = 0;
            long time = System.currentTimeMillis();
            for (Node tblRow : matrix.getRowTraverser()) {
                monitor.subTask("create row " + row);
                column = 0;
                // Serving cell name
                String bestCellName = matrix.getBestCellName(tblRow);
                cellToadd = new Cell(row, column, "", bestCellName, null);
                creator.saveCell(cellToadd);
                column++;
                // "Serving PSC"
                String value = matrix.getBestCellPSC(tblRow);
                cellToadd = new Cell(row, column, "", value, null);
                creator.saveCell(cellToadd);
                column++;
                // Interfering cell name
                value = matrix.getInterferingCellName(tblRow);
                cellToadd = new Cell(row, column, "", value, null);
                creator.saveCell(cellToadd);
                column++;
                // Interfering PSC
                value = matrix.getInterferingCellPSC(tblRow);
                cellToadd = new Cell(row, column, "", value, null);
                creator.saveCell(cellToadd);
                column++;
                // Defined NBR
                value = String.valueOf(matrix.isDefinedNbr(tblRow));
                cellToadd = new Cell(row, column, "", value, null);
                creator.saveCell(cellToadd);
                column++;
                // Distance
                value = String.valueOf(matrix.getDistance(tblRow));
                cellToadd = new Cell(row, column, "", value, null);
                creator.saveCell(cellToadd);
                column++;
                // Tier Distance
                value = String.valueOf("N/A");
                cellToadd = new Cell(row, column, "", value, null);
                creator.saveCell(cellToadd);
                column++;
                // # of MR for best cell
                value = String.valueOf(matrix.getNumMRForBestCell(tblRow));
                cellToadd = new Cell(row, column, "", value, null);
                creator.saveCell(cellToadd);
                column++;
                // # of MR for Interfering cell
                value = String.valueOf(matrix.getNumMRForInterferingCell(tblRow));
                cellToadd = new Cell(row, column, "", value, null);
                creator.saveCell(cellToadd);
                column++;
                // Delta EcNo 1-5
                for (int i = 1; i <= 5; i++) {
                    value = String.valueOf(matrix.getDeltaEcNo(i, tblRow));
                    cellToadd = new Cell(row, column, "", value, null);
                    creator.saveCell(cellToadd);
                    column++;
                }
                for (int i = 1; i <= 5; i++) {
                    value = String.valueOf(matrix.getDeltaRSCP(i, tblRow));
                    cellToadd = new Cell(row, column, "", value, null);
                    creator.saveCell(cellToadd);
                    column++;
                }
                for (int i = 1; i <= 5; i++) {
                    value = String.valueOf(matrix.getPosition(i, tblRow));
                    cellToadd = new Cell(row, column, "", value, null);
                    creator.saveCell(cellToadd);
                    column++;
                }
                monitor.setTaskName(String.format("Rows created: %s", row));

                saveCount++;
                if (saveCount > 1000) {
                    time = System.currentTimeMillis() - time;
                    tx.success();
                    tx.finish();
                    saveCount = 0;
                    System.out.println("time of storing 1000 rows: " + time);
                    time = System.currentTimeMillis();
                    tx = service.beginTx();
                }
                row++;
            }
            tx.success();
            System.out.println(creator.getSpreadsheet().getUnderlyingNode().getId());
            return creator.getSpreadsheet();
        } finally {
            tx.finish();
        }
    }

    /**
     * Creates the inter idcm spread sheet.
     * 
     * @param spreadsheetName the spreadsheet name
     * @return the spreadsheet node
     */
    public SpreadsheetNode createInterIDCMSpreadSheet(String spreadsheetName) {
        // TODO implement
        createMatrix();
        GpehReportModel mdl = getReportModel();
        InterFrequencyICDM matrix = mdl.getInterFrequencyICDM();
        Transaction tx = service.beginTx();
        try {
            SpreadsheetCreator creator = new SpreadsheetCreator(NeoSplashUtil.configureRubyPath(GpehReportUtil.RUBY_PROJECT_NAME),
                    spreadsheetName);
            int column = 0;
            monitor.subTask("create header");
            Cell cellToadd = new Cell(0, column, "", "Serving cell name", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "Serving PSC", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "Serving cell UARFCN", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "Overlapping cell name", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "Interfering PSC", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "Overlapping cell UARCFN", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "Defined NBR", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "Distance", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "Tier Distance", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "# of MR for best cell", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "# of MR for Interfering cell", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "EcNo 1", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "EcNo 2", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "EcNo 3", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "EcNo 4", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "EcNo 5", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "RSCP1_14", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "RSCP2_14", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "RSCP3_14", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "RSCP4_14", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "RSCP5_14", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "RSCP1_10", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "RSCP2_10", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "RSCP3_10", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "RSCP4_10", null);
            creator.saveCell(cellToadd);
            column++;
            cellToadd = new Cell(0, column, "", "RSCP5_10", null);
            creator.saveCell(cellToadd);
            column++;
            int row = 1;
            int saveCount = 0;
            long time = System.currentTimeMillis();
            for (Node tblRow : matrix.getRowTraverser()) {
                monitor.subTask("create row " + row);
                column = 0;
                // Serving cell name
                String bestCellName = matrix.getBestCellName(tblRow);
                cellToadd = new Cell(row, column, "", bestCellName, null);
                creator.saveCell(cellToadd);
                column++;
                // "Serving PSC"
                String value = matrix.getBestCellPSC(tblRow);
                cellToadd = new Cell(row, column, "", value, null);
                creator.saveCell(cellToadd);
                column++;
                // Serving cell UARFCN
                value = matrix.getBestCellUARFCN(tblRow);
                cellToadd = new Cell(row, column, "", value, null);
                creator.saveCell(cellToadd);
                column++;
                // Interfering cell name
                value = matrix.getInterferingCellName(tblRow);
                cellToadd = new Cell(row, column, "", value, null);
                creator.saveCell(cellToadd);
                column++;
                // Interfering PSC
                value = matrix.getInterferingCellPSC(tblRow);
                cellToadd = new Cell(row, column, "", value, null);
                creator.saveCell(cellToadd);
                column++;
                // Interfering cell UARFCN
                value = matrix.getInterferingCellUARFCN(tblRow);
                cellToadd = new Cell(row, column, "", value, null);
                creator.saveCell(cellToadd);
                column++;
                // Defined NBR
                value = String.valueOf(matrix.isDefinedNbr(tblRow));
                cellToadd = new Cell(row, column, "", value, null);
                creator.saveCell(cellToadd);
                column++;
                // Distance
                value = String.valueOf(matrix.getDistance(tblRow));
                cellToadd = new Cell(row, column, "", value, null);
                creator.saveCell(cellToadd);
                column++;
                // Tier Distance
                value = String.valueOf("N/A");
                cellToadd = new Cell(row, column, "", value, null);
                creator.saveCell(cellToadd);
                column++;
                // # of MR for best cell
                value = String.valueOf(matrix.getNumMRForBestCell(tblRow));
                cellToadd = new Cell(row, column, "", value, null);
                creator.saveCell(cellToadd);
                column++;
                // # of MR for Interfering cell
                value = String.valueOf(matrix.getNumMRForInterferingCell(tblRow));
                cellToadd = new Cell(row, column, "", value, null);
                creator.saveCell(cellToadd);
                column++;
                // Delta EcNo 1-5
                for (int i = 1; i <= 5; i++) {
                    value = String.valueOf(matrix.getEcNo(i, tblRow));
                    cellToadd = new Cell(row, column, "", value, null);
                    creator.saveCell(cellToadd);
                    column++;
                }
                for (int i = 1; i <= 5; i++) {
                    value = String.valueOf(matrix.getRSCP(i, 14, tblRow));
                    cellToadd = new Cell(row, column, "", value, null);
                    creator.saveCell(cellToadd);
                    column++;
                }
                for (int i = 1; i <= 5; i++) {
                    value = String.valueOf(matrix.getRSCP(i, 10, tblRow));
                    cellToadd = new Cell(row, column, "", value, null);
                    creator.saveCell(cellToadd);
                    column++;
                }

                monitor.setTaskName(String.format("Rows created: %s", row));

                saveCount++;
                if (saveCount > 1000) {
                    time = System.currentTimeMillis() - time;
                    tx.success();
                    tx.finish();
                    saveCount = 0;
                    System.out.println("time of storing 1000 rows: " + time);
                    time = System.currentTimeMillis();
                    tx = service.beginTx();
                }
                row++;
            }
            tx.success();
            System.out.println(creator.getSpreadsheet().getUnderlyingNode().getId());
            return creator.getSpreadsheet();
        } finally {
            tx.finish();
        }
    }

    public class GPEHFakeStatHandler implements IStatisticHandler, IStatisticStore {

        @Override
        public IStatisticElement getStatisics(final Long periodTime, final Long periodEnd) {
            return new IStatisticElement() {

                @Override
                public long getStartTime() {
                    return periodTime;
                }

                @Override
                public CallTimePeriods getPeriod() {
                    return CallTimePeriods.HOURLY;
                }

                @Override
                public long getEndTime() {
                    return periodEnd;
                }
            };
        }

        @Override
        public void storeStatisticElement(IStatisticElement statElem, Node node) {
        }

    }

    /**
     * Creates the rscp cell spread sheet.
     * 
     * @param string the string
     * @param period the period
     * @return the spreadsheet node
     */
    public SpreadsheetNode createRSCPCellSpreadSheet(String string, CallTimePeriods period) {
        IPath path = ResourcesPlugin.getWorkspace().getRoot().getFullPath().append(
                new Path(new StringBuilder("RSCP_REPORT_").append(period.getId()).append(".csv").toString()));
        File file = path.toFile();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Transaction tx = service.beginTx();
        try {
            CSVWriter out = new CSVWriter(new FileWriter(file));
            CellRscpAnalisis analyse = getReportModel().getCellRscpAnalisis(period);
            analyse.getMainNode();
            Node sourceMainNode = analyse.getMainNode();
            Pair<Long, Long> minMax = NeoUtils.getMinMaxTimeOfDataset(gpeh, service);
            // add header
            Calendar calendar = Calendar.getInstance();
            List<String> outRes = new LinkedList<String>();
            outRes.add("Cell Name");
            outRes.add("Date");
            outRes.add("Time");
            outRes.add("Resolution");
            for (int i = 0; i <= 91; i++) {
                outRes.add(new StringBuilder("RSCP=").append(i).append(" (3GPP)").toString());
            }
            out.writeNext(outRes.toArray(new String[0]));
            outRes.clear();
            for (Relationship rel : sourceMainNode.getRelationships(Direction.OUTGOING)) {

                String bestCellId = StatisticNeoService.getBestCellId(rel.getType().name());
                if (bestCellId != null) {
                    outRes.clear();
                    Node sector = service.getNodeById(Long.parseLong(bestCellId));
                    String name = (String)sector.getProperty("userLabel", "");
                    if (StringUtil.isEmpty(name)) {
                        name = NeoUtils.getNodeName(sector);
                    }

                    StatisticByPeriodStructure structure = analyse.getStatisticStructure(bestCellId);
                    for (IStatisticElementNode statNode : structure.getStatNedes(minMax.getLeft(), minMax.getRight())) {
                        outRes.add(name);
                        calendar.setTimeInMillis(statNode.getStartTime());
                        outRes.add(dateFormat.format(calendar.getTime()));
                        outRes.add(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
                        outRes.add(period.getId());
                        if (NeoArray.hasArray(CellRscpAnalisis.ARRAY_NAME, statNode.getNode(), service)) {
                            NeoArray array = new NeoArray(statNode.getNode(), CellRscpAnalisis.ARRAY_NAME, service);
                            for (int i = 0; i <= 91; i++) {
                                Object value = array.getValue(i);
                                if (value == null) {
                                    value = 0;
                                }
                                outRes.add(String.valueOf(value));
                            }
                        } else {
                            for (int i = 0; i <= 91; i++) {
                                outRes.add("0");
                            }
                        }
                        out.writeNext(outRes.toArray(new String[0]));
                        outRes.clear();
                    }

                }
            }
            out.close();
        } catch (IOException e) {
            // TODO Handle IOException
            throw (RuntimeException)new RuntimeException().initCause(e);
        } finally {
            tx.finish();
        }
        return null;
    }

    public SpreadsheetNode createRscpEcNoCellSpreadSheet(String string, CallTimePeriods period) {
        IPath path = ResourcesPlugin.getWorkspace().getRoot().getFullPath().append(
                new Path(new StringBuilder("ECNO_REPORT_").append(period.getId()).append(".csv").toString()));

        File file = path.toFile();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Transaction tx = service.beginTx();
        try {
            CSVWriter out = new CSVWriter(new FileWriter(file));
            CellRscpEcNoAnalisis analyse = getReportModel().getCellRscpEcNoAnalisis(period);
            analyse.getMainNode();
            Node sourceMainNode = analyse.getMainNode();
            Pair<Long, Long> minMax = NeoUtils.getMinMaxTimeOfDataset(gpeh, service);
            // add header
            Calendar calendar = Calendar.getInstance();
            List<String> outRes = new LinkedList<String>();
            outRes.add("Cell Name");
            outRes.add("Date");
            outRes.add("Time");
            outRes.add("Resolution");
            for (int i = 0; i <= 49; i++) {
                outRes.add(new StringBuilder("EcNo=").append(i).append(" (3GPP)").toString());
            }
            out.writeNext(outRes.toArray(new String[0]));
            outRes.clear();
            for (Relationship rel : sourceMainNode.getRelationships(Direction.OUTGOING)) {

                String bestCellId = StatisticNeoService.getBestCellId(rel.getType().name());
                if (bestCellId != null) {
                    outRes.clear();
                    Node sector = service.getNodeById(Long.parseLong(bestCellId));
                    String name = (String)sector.getProperty("userLabel", "");
                    if (StringUtil.isEmpty(name)) {
                        name = NeoUtils.getNodeName(sector);
                    }

                    StatisticByPeriodStructure structure = analyse.getStatisticStructure(bestCellId);
                    for (IStatisticElementNode statNode : structure.getStatNedes(minMax.getLeft(), minMax.getRight())) {
                        outRes.add(name);
                        calendar.setTimeInMillis(statNode.getStartTime());
                        outRes.add(dateFormat.format(calendar.getTime()));
                        outRes.add(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
                        outRes.add(period.getId());
                        if (NeoArray.hasArray(CellEcNoAnalisis.ARRAY_NAME, statNode.getNode(), service)) {
                            NeoArray array = new NeoArray(statNode.getNode(), CellEcNoAnalisis.ARRAY_NAME, service);
                            for (int i = 0; i <= 49; i++) {
                                Object value = array.getValue(i);
                                if (value == null) {
                                    value = 0;
                                }
                                outRes.add(String.valueOf(value));
                            }
                        } else {
                            for (int i = 0; i <= 49; i++) {
                                outRes.add("0");
                            }
                        }
                        out.writeNext(outRes.toArray(new String[0]));
                        outRes.clear();
                    }

                }
            }
            out.close();
        } catch (IOException e) {
            // TODO Handle IOException
            throw (RuntimeException)new RuntimeException().initCause(e);
        } finally {
            tx.finish();
        }
        return null;
    }

    public SpreadsheetNode createUeTxPowerCellSpreadSheet(String string, CallTimePeriods period) {
        IPath path = ResourcesPlugin.getWorkspace().getRoot().getFullPath().append(
                new Path(new StringBuilder("UeTxPower_REPORT_").append(period.getId()).append(".csv").toString()));

        File file = path.toFile();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Transaction tx = service.beginTx();
        try {
            CSVWriter out = new CSVWriter(new FileWriter(file));
            CellUeTxPowerAnalisis analyse = getReportModel().getCellUeTxPowerAnalisis(period);
            analyse.getMainNode();
            Node sourceMainNode = analyse.getMainNode();
            Pair<Long, Long> minMax = NeoUtils.getMinMaxTimeOfDataset(gpeh, service);
            // add header
            Calendar calendar = Calendar.getInstance();
            List<String> outRes = new LinkedList<String>();
            outRes.add("Cell Name");
            outRes.add("Date");
            outRes.add("Time");
            outRes.add("Resolution");
            for (int i = 21; i <= 104; i++) {
                outRes.add(new StringBuilder("UeTxPower=").append(i).append(" (3GPP)").toString());
            }
            out.writeNext(outRes.toArray(new String[0]));
            outRes.clear();
            for (Relationship rel : sourceMainNode.getRelationships(Direction.OUTGOING)) {

                String bestCellId = StatisticNeoService.getBestCellId(rel.getType().name());
                if (bestCellId != null) {
                    outRes.clear();
                    Node sector = service.getNodeById(Long.parseLong(bestCellId));
                    String name = (String)sector.getProperty("userLabel", "");
                    if (StringUtil.isEmpty(name)) {
                        name = NeoUtils.getNodeName(sector, service);
                    }

                    StatisticByPeriodStructure structure = analyse.getStatisticStructure(bestCellId);
                    for (IStatisticElementNode statNode : structure.getStatNedes(minMax.getLeft(), minMax.getRight())) {
                        outRes.add(name);
                        calendar.setTimeInMillis(statNode.getStartTime());
                        outRes.add(dateFormat.format(calendar.getTime()));
                        outRes.add(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
                        outRes.add(period.getId());
                        if (NeoArray.hasArray(CellEcNoAnalisis.ARRAY_NAME, statNode.getNode(), service)) {
                            NeoArray array = new NeoArray(statNode.getNode(), CellEcNoAnalisis.ARRAY_NAME, service);
                            for (int i = 21; i <= 104; i++) {
                                Object value = array.getValue(i);
                                if (value == null) {
                                    value = 0;
                                }
                                outRes.add(String.valueOf(value));
                            }
                        } else {
                            for (int i = 0; i <= 49; i++) {
                                outRes.add("0");
                            }
                        }
                        out.writeNext(outRes.toArray(new String[0]));
                        outRes.clear();
                    }

                }
            }
            out.close();
        } catch (IOException e) {
            // TODO Handle IOException
            throw (RuntimeException)new RuntimeException().initCause(e);
        } finally {
            tx.finish();
        }
        return null;
    }
    public SpreadsheetNode createEcNoCellSpreadSheet(String string, CallTimePeriods period) {
        IPath path = ResourcesPlugin.getWorkspace().getRoot().getFullPath().append(
                new Path(new StringBuilder("ECNO_REPORT_").append(period.getId()).append(".csv").toString()));

        File file = path.toFile();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Transaction tx = service.beginTx();
        try {
            CSVWriter out = new CSVWriter(new FileWriter(file));
            CellEcNoAnalisis analyse = getReportModel().getCellEcNoAnalisis(period);
            analyse.getMainNode();
            Node sourceMainNode = analyse.getMainNode();
            Pair<Long, Long> minMax = NeoUtils.getMinMaxTimeOfDataset(gpeh, service);
            // add header
            Calendar calendar = Calendar.getInstance();
            List<String> outRes = new LinkedList<String>();
            outRes.add("Cell Name");
            outRes.add("Date");
            outRes.add("Time");
            outRes.add("Resolution");
            for (int i = 0; i <= 49; i++) {
                outRes.add(new StringBuilder("EcNo=").append(i).append(" (3GPP)").toString());
            }
            out.writeNext(outRes.toArray(new String[0]));
            outRes.clear();
            for (Relationship rel : sourceMainNode.getRelationships(Direction.OUTGOING)) {

                String bestCellId = StatisticNeoService.getBestCellId(rel.getType().name());
                if (bestCellId != null) {
                    outRes.clear();
                    Node sector = service.getNodeById(Long.parseLong(bestCellId));
                    String name = (String)sector.getProperty("userLabel", "");
                    if (StringUtil.isEmpty(name)) {
                        name = NeoUtils.getNodeName(sector);
                    }

                    StatisticByPeriodStructure structure = analyse.getStatisticStructure(bestCellId);
                    for (IStatisticElementNode statNode : structure.getStatNedes(minMax.getLeft(), minMax.getRight())) {
                        outRes.add(name);
                        calendar.setTimeInMillis(statNode.getStartTime());
                        outRes.add(dateFormat.format(calendar.getTime()));
                        outRes.add(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
                        outRes.add(period.getId());
                        if (NeoArray.hasArray(CellEcNoAnalisis.ARRAY_NAME, statNode.getNode(), service)) {
                            NeoArray array = new NeoArray(statNode.getNode(), CellEcNoAnalisis.ARRAY_NAME, service);
                            for (int i = 0; i <= 49; i++) {
                                Object value = array.getValue(i);
                                if (value == null) {
                                    value = 0;
                                }
                                outRes.add(String.valueOf(value));
                            }
                        } else {
                            for (int i = 0; i <= 49; i++) {
                                outRes.add("0");
                            }
                        }
                        out.writeNext(outRes.toArray(new String[0]));
                        outRes.clear();
                    }

                }
            }
            out.close();
        } catch (IOException e) {
            // TODO Handle IOException
            throw (RuntimeException)new RuntimeException().initCause(e);
        } finally {
            tx.finish();
        }
        return null;
    }

    /**
     * The Class StructureCell.
     */
    public static class StructureCell {

        /** The stat elem. */
        final IStatisticElement statElem;

        /** The node. */
        final Node node;

        /** The rncp ar. */
        final int[] rncpAr;

        /**
         * Instantiates a new structure cell.
         * 
         * @param statElem the stat elem
         * @param node the node
         */
        public StructureCell(IStatisticElement statElem, Node node) {
            super();
            this.statElem = statElem;
            this.node = node;
            rncpAr = new int[92];
        }

        /**
         * Store rnsp.
         */
        public void storeRNSP() {
            node.setProperty(CellReportsProperties.RNSP_ARRAY, rncpAr);
        }

        /**
         * Gets the stat elem.
         * 
         * @return the stat elem
         */
        public IStatisticElement getStatElem() {
            return statElem;
        }

        /**
         * Gets the node.
         * 
         * @return the node
         */
        public Node getNode() {
            return node;
        }

        /**
         * Gets the rncp ar.
         * 
         * @return the rncp ar
         */
        public int[] getRncpAr() {
            return rncpAr;
        }

    }

    public static class GPEHStatisticHandler implements IStatisticHandler {

        private final StatisticByPeriodStructure sourceStruc;

        /**
         * @param sourceStruc
         */
        public GPEHStatisticHandler(StatisticByPeriodStructure sourceStruc) {
            this.sourceStruc = sourceStruc;
        }

        @Override
        public IStatisticElement getStatisics(Long periodTime, Long periodEnd) {
            return new StatisticSetElement(periodTime, periodEnd, null, sourceStruc.getStatNedes(periodTime, periodEnd));
        }

    }

    public static class StatisticSetElement implements IStatisticElement {

        private final long endTime;
        private final CallTimePeriods period;
        private final long startTime;
        private final Set<IStatisticElementNode> sources;

        public StatisticSetElement(long startTime, long endTime, CallTimePeriods period, Set<IStatisticElementNode> sources) {
            super();
            this.endTime = endTime;
            this.period = period;
            this.startTime = startTime;
            this.sources = sources;
        }

        @Override
        public long getEndTime() {
            return endTime;
        }

        @Override
        public CallTimePeriods getPeriod() {
            return period;
        }

        @Override
        public long getStartTime() {
            return startTime;
        }

        /**
         * @return Returns the sources.
         */
        public Set<IStatisticElementNode> getSources() {
            return sources;
        }

    }

    public class GPEHRSCPStorer implements IStatisticStore {

        @Override
        public void storeStatisticElement(IStatisticElement statElem, Node node) {
            StatisticSetElement source = (StatisticSetElement)statElem;
            NeoArray array = new NeoArray(node, CellRscpAnalisis.ARRAY_NAME, 1, service);

            Set<NeoArray> arraySet = new HashSet<NeoArray>(0);
            for (IStatisticElementNode singlElement : source.getSources()) {
                if (NeoArray.hasArray(CellRscpEcNoAnalisis.ARRAY_NAME, singlElement.getNode(), service)) {
                    arraySet.add(new NeoArray(singlElement.getNode(), CellRscpEcNoAnalisis.ARRAY_NAME, 2, service));
                }
            }
            for (int rscp = 0; rscp <= 91; rscp++) {
                Node arrayNode = null;
                int count = 0;
                for (int ecNo = 0; ecNo <= 49; ecNo++) {
                    for (NeoArray sArray : arraySet) {
                        Node sourceNode = sArray.getNode(rscp, ecNo);
                        if (sourceNode != null) {
                            Object value = sArray.getValueFromNode(sourceNode);
                            if (value != null) {
                                count += (Integer)value;
                                if (arrayNode == null && count >= 0) {
                                    arrayNode = array.findOrCreateNode(rscp);
                                    arrayNode.createRelationshipTo(sourceNode, ReportsRelations.SOURCE);
                                }
                            }
                        }
                    }
                }
                if (arrayNode != null) {
                    array.setValueToNode(arrayNode, count);
                }
            }
        }
    }

    public class GPEHUeTxPowerStorer implements IStatisticStore {

        @Override
        public void storeStatisticElement(IStatisticElement statElem, Node node) {
            StatisticSetElement source = (StatisticSetElement)statElem;
            NeoArray array = new NeoArray(node, CellRscpAnalisis.ARRAY_NAME, 1, service);

            Set<NeoArray> arraySet = new HashSet<NeoArray>(0);
            for (IStatisticElementNode singlElement : source.getSources()) {
                if (NeoArray.hasArray(CellRscpEcNoAnalisis.ARRAY_NAME, singlElement.getNode(), service)) {
                    arraySet.add(new NeoArray(singlElement.getNode(), CellUeTxPowerAnalisis.ARRAY_NAME, 1, service));
                }
            }
            for (int txPow = 21; txPow <= 104; txPow++) {
                Node arrayNode = null;
                int count = 0;
                    for (NeoArray sArray : arraySet) {
                    Node sourceNode = sArray.getNode(txPow);
                        if (sourceNode != null) {
                            Object value = sArray.getValueFromNode(sourceNode);
                            if (value != null) {
                                count += (Integer)value;
                                if (arrayNode == null && count >= 0) {
                                arrayNode = array.findOrCreateNode(txPow);
                                    arrayNode.createRelationshipTo(sourceNode, ReportsRelations.SOURCE);
                                }
                            }
                        }
                }
                if (arrayNode != null) {
                    array.setValueToNode(arrayNode, count);
                }
            }
        }
    }

    public class GPEHEcNoStorer implements IStatisticStore {

        @Override
        public void storeStatisticElement(IStatisticElement statElem, Node node) {
            StatisticSetElement source = (StatisticSetElement)statElem;
            NeoArray array = new NeoArray(node, CellEcNoAnalisis.ARRAY_NAME, 1, service);

            Set<NeoArray> arraySet = new HashSet<NeoArray>(0);
            for (IStatisticElementNode singlElement : source.getSources()) {
                if (NeoArray.hasArray(CellRscpEcNoAnalisis.ARRAY_NAME, singlElement.getNode(), service)) {
                    arraySet.add(new NeoArray(singlElement.getNode(), CellRscpEcNoAnalisis.ARRAY_NAME, 2, service));
                }
            }
            for (int ecNo = 0; ecNo <= 49; ecNo++) {
                Node arrayNode = null;
                int count = 0;
                for (int rscp = 0; rscp <= 91; rscp++) {
                    for (NeoArray sArray : arraySet) {
                        Node sourceNode = sArray.getNode(rscp, ecNo);
                        if (sourceNode != null) {
                            Object value = sArray.getValueFromNode(sourceNode);
                            if (value != null) {
                                count += (Integer)value;
                                if (arrayNode == null && count >= 0) {
                                    arrayNode = array.findOrCreateNode(ecNo);
                                    arrayNode.createRelationshipTo(sourceNode, ReportsRelations.SOURCE);
                                }
                            }
                        }
                    }
                }
                if (arrayNode != null) {
                    array.setValueToNode(arrayNode, count);
                }
            }
        }
    }

    public class GPEHRscpEcNoStorer implements IStatisticStore {

        @Override
        public void storeStatisticElement(IStatisticElement statElem, Node node) {
            StatisticSetElement source = (StatisticSetElement)statElem;
            NeoArray array = new NeoArray(node, CellRscpEcNoAnalisis.ARRAY_NAME, 2, service);

            Set<NeoArray> arraySet = new HashSet<NeoArray>(0);
            for (IStatisticElementNode singlElement : source.getSources()) {
                if (NeoArray.hasArray(CellRscpEcNoAnalisis.ARRAY_NAME, singlElement.getNode(), service)) {
                    arraySet.add(new NeoArray(singlElement.getNode(), CellRscpEcNoAnalisis.ARRAY_NAME, 2, service));
                }
            }
            for (int ecNo = 0; ecNo <= 49; ecNo++) {
                int count = 0;
                for (int rscp = 0; rscp <= 91; rscp++) {
                    Node arrayNode = null;
                    for (NeoArray sArray : arraySet) {
                        Node sourceNode = sArray.getNode(rscp, ecNo);
                        if (sourceNode != null) {
                            Object value = sArray.getValueFromNode(sourceNode);
                            if (value != null) {
                                count += (Integer)value;
                                if (arrayNode == null && count >= 0) {
                                    arrayNode = array.findOrCreateNode(rscp, ecNo);
                                    arrayNode.createRelationshipTo(sourceNode, ReportsRelations.SOURCE);
                                }
                            }
                        }
                    }
                    if (arrayNode != null) {
                        array.setValueToNode(arrayNode, count);
                    }
                }

            }
        }
    }


    public void createUeTxPowerCellReport(CallTimePeriods periods) {
        if (model.getCellUeTxPowerAnalisis(periods) != null) {
            return;
        }
        assert !"main".equals(Thread.currentThread().getName());
        Transaction tx = service.beginTx();
        try {
            createMatrix();
            Node parentNode = service.createNode();
            parentNode.setProperty(CellReportsProperties.PERIOD_ID, periods.getId());
            model.getRoot().createRelationshipTo(parentNode, periods.getPeriodRelation(CellUeTxPowerAnalisis.PRFIX));
            CellRscpEcNoAnalisis sourceModel = model.getCellRscpEcNoAnalisis(CallTimePeriods.HOURLY);
            Node sourceMainNode = sourceModel.getMainNode();
            Pair<Long, Long> minMax = NeoUtils.getMinMaxTimeOfDataset(gpeh, service);
            for (Relationship rel : sourceMainNode.getRelationships(Direction.OUTGOING)) {
                String bestCellId = StatisticNeoService.getBestCellId(rel.getType().name());
                if (bestCellId != null) {
                    StatisticByPeriodStructure sourceStruc = new StatisticByPeriodStructure(rel.getOtherNode(sourceMainNode), service);
                    GPEHStatisticHandler handler = new GPEHStatisticHandler(sourceStruc);
                    GPEHUeTxPowerStorer store = new GPEHUeTxPowerStorer();
                    TimePeriodStructureCreator creator = new TimePeriodStructureCreator(parentNode, bestCellId, minMax.getLeft(), minMax.getRight(),
                            periods, handler, store, service);
                    creator.createStructure();
                }
            }
            model.findCellUeTxPowerAnalisis(periods);
            tx.success();
        } finally {
            tx.finish();
        }

    }
}
