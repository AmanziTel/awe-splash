package org.amanzi.awe.neostyle;

import java.awt.Color;

/**
 * <p>
 * Contains information about network and tems style
 * </p>
 * 
 * @author Cinkel_A
 * @since 1.1.0
 */
public class NeoStyle {
    private Color line;
    private Color fill;
    private Color label;
    private Integer smallestSymb;
    private Integer smallSymb;
    private Integer labeling;
    private boolean fixSymbolSize;
    private Integer symbolSize;
    private Integer sectorTransparency;

    public NeoStyle(Color line, Color fill, Color label) {
        super();
        this.line = line;
        this.fill = fill;
        this.label = label;
    }

    /**
     * gets line color
     * 
     * @return
     */
    public Color getLine() {
        return line;
    }

    /**
     * sets color of line
     * 
     * @param line
     */
    public void setLine(Color line) {
        this.line = line;
    }

    /**
     * gets fill color
     * 
     * @return
     */
    public Color getFill() {
        return fill;
    }

    /**
     * sets fill color
     * 
     * @param fill
     */
    public void setFill(Color fill) {
        this.fill = fill;
    }

    /**
     * get label color
     * 
     * @return
     */
    public Color getLabel() {
        return label;
    }

    /**
     * sets label color
     * 
     * @param label
     */
    public void setLabel(Color label) {
        this.label = label;
    }

    /**
     * @return Returns the smallestSymb.
     */
    public Integer getSmallestSymb() {
        return smallestSymb;
    }

    /**
     * @param smallestSymb The smallestSymb to set.
     */
    public void setSmallestSymb(Integer smallestSymb) {
        this.smallestSymb = smallestSymb;
    }

    /**
     * @return Returns the smallSymb.
     */
    public Integer getSmallSymb() {
        return smallSymb;
    }

    /**
     * @param smallSymb The smallSymb to set.
     */
    public void setSmallSymb(Integer smallSymb) {
        this.smallSymb = smallSymb;
    }

    /**
     * @return Returns the labeling.
     */
    public Integer getLabeling() {
        return labeling;
    }

    /**
     * @param labeling The labeling to set.
     */
    public void setLabeling(Integer labeling) {
        this.labeling = labeling;
    }

    /**
     * @param selection
     */
    public void setFixSymbolSize(boolean fixdSymbolSize) {
        this.fixSymbolSize = fixdSymbolSize;
    }

    /**
     * @return Returns the fixdSymbolSize.
     */
    public boolean isFixSymbolSize() {
        return fixSymbolSize;
    }

    /**
     * @return Returns the symbolSize.
     */
    public Integer getSymbolSize() {
        return symbolSize;
    }

    /**
     * @param symbolSize The symbolSize to set.
     */
    public void setSymbolSize(Integer symbolSize) {
        this.symbolSize = symbolSize;
    }

    /**
     * @return Returns the sectorTransparency.
     */
    public Integer getSectorTransparency() {
        return sectorTransparency;
    }

    /**
     * @param sectorTransparency The sectorTransparency to set.
     */
    public void setSectorTransparency(Integer sectorTransparency) {
        this.sectorTransparency = sectorTransparency;
    }

}
