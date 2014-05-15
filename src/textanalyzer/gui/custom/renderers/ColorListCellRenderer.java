/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzer.gui.custom.renderers;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import textanalyzer.gui.custom.models.ColorListModel;

/**
 *
 * @author Cristi
 */
public class ColorListCellRenderer extends DefaultListCellRenderer {
    private ColorListModel listModel;
    
    public ColorListCellRenderer(ColorListModel listModel) {
        this.listModel = listModel;
    }
    
    
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {  
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);  
        
        if (index != -1) {
            c.setBackground(listModel.getColorAt(index));
        }         
        
        return c;  
    }    
}
