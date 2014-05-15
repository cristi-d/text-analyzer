/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzer.gui.custom.models;

import java.awt.Color;

/**
 *
 * @author Cristi
 */
public interface ColorListModel {
    public Color getColorAt(int index);
    public Color getObjectColor(Object obj);
}
