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

package org.amanzi.neo.services.enums;

import org.amanzi.neo.services.utils.Utils;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Transaction;

/**
 * <p>
 * Network site type
 * </p>
 * @author Cinkel_A
 * @since 1.0.0
 */
public enum NetworkSiteType {
    SITE_2G("site_2g"), SITE_3G("site_3g");
    public static final String PROPERTY_NAME = "site_type";
    private final String id;


    private NetworkSiteType(String id) {
        this.id = id;
    }

    /**
     * Returns NetworkTypes by its ID
     * 
     * @param enumId id of Node Type
     * @return NodeTypes or null
     */
    public static NetworkSiteType getEnumById(String enumId) {
        if (enumId == null) {
            return null;
        }
        for (NetworkSiteType oss : NetworkSiteType.values()) {
            if (oss.getId().equals(enumId)) {
                return oss;
            }
        }
        return null;
    }

    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }

    /**
     * returns type of node
     * 
     * @param container PropertyContainer
     * @param service NeoService
     * @return type of node
     */
    public static NetworkSiteType getNetworkSiteType(PropertyContainer networkGis, GraphDatabaseService service) {
        Transaction tx = service == null ? null : service.beginTx();
        try {
            return getEnumById((String)networkGis.getProperty(PROPERTY_NAME, null));
        } finally {
            if (service != null) {
                tx.finish();
            }
        }
    }
    /**
     * returns type of node
     * 
     * @param container PropertyContainer
     * @param service NeoService
     * @return type of node
     */
    public void setSiteType(PropertyContainer container, GraphDatabaseService service) {
        Transaction tx = Utils.beginTx(service);
        try {
            container.setProperty(PROPERTY_NAME,getId());
            Utils.successTx(tx);
        } finally {
            Utils.finishTx(tx);
        }
    }

    /**
     *check node by necessary type
     * @param container - node
     * @param service - neo service
     * @return result of checking
     */
    public boolean checkNode(PropertyContainer container, GraphDatabaseService service) {
        Transaction tx = Utils.beginTx(service);
        try {
            return getId().equals(container.getProperty(PROPERTY_NAME,""));
        } finally {
            Utils.finishTx(tx);
        }
    }
}