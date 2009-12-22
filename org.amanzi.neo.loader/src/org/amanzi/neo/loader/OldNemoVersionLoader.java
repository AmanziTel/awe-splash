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

package org.amanzi.neo.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.amanzi.neo.core.INeoConstants;
import org.amanzi.neo.core.enums.GeoNeoRelationshipTypes;
import org.amanzi.neo.loader.internal.NeoLoaderPlugin;
import org.eclipse.swt.widgets.Display;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Transaction;

/**
 * <p>
 * NeoLoader for old version nemo file format
 * </p>
 * 
 * @author Cinkel_A
 * @since 1.0.0
 */
public class OldNemoVersionLoader extends NemoLoader {
    /** String VELOCITY field */
    private static final String VELOCITY = "velocity";
    /** String SATELITES field */
    private static final String SATELITES = "satelites";
    /** String GPS_STATUS field */
    private static final String GPS_STATUS = "gps_status";
    /** String DISTANCE field */
    private static final String DISTANCE = "distance";
    /** String HEIGHT field */
    private static final String HEIGHT = "height";
    protected String latLong = null;

    /**
     * @param filename
     * @param display
     * @param dataset
     */
    public OldNemoVersionLoader(String filename, Display display, String dataset) {
        super(filename, display, dataset);
        possibleFieldSepRegexes = new char[] {' ', '\t', ',', ';'};
    }

    @Override
    protected void parseLine(String line) {
        try {
            if (line.startsWith("*") || line.startsWith("#")) {
                NeoLoaderPlugin.error("Not parsed: " + line);
                return;
            }
        if (parser == null) {
            determineFieldSepRegex(line);
        }

        List<String> parsedLine = parser.parse(line);
        if (parsedLine.size() < 1) {
            return;
        }
        OldEvent event = new OldEvent(parsedLine);
        try {
            event.analyseKnownParameters(headers);
        } catch (Exception e) {
            e.printStackTrace();
            NeoLoaderPlugin.error(e.getLocalizedMessage());
            return;
        }

        String latLon = event.latitude + "\t" + event.longitude;
        if (latLong != null && latLong.equals(latLon)) {
            createMsNode(event);
        } else {
            if (Double.parseDouble(event.latitude) == 0 && Double.parseDouble(event.longitude) == 0) {
                NeoLoaderPlugin.error("Not parsed: " + line);
                return;
            }
            latLong = latLon;
            createPointNode(event);
        }
        } catch (Exception e) {
            e.printStackTrace();
            NeoLoaderPlugin.error("Not parsed: " + line);
        }

    }

    @Override
    protected void createPointNode(Event events) {
        OldEvent event = (OldEvent)events;
        Transaction transaction = neo.beginTx();
        try {
            Float lon = Float.parseFloat(event.longitude);
            Float lat = Float.parseFloat(event.latitude);
            String time = event.time;
            if (lon == null || lat == null) {
                return;
            }
            long timestamp = timeFormat.parse(time).getTime();
            Node mp = neo.createNode();
            mp.setProperty(INeoConstants.PROPERTY_TYPE_NAME, INeoConstants.MP_TYPE_NAME);
            mp.setProperty(INeoConstants.PROPERTY_TIME_NAME, time);
            mp.setProperty(INeoConstants.PROPERTY_TIMESTAMP_NAME, timestamp);
            mp.setProperty(INeoConstants.PROPERTY_LAT_NAME, lat.doubleValue());
            mp.setProperty(INeoConstants.PROPERTY_LON_NAME, lon.doubleValue());
            findOrCreateFileNode(mp);
            updateBBox(lat, lon);
            checkCRS((float)lat, (float)lon, null);
            // debug("Added measurement point: " + propertiesString(mp));
            if (pointNode != null) {
                pointNode.createRelationshipTo(mp, GeoNeoRelationshipTypes.NEXT);
            }
            index(mp);
            transaction.success();
            pointNode = mp;
            msNode = null;
            createMsNode(event);
        } catch (Exception e) {
            e.printStackTrace();
            NeoLoaderPlugin.error(e.getLocalizedMessage());
            return;
        } finally {
            transaction.finish();
        }
    }

    @Override
    protected void initializeKnownHeaders() {
        super.initializeKnownHeaders();
        headers.put(HEIGHT, new IntegerHeader(new Header(HEIGHT, HEIGHT, 3)));
        headers.put(DISTANCE, new IntegerHeader(new Header(DISTANCE, DISTANCE, 4)));
        headers.put(GPS_STATUS, new IntegerHeader(new Header(GPS_STATUS, GPS_STATUS, 5)));
        headers.put(SATELITES, new IntegerHeader(new Header(SATELITES, SATELITES, 6)));
        headers.put(VELOCITY, new IntegerHeader(new Header(VELOCITY, VELOCITY, 4)));

    }

    public class OldEvent extends Event {

        private String longitude;
        private String latitude;
        private String height;
        private String distance;
        private String GPSstatus;
        private String satelites;
        private String velocity;

        /**
         * @param parcedLine
         */
        public OldEvent(List<String> parcedLine) {
            super(parcedLine);
        }

        @Override
        protected void parse(List<String> parcedLine) {
            eventId = parcedLine.get(0);
            longitude = parcedLine.get(1);
            latitude = parcedLine.get(2);
            height = parcedLine.get(3);
            distance = parcedLine.get(4);
            GPSstatus = parcedLine.get(5);
            satelites = parcedLine.get(6);
            velocity = parcedLine.get(7);
            time = parcedLine.get(8);
            event = NemoEvents.getEventById(eventId);
            contextId = null;
            parameters = new ArrayList<String>();
            for (int i = 9; i < parcedLine.size(); i++) {
                parameters.add(parcedLine.get(i));
            }
        }

        @Override
        public void store(Node msNode, Map<String, Header> statisticHeaders) {
            storeProperties(msNode, EVENT_ID, eventId, statisticHeaders);
            storeProperties(msNode, INeoConstants.PROPERTY_TIME_NAME, time, statisticHeaders);
            // TODO store header if necessary
            for (String key : parsedParameters.keySet()) {
                storeProperties(msNode, key, parsedParameters.get(key), statisticHeaders);
            }
        }

        @Override
        protected String getVersion() {
            return "1.86";
        }
    }
}
