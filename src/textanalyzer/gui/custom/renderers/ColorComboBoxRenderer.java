/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzer.gui.custom.renderers;

import java.awt.Component;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import textanalyzer.gui.custom.models.ColorListModel;

/**
 *
 * @author Cristi
 */
public class ColorComboBoxRenderer  extends BasicComboBoxRenderer {
    private ColorListModel listModel;
    
    public ColorComboBoxRenderer(ColorListModel listModel) {
        this.listModel = listModel;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        if (index != -1) {
            c.setBackground(listModel.getColorAt(index));
        } else {
            if (value != null) {
                c.setBackground(listModel.getObjectColor(value));
            }
        }
        
        return c;
    }
    
    
}
