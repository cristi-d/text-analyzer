/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzer.gui.custom.models;

import javax.swing.AbstractListModel;
import textanalyzer.model.doc.Document;

/**
 *
 * @author Cristi
 */
public class PersonagesListModel extends AbstractListModel {
    private Document doc;
    
    public PersonagesListModel() {
        this.doc = null;
    }
    
    public void setDocument(Document doc) {
        this.doc = doc;
    }
    
    public void addPersonage(String name) {
        
    }
    
    @Override
    public int getSize() {
        if (doc == null || doc.getPersonages() == null) {
            return 0;
        } else {
            return doc.getPersonages().size();
        }
    }

    @Override
    public Object getElementAt(int index) {
        return doc.getPersonages().get(index).getName();
    }
    
}
