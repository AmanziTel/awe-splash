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
package org.amanzi.awe.catalog.neo;

import java.io.IOException;

import net.refractions.udig.catalog.IGeoResource;
import net.refractions.udig.project.ILayerListener;
import net.refractions.udig.project.LayerEvent;
import net.refractions.udig.project.LayerEvent.EventType;
import net.refractions.udig.project.internal.Layer;

import org.amanzi.neo.core.INeoConstants;
import org.amanzi.neo.core.service.NeoServiceProvider;
import org.amanzi.neo.core.utils.ActionUtil;
import org.amanzi.neo.core.utils.ActionUtil.RunnableWithResult;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Transaction;

/**
 * <p>
 * Layer created interceptor
 * </p>
 * 
 * @author Cinkel_A
 * @since 1.0.0
 */
public class LayerInterceptor implements net.refractions.udig.project.interceptor.LayerInterceptor {

    protected static final String RENAME_TITLE = "Rename catalog entry";
    protected static final String RENAME_MESSAGE = "Do you also wish to rename the catalog entry\nfrom '%s' to '%s'?";

    @Override
    public void run(Layer layer) {
        if (layer.findGeoResource(NeoGeoResource.class) != null) {
            final Layer fLayer = layer;
            ILayerListener renameNameListener = new ILayerListener() {
                @Override
                public void refresh(LayerEvent event) {
                    if (event.getType() == EventType.NAME) {
                        changeGisName(fLayer);
                    }
                }
            };
            layer.addListener(renameNameListener);
        }

    }

    /**
     * change gis name depends of layer name
     * 
     * @param layer layer
     */
    protected void changeGisName(final Layer layer) {
        IProgressMonitor monitor = new NullProgressMonitor();
        IGeoResource resource = layer.findGeoResource(NeoGeoResource.class);
        Transaction tx = NeoServiceProvider.getProvider().getService().beginTx();
        try {
            NeoGeoResource geoRes = resource.resolve(NeoGeoResource.class, monitor);
            Node gisNode = geoRes.resolve(Node.class, monitor);
            String oldName = gisNode.getProperty(INeoConstants.PROPERTY_NAME_NAME).toString();
            String name = layer.getName().trim();
            if (name.equals(oldName)) {
                return;
            }
            if (name.isEmpty() || !askToRename(oldName, name)) {
                return;
            }
            gisNode.setProperty(INeoConstants.PROPERTY_NAME_NAME, name);

            NeoServiceProvider.getProvider().commit();
            layer.setID(geoRes.getIdentifier());

        } catch (IOException e) {
            throw (RuntimeException)new RuntimeException().initCause(e);
        } finally {
            tx.finish();
        }
    }

    /**
     * Ask user to rename gis node
     * 
     * @param name new name
     * @return true if user agree
     */
    private boolean askToRename(final String oldName, final String newName) {
        return ActionUtil.getInstance().runTaskWithResult(new RunnableWithResult<Integer>() {
            int result;

            @Override
            public void run() {
                String message = String.format(RENAME_MESSAGE, oldName, newName);
                MessageBox msg = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.YES | SWT.NO);
                msg.setText(RENAME_TITLE);
                msg.setMessage(message);
                result = msg.open();
            }

            @Override
            public Integer getValue() {
                return result;
            }
        }) == SWT.YES;
    }

}
