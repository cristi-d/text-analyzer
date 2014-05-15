/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzer.gui.custom.models;

import javax.swing.DefaultComboBoxModel;
import textanalyzer.model.doc.Document;

/**
 *
 * @author Cristi
 */
public class PersonagesComboBoxModel extends DefaultComboBoxModel {
    private Document doc;
    
    public PersonagesComboBoxModel() {
        super();
    }
    
    public void setDocument(Document doc) {
        this.doc = doc;
    }
    
    @Override
    public int getSize() {
        if (doc == null) {
            return 0;
        } else {
            if (doc.getPersonages() == null) {
                return 0;
            } else {
                return doc.getPersonages().size();
            }
        }
        
    }

    @Override
    public Object getElementAt(int index) {
        return doc.getPersonages().get(index).getName();
    }
    
    
}
