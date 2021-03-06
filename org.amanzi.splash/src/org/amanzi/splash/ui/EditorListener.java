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
/**
 * 
 */
package org.amanzi.splash.ui;

import java.util.Arrays;
import java.util.LinkedList;

import org.amanzi.neo.services.AweProjectService;
import org.amanzi.neo.services.NeoServiceFactory;
import org.amanzi.neo.services.nodes.CellNode;
import org.amanzi.neo.services.nodes.RubyProjectNode;
import org.amanzi.neo.services.nodes.RubyScriptNode;
import org.amanzi.neo.services.ui.NeoServicesUiPlugin;
import org.amanzi.neo.services.ui.utils.ActionUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;

/**
 * Listener for change ruby script
 * 
 * @author Cinkel_A
 */
public class EditorListener implements IResourceChangeListener {

    public void resourceChanged(IResourceChangeEvent event) {
        IResourceDelta delta = event.getDelta();
        LinkedList<IResourceDelta> listResourceDelta = new LinkedList<IResourceDelta>();
        listResourceDelta.add(delta);
        String projectName = null;
        for (int i = 0; i < listResourceDelta.size(); i++) {
            final IResourceDelta resourceDelta = listResourceDelta.get(i);
            IResource resource = resourceDelta.getResource();
            if (resource instanceof IProject) {
                projectName = resource.getName();
            }
            if ("rb".equals(resourceDelta.getFullPath().getFileExtension())
                    && ((resourceDelta.getFlags() & IResourceDelta.CONTENT) != 0)) {
                ActionUtil.getInstance().runTask(new UpdateScript(projectName, resourceDelta), false);
            }
            listResourceDelta.addAll(Arrays.asList(resourceDelta.getAffectedChildren(IResourceDelta.CHANGED)));
        }

    }

    /**
     * Runnable class for update script in database
     * 
     * @author Cinkel_A
     */
    private static class UpdateScript implements Runnable {

        private final String projectName;
        private final String scriptName;

        public UpdateScript(String projectName, IResourceDelta resourceDelta) {
            this.projectName = projectName;
            scriptName = resourceDelta.getResource().getName();
        }

        @Override
        public void run() {
            AweProjectService projectService = NeoServiceFactory.getInstance().getProjectService();
            RubyProjectNode rubyProject = projectService.findRubyProject(projectName);
            if (rubyProject != null) {
                RubyScriptNode script = projectService.findScript(rubyProject, scriptName);
                if (script != null) {
                    CellNode cell = projectService.findCellByScriptReference(script);
                    if (cell != null) {
                        NeoServicesUiPlugin.getDefault().getUpdateViewManager().updateCell(projectName,
                                projectService.getSpreadsheetByCell(cell).getName(),
                                SplashPlugin.getDefault().getSpreadsheetService().getFullId(cell));
                    }
                }
            }

        }
    }
}
