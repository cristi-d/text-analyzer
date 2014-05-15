/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzer.gui.custom.models;

import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Cristi
 */
public class VoicesComboBoxModel extends DefaultComboBoxModel {
    private VoiceListModel listModel;
    
    public VoicesComboBoxModel(VoiceListModel listModel) {
        this.listModel = listModel;
    }

    @Override
    public int getSize() {
        return listModel.getSize();
    }
    
    
    
    @Override
    public Object getElementAt(int index) {
        return listModel.getElementAt(index);
    }
    
    
}
