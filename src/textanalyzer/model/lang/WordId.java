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
public class WordId implements Serializable {
    private String lemma;
    private Word.POS pos;

    public WordId(String lemma, Word.POS pos) {
        this.lemma = lemma;
        this.pos = pos;
    }

    public String getLemma() {
        return lemma;
    }

    public Word.POS getPos() {
        return pos;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.lemma != null ? this.lemma.hashCode() : 0);
        hash = 67 * hash + (this.pos != null ? this.pos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WordId other = (WordId) obj;
        if ((this.lemma == null) ? (other.lemma != null) : !this.lemma.equals(other.lemma)) {
            return false;
        }
        if ((this.pos == null) ? (other.pos != null) : !this.pos.equals(other.pos)) {
            return false;
        }
        return true;
    }
    
    
}
