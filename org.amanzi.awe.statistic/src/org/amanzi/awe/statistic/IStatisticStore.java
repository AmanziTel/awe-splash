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

package org.amanzi.awe.statistic;

import org.neo4j.graphdb.Node;

/**
 * <p>
 * IStatisticStore
 * </p>
 * 
 * @author TsAr
 * @since 1.0.0
 */
public interface IStatisticStore {


    /**
     * Store statistic element.
     * 
     * @param statElem the stat elem
     * @param node the node
     */
    void storeStatisticElement(IStatisticElement statElem, Node node);

}
