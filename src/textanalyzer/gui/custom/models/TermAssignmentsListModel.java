/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzer.gui.custom.models;

import javax.swing.AbstractListModel;
import textanalyzer.model.doc.Voice;
import textanalyzer.model.lang.Word;

/**
 *
 * @author Cristi
 */
public class TermAssignmentsListModel extends AbstractListModel {
    private Voice selectedVoice;
    
    public TermAssignmentsListModel() {
        
    }
    
    public void setSelectedVoice(Voice voice) {
        selectedVoice = voice;
    }

    @Override
    public int getSize() {
        if (selectedVoice != null) {
            return selectedVoice.getAssociatedTerms().size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getElementAt(int index) {
        Word w = selectedVoice.getAssociatedTerms().get(index);
        return w.getLemma();
    }
    
}
