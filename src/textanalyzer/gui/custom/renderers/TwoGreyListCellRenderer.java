/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzer.gui.custom.renderers;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author Cristi
 */
public class TwoGreyListCellRenderer extends DefaultListCellRenderer {
    private Color grey1, grey2;
    
    public TwoGreyListCellRenderer() {
        grey1 = new Color(220, 220, 220);
        grey2 = new Color(230, 230, 230);
    }
    
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        if (index % 2 == 0) {
            c.setBackground(grey1);
        } else {
            c.setBackground(grey2);
        }
        
        return c;
    }
    
    
}
