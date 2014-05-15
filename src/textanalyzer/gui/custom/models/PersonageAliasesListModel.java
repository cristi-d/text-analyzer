/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzer.gui.custom.models;

import javax.swing.AbstractListModel;
import textanalyzer.model.doc.Personage;

/**
 *
 * @author Cristi
 */
public class PersonageAliasesListModel extends AbstractListModel {
    private Personage personage;
    
    public PersonageAliasesListModel() {
        this.personage = null;
    }
    
    public void setPersonage(Personage p) {
        this.personage = p;
    }
    
    @Override
    public int getSize() {
        if (this.personage == null) {
            return 0;
        } else {
            return personage.getAliases().size();
        }
    }

    @Override
    public Object getElementAt(int index) {
        return personage.getAliases().get(index);
    }
    
}
