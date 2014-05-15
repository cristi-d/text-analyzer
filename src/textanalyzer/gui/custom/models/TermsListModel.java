/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzer.gui.custom.models;

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import textanalyzer.model.lang.Word;

/**
 *
 * @author Cristi
 */
public class TermsListModel extends AbstractListModel {
    private List<Word> terms;
    private List<Word> filteredTerms;
    
    
    public TermsListModel() {
        terms = new ArrayList<Word>();
        filteredTerms = terms;
    }
    
    public void clearAll() {
        terms.clear();
    }
    
    public void setTermList(List<Word> terms) {
        this.terms = terms;
        filteredTerms = terms;
    }
    
    public List<Word> getAllTerms() {
        return terms;
    }
    
    public void removeWord(int index) {
        if (filteredTerms != terms) {
            Word w = filteredTerms.remove(index);
            terms.remove(w);
        } else {
            terms.remove(index);
        }
    }
    
    public void addWord(Word w) {
        int index = 0;
        
        for (int i = 0; i < terms.size(); i++) {
            if (terms.get(i).getOccurances() <= w.getOccurances()) {
                index = i;
                break;
            }
        }
        terms.add(index, w);
        
        if (filteredTerms != terms) {
            for (int i = 0; i < filteredTerms.size(); i++) {
                if (filteredTerms.get(i).getOccurances() <= w.getOccurances()) {
                    index = i;
                    break;
                }
            }

            filteredTerms.add(index, w);
        }
        
    }
    
    public void setFilteredTerms(List<Word> filteredTerms) {
        this.filteredTerms = filteredTerms;
    }
    
    @Override
    public int getSize() {
        if (filteredTerms == null) {
            return 0;
        } else {
            return filteredTerms.size();
        }
    }
    
    public Word getWordAt(int index) {
        return filteredTerms.get(index);
    }

    @Override
    public Object getElementAt(int index) {
        if (filteredTerms == null) {
            return null;
        } else {
            Word w = filteredTerms.get(index);
            return w.getLemma() + " [" + w.getOccurances() + "]";
        }
    }
    
}
