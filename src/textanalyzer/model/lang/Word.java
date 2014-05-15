/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzer.model.lang;

import java.io.Serializable;

/**
 *
 * @author Cristi
 */
public class Word implements Comparable<Word>, Serializable {
    private String lemma;
    private POS pos;
    private int occurances;

    @Override
    public int compareTo(Word o) {
        if (this.occurances > o.getOccurances()) {
            return 1;
        } else {
            if (this.occurances == o.getOccurances()) {
                return 0;
            } else {
                return -1;
            }
        }
    }
    
    public enum POS {
        ADJECTIVE, ADVERB, NOUN, VERB;
        
        public static POS fromStanfordTag(String tag) {
            if (tag.startsWith("NN")) {
                return NOUN;
            }
            if (tag.startsWith("JJ")) {
                return ADJECTIVE;
            }
            if (tag.startsWith("VB")) {
                return VERB;
            }
            if (tag.startsWith("RB")) {
                return ADVERB;
            }
            
            return null;
        }
        
    }
    
    public Word(String lemma, POS pos, int occurances) {
        this.lemma = lemma;
        this.pos = pos;
        this.occurances = occurances;
    }
    
    public Word(String lemma, POS pos) {
        this.lemma = lemma;
        this.pos = pos;
        this.occurances = 1;
    }

    public String getLemma() {
        return lemma;
    }

    public POS getPos() {
        return pos;
    }
    
    public void incrementOccurances() {
        this.occurances++;
    }
    
    public int getOccurances() {
        return occurances;
    }
    
    @Override
    public String toString() {
        return lemma + "/" + pos + "/" + occurances;
    }
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof Word == false) {
            return false;
        }
        
        Word w = (Word) other;
        return w.getLemma().equals(lemma) && w.getPos().equals(pos);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (this.lemma != null ? this.lemma.hashCode() : 0);
        hash = 61 * hash + (this.pos != null ? this.pos.hashCode() : 0);
        return hash;
    }
}
