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
package org.amanzi.splash.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

import org.amanzi.splash.utilities.NeoSplashUtil;

public class ColumnHeaderRenderer extends DefaultTableCellRenderer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8315459904464796962L;

	public Color bgColor = Color.gray;
	
	public ColumnHeaderRenderer(int width, int height) {
		setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		setHorizontalAlignment(CENTER);
		setBackground(Color.gray);
		
		setPreferredSize(new Dimension(width,height));
	}
	
	
	public boolean selected = false;

	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus,
			int row, int column)
	{
		//Util.logn("getTableCellRendererComponent is called ");
		
		setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		setHorizontalAlignment(CENTER);
		
		
		if (selected == true){
			setFont(NeoSplashUtil.selectedHeaderFont);
			setBackground(NeoSplashUtil.selectedHeaderColor);
			//selected = false;
		}
		else{
			setFont(NeoSplashUtil.unselectedHeaderFont);
			setBackground(NeoSplashUtil.unselectedHeaderColor);
		}
			
		setValue(value.toString());
		//selected = false;
		return this;
	}

	public Color getBgColor() {
		return bgColor;
	}

	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}
}
