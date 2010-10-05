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

package org.amanzi.awe.views.network.view;

import java.util.Map;

import org.amanzi.neo.core.INeoConstants;
import org.amanzi.neo.core.enums.INodeType;
import org.amanzi.neo.core.enums.NodeTypes;
import org.amanzi.neo.core.service.NeoServiceProvider;
import org.amanzi.neo.core.utils.EditPropertiesPage;
import org.amanzi.neo.core.utils.NeoUtils;
import org.amanzi.neo.services.statistic.IPropertyHeader;
import org.amanzi.neo.services.statistic.PropertyHeader;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.neo4j.graphdb.Node;
import org.neo4j.index.lucene.LuceneIndexService;

/**
 * <p>
 * </p>
 * 
 * @author NiCK
 * @since 1.0.0
 */
public class CreateNewNodeWizardPage extends EditPropertiesPage {

    /** int VIEVER_HEIGHT_HINT field */
    private static final int VIEVER_HEIGHT_HINT = 150;
    private final Node sourceNode;

    /**
     * @param pageName
     * @param title
     * @param nodeType
     */
    public CreateNewNodeWizardPage(String pageName, String title, INodeType nodeType, Node sourceNode) {
        super(pageName, title, nodeType);
        this.sourceNode = sourceNode;
    }

    @Override
    protected void initProperty() {
        // super.initProperty();
        // propertyList.add(new PropertyWrapper("lat", Double.class, "", false));

        NodeTypes type = NodeTypes.getEnumById(nodeType.getId());
        propertyList.add(new NewNodePropertyWrapper("name", String.class, "new " + nodeType.getId(), true));
        if (type != null) {
            if (type == NodeTypes.SECTOR) {
                propertyList.add(new NewNodePropertyWrapper(INeoConstants.PROPERTY_SECTOR_CI, Integer.class, "0", true));
                propertyList.add(new NewNodePropertyWrapper(INeoConstants.PROPERTY_SECTOR_LAC, Integer.class, "0", true));
            }
            IPropertyHeader ph = PropertyHeader.getPropertyStatistic(NeoUtils.getParentNode(sourceNode, NodeTypes.NETWORK.getId()));
            Map<String, Object> statisticProperties = ph.getStatisticParams(type);
            for (String key : statisticProperties.keySet()) {
                propertyList.add(new NewNodePropertyWrapper(key, statisticProperties.get(key).getClass(), statisticProperties.get(key).toString(), false));
            }
        }
    }

    /**
     * validate properties
     */
    @Override
    protected void validate() {
        super.validate();

        for (int i = 0; i < propertyList.size(); i++) {
            PropertyWrapper wr = propertyList.get(i);
            if (wr.getName() == INeoConstants.PROPERTY_NAME_NAME) {
                LuceneIndexService luceneInd = NeoServiceProvider.getProvider().getIndexService();
                if (luceneInd.getNodes(INeoConstants.PROPERTY_NAME_NAME, wr.getName()).size() > 0) {
                    setDescription(String.format("Node with the name '%s' is alredy exist", wr.getDefValue()));
                    setPageComplete(false);
                    return;
                }
            }
        }

        if (nodeType == NodeTypes.SECTOR) {
            PropertyWrapper ciWr = null;
            PropertyWrapper lacWr = null;
            for (int i = 0; i < propertyList.size(); i++) {
                if (propertyList.get(i).getName() == INeoConstants.PROPERTY_SECTOR_CI)
                    ciWr = propertyList.get(i);
                if (propertyList.get(i).getName() == INeoConstants.PROPERTY_SECTOR_LAC)
                    lacWr = propertyList.get(i);
            }
            try {
                Node sector = NeoUtils.findSector(NeoUtils.getParentNode(sourceNode, NodeTypes.NETWORK.getId()), Integer.valueOf(ciWr.getDefValue()), Integer
                        .valueOf(lacWr.getDefValue()), null, true, NeoServiceProvider.getProvider().getIndexService(), NeoServiceProvider.getProvider().getService());
                if (sector != null) {
                    setDescription(String.format("Sector node with CI = '%s' and LAC = '%s' is alredy exist", ciWr.getDefValue(), lacWr.getDefValue()));
                    setPageComplete(false);
                    return;
                }
            } catch (Exception e) {
                setDescription(e.getMessage());
                e.printStackTrace();
                setPageComplete(false);
                return;
            }
        }

        setDescription(getNormalDescription());
        setPageComplete(true);
        return;
    }

    /**
     * The Class NewNodePropertyWrapper.
     */
    protected class NewNodePropertyWrapper extends PropertyWrapper {

        /**
         * Instantiates a new property wrapper.
         * 
         * @param name the name
         * @param type the type
         * @param defValue the def value
         * @param editable the editable
         */
        public NewNodePropertyWrapper(String name, Class< ? > type, String defValue, boolean editable) {
            super(name, type, defValue, editable);
        }

    }

    @Override
    public void createControl(Composite parent) {
        super.createControl(parent);
        ((GridData)viewer.getTable().getLayoutData()).heightHint = VIEVER_HEIGHT_HINT;
    }

}