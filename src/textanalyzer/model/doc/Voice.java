/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzer.model.doc;

import textanalyzer.model.lang.WordOccurance;
import textanalyzer.model.lang.Word;
import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Cristi
 */
public class Voice implements Serializable {
    private List<Word> associatedTerms;
    private List<WordOccurance> occurances;
    private Color highlightColor;
    private String name;
            
    public Voice(String name, Color color) {
        associatedTerms = new ArrayList<Word>();
        occurances = new ArrayList<WordOccurance>();
        this.name = name;
        this.highlightColor = color;
    }
    
    public List<WordOccurance> getOccurances() {
        return occurances;
    }
    
    public void addOccurance(WordOccurance occ) {
        occurances.add(occ);
    }
    
    public Voice(List<Word> associatedTerms) {
        this.associatedTerms = associatedTerms;
    }
    
    public void setHighlightColor(Color highlightColor) {
        this.highlightColor = highlightColor;
    }
    
    public Color getHighlightColor() {
        return highlightColor;
    }
    
    public void addAssociatedTerm(Word w) {
        associatedTerms.add(w);
    }
    
    public Word removeAssociatedTerm(int index) {
        Word w = associatedTerms.get(index);
        
        List<WordOccurance> temp = new ArrayList<WordOccurance>();
        for (WordOccurance occ : occurances) {
            if (occ.getWord().equals(w)) {
                occ.setVoice(null);
                temp.add(occ);
            }
        }
        
        for (WordOccurance toRem : temp) {
            
            occurances.remove(toRem);
        }
        
        return associatedTerms.remove(index);
    }
    
    public void removeAssociatedTerm(Word w) {
        List<WordOccurance> temp = new ArrayList<WordOccurance>();
        for (WordOccurance occ : occurances) {
            if (occ.getWord().equals(w)) {
                occ.setVoice(null);
                temp.add(occ);
            }
        }
        
        for (WordOccurance toRem : temp) {
            
            occurances.remove(toRem);
        }
        
        associatedTerms.remove(w);
    }
    
    public List<Word> getAssociatedTerms() {
        return associatedTerms;
    }
    
    public boolean hasAssociatedTerm(Word w) {
        return associatedTerms.contains(w);
    }
    
    public String getName() {
        return name;
    }
    
}
