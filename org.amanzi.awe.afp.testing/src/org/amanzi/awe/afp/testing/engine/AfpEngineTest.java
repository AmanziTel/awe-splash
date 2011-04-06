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

package org.amanzi.awe.afp.testing.engine;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.amanzi.awe.afp.executors.AfpProcessExecutor;
import org.amanzi.awe.afp.exporters.AfpExporter;
import org.amanzi.awe.afp.models.AfpModel;
import org.amanzi.awe.afp.testing.engine.AfpModelFactory.AfpScenario;
import org.amanzi.awe.afp.testing.engine.TestDataLocator.DataType;
import org.amanzi.neo.db.manager.DatabaseManager;
import org.amanzi.neo.loader.ui.preferences.DataLoadPreferenceInitializer;
import org.amanzi.neo.services.ui.NeoServiceProviderUi;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;

/**
 * TODO Purpose of 
 * <p>
 *
 * </p>
 * @author gerzog
 * @since 1.0.0
 */
public class AfpEngineTest {
    
    private static Logger LOGGER = Logger.getLogger(AfpEngineTest.class);
    
    private static GraphDatabaseService graphDatabaseService;
    
    private static ArrayList<IDataset> datasets = new ArrayList<IDataset>();
    
    private static long startTimestamp;
    
    private static HashMap<IDataset, HashMap<AfpScenario, AfpModel>> scenarios = new HashMap<IDataset, HashMap<AfpScenario, AfpModel>>();
    
    /**
     *
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        startTimestamp = System.currentTimeMillis();
        LOGGER.info("Set up AFP Engine Test");
        
        try {
            initEnvironment();
            loadDataset();
            exportInputFiles();
            runEngine();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        NeoServiceProviderUi.getProvider().getIndexService().shutdown();
        graphDatabaseService.shutdown();
         
        clearDb();
        
        long duration = System.currentTimeMillis() - startTimestamp;
        int seconds = (int)(duration / 1000 % 60 );
        int minutes = (int)(duration / 1000 / 60 % 60);
        int hours = (int)(duration / 1000 / 60 / 60 % 24);
        LOGGER.info("Test finished. Test time - " + hours + " hours " + 
                                                    minutes + " minutes " + 
                                                    seconds + " seconds");
    }

    /**
     *
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     *
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }
    
    /**
     * Clears Database Directory
     */
    private static void clearDb() {
        deleteDirectory(new File(getDbLocation()));
    }
    
    private static void deleteDirectory(File directory) {
        for (File subFile : directory.listFiles()) {
            if (subFile.isDirectory()) {
                deleteDirectory(subFile);
            }            
            else {
                subFile.delete();
            }
        }
        directory.delete();
    }
    
    /**
     * Initialized Database on selected Directory
     */
    private static void initializeDb() {
        LOGGER.info("Initialize Database");
        graphDatabaseService = new EmbeddedGraphDatabase(getDbLocation());
        NeoServiceProviderUi.initProvider(graphDatabaseService, getDbLocation());
        DatabaseManager.setDatabaseAndIndexServices(graphDatabaseService, NeoServiceProviderUi.getProvider().getIndexService());
        LOGGER.info("Database was successfully initialized");
    }
    
    private static String getDbLocation() {
        return System.getProperty("user.home") + File.separator + ".amanzi" + File.separator + "neo_test";
    }
    
    private static void loadDataset() {
        LOGGER.info("Load Datasets");
        for (IDataset loader : datasets) {
            if (loader != null) {
                loader.run();
            }
        }
    }
    
    private static void initEnvironment() throws IOException {
        initializeDb();
        initPreferences();
        
        LOGGER.info("Initialize Test datasets");
        for (DataType singleType : DataType.values()) {
            datasets.add(getDatasetLoader(singleType));
        }
    }
    
    private static void initPreferences() {
        LOGGER.info("Load Preferences");
        DataLoadPreferenceInitializer initializer = new DataLoadPreferenceInitializer();
        initializer.initializeDefaultPreferences();
    }
    
    private static IDataset getDatasetLoader(DataType dataType) throws IOException {
        switch (dataType) {
        case ERICSSON:
            return new LoadEricssonDataAction("project");
        case GENERAL_FORMAT:
            return null;
        case GERMANY:
            return new LoadGermanyDataAction("project");
        }
        
        return null;
    }
    
    private static void exportInputFiles() {
        LOGGER.info("Export input files for AFP Engine");
        for (IDataset dataset : datasets) {
            if (dataset == null) {
                continue;
            }
            for (AfpScenario scenario : AfpScenario.values()) {
                AfpModel model = dataset.getAfpModel(scenario);
                AfpExporter exporter = model.getExporter();
                
                LOGGER.info("Writing files for Dataset <" + dataset.getName() + "> using " + scenario.name() + " scenario");
                long before = System.currentTimeMillis();
                exporter.run(null);
                long after = System.currentTimeMillis();
                LOGGER.info("Writing finished in " + (after - before) + " milliseconds");
                
                model.saveUserData();
                model.executeAfpEngine(null, exporter);
                
                if (!scenarios.containsKey(dataset)) {
                    scenarios.put(dataset, new HashMap<AfpScenario, AfpModel>());
                }
                scenarios.get(dataset).put(scenario, model);
            }
        }
    }
    
    private static void runEngine() {
        LOGGER.info("Running AFP Engine");
        
        for (IDataset dataset : scenarios.keySet()) {
            for (AfpScenario scenario : scenarios.get(dataset).keySet()) {
                AfpModel model = scenarios.get(dataset).get(scenario);
                
                AfpProcessExecutor executor = model.getExecutor();
                LOGGER.info("AFP Engine started for dataset <" + dataset.getName() + "> with " + scenario.name() + " scenario");
                long before = System.currentTimeMillis();
                executor.run(null);
                long after = System.currentTimeMillis();
                LOGGER.info("AFP Engine finished. Spent time - " + toHourTime(after - before));
            }
        }
    }
    
    private static String toHourTime(long milliseconds) {
        int seconds = (int)(milliseconds / 1000 % 60 );
        int minutes = (int)(milliseconds / 1000 / 60 % 60);
        int hours = (int)(milliseconds / 1000 / 60 / 60 % 24);
        return hours + " hours " + 
               minutes + " minutes " + 
               seconds + " seconds";
        
    }
    @Test
    public void test1() {
        
    }

}