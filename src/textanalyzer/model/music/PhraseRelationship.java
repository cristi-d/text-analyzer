/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package textanalyzer.model.music;

/**
 *
 * @author cristiand
 */
public class PhraseRelationship {
    public static enum Type {
        UNITY, DIFFERENCE, NONE;
    }
    
    private final Phrase source, destination;
    private final Type type;

    public PhraseRelationship(Phrase source, Phrase destination, Type type) {
        this.source = source;
        this.destination = destination;
        this.type = type;
    }

    public Phrase getSource() {
        return source;
    }

    public Phrase getDestination() {
        return destination;
    }

    public Type getType() {
        return type;
    }
    
    
}
