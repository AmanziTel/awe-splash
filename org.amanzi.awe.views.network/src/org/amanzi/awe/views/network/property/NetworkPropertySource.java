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
package org.amanzi.awe.views.network.property;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.refractions.udig.catalog.IGeoResource;
import net.refractions.udig.project.ILayer;
import net.refractions.udig.project.IMap;
import net.refractions.udig.project.ui.ApplicationGIS;

import org.amanzi.awe.views.network.proxy.NeoNode;
import org.amanzi.neo.core.NeoCorePlugin;
import org.amanzi.neo.core.service.NeoServiceProvider;
import org.amanzi.neo.core.utils.NeoUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Transaction;
import org.neo4j.neoclipse.property.NodePropertySource;
import org.neo4j.neoclipse.property.PropertyDescriptor;
import org.neo4j.neoclipse.property.PropertyTransform;
import org.neo4j.neoclipse.property.PropertyTransform.PropertyHandler;

/**
 * Class that creates a properties of given Node
 * 
 * @author Lagutko_N
 * @since 1.0.0
 */

public class NetworkPropertySource extends NodePropertySource implements IPropertySource {
    private boolean isDeltaNode;
    Pattern pattern = Pattern.compile("Delta (\\w+)\\s+(.*)");

    public NetworkPropertySource(NeoNode node) {
        super(node.getNode(), null);
        isDeltaNode = node.getNode().getProperty("type","").toString().startsWith("delta_");
    }

    /**
     * Returns the descriptors for the properties of the node.
     */
    public IPropertyDescriptor[] getPropertyDescriptors() {
        List<IPropertyDescriptor> descs = new ArrayList<IPropertyDescriptor>();
        descs.addAll(getHeadPropertyDescriptors());
        Iterable<String> keys = container.getPropertyKeys();
        for (String key : keys) {
            Object value = container.getProperty((String)key);
            Class< ? > c = value.getClass();
            if(isDeltaNode && key.startsWith("Delta ")) {
                String name = key.replace("Delta ", "");
                String category = "Changes for";
                Matcher matcher = pattern.matcher(key);
                if(matcher.matches()) {
                    name = matcher.group(2);
                    category = category + " " + matcher.group(1);
                } else if(container.hasProperty("name")) {
                    category = category + " " + container.getProperty("name");
                }
                descs.add(new PropertyDescriptor(key, name, category, c));
            } else {
                descs.add(new PropertyDescriptor(key, key, PROPERTIES_CATEGORY, c));
            }
        }
        return descs.toArray(new IPropertyDescriptor[descs.size()]);
    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        if (!((String)id).startsWith("delta_")) {
            Transaction tx = NeoServiceProvider.getProvider().getService().beginTx();
            try {
                if (container.hasProperty((String)id)) {
                    // try to keep the same type as the previous value
                    Class< ? > c = container.getProperty((String)id).getClass();
                    PropertyHandler propertyHandler = PropertyTransform.getHandler(c);
                    if (propertyHandler == null) {
                        MessageDialog.openError(null, "Error", "No property handler was found for type " + c.getSimpleName() + ".");
                        return;
                    }
                    Object o = null;
                    try {
                        o = propertyHandler.parse(value);
                    } catch (Exception e) {
                        MessageDialog.openError(null, "Error", "Could not parse the input as type " + c.getSimpleName() + ".");
                        return;
                    }
                    if (o == null) {
                        MessageDialog.openError(null, "Error", "Input parsing resulted in null value.");
                        return;
                    }
                    try {
                        container.setProperty((String)id, o);
                    } catch (Exception e) {
                        MessageDialog.openError(null, "Error", "Error in Neo service: " + e.getMessage());
                    }
                } else {
                    // simply set the value
                    try {
                        container.setProperty((String)id, value);
                    } catch (Exception e) {
                        MessageDialog.openError(null, "Error", "Error in Neo service: " + e.getMessage());
                    }
                }
                tx.success();
                updateLayer();
            } finally {
                tx.finish();
                NeoServiceProvider.getProvider().commit();
            }
        }
    }

    /**
     *updates layer
     */
    private void updateLayer() {
        final IMap map = ApplicationGIS.getActiveMap();
        if (map == ApplicationGIS.NO_MAP) {
            return;
        }
        Job job = new Job("update layers") {

            @Override
            protected IStatus run(IProgressMonitor monitor) {
                Transaction tx = NeoServiceProvider.getProvider().getService().beginTx();
                NeoUtils.addTransactionLog(tx, Thread.currentThread(), "updateLayer");
                try {
                    Node gisNode = NeoUtils.findGisNodeByChild((Node)container);
                    if (gisNode != null) {
                        ILayer layer = findLayerByNode(map, gisNode);
                        if (layer != null) {
                            layer.refresh(null);
                        }
                    }
                    return Status.OK_STATUS;
                } finally {
                    tx.finish();
                }
            }

        };
        job.schedule();
    }

    // TODO move to utility class
    /**
     *Returns layer, that contains necessary gis node
     * 
     * @param map map
     * @param gisNode gis node
     * @return layer or null
     */
    public static ILayer findLayerByNode(IMap map, Node gisNode) {
        try {
            for (ILayer layer : map.getMapLayers()) {
                IGeoResource resource = layer.findGeoResource(Node.class);
                if (resource != null && resource.resolve(Node.class, null).equals(gisNode)) {
                    return layer;
                }
            }
            return null;
        } catch (IOException e) {
            NeoCorePlugin.error(e.getLocalizedMessage(), e);
            return null;
        }
    }
}
