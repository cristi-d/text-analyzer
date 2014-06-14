/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package textanalyzer.model.music;

import textanalyzer.model.doc.Personage;
import textanalyzer.model.doc.Voice;
import textanalyzer.sonification.music.MusicalPhrase;

/**
 *
 * @author cristiand
 */
public class Phrase {
    private int index;
    private String emmiter;
    private int length;
    private String voice;
    private MusicalPhrase sonification;
    
    public Phrase(int index, Personage personage, Voice voice, int length) {
        this.emmiter = personage.getName();
        this.length = length;
        this.voice = voice.getName();
        this.index = index;
    }
    
    public String getEmmiter() {
        return emmiter;
    }
    
    public String getVoice() {
        return voice;
    }
    
    public int getLength() {
        return length;
    }
    
    public int getIndex() {
        return index;
    }

    public MusicalPhrase getSonification() {
        return sonification;
    }

    public void setSonification(MusicalPhrase sonification) {
        this.sonification = sonification;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Phrase) {
            Phrase p = (Phrase) obj;
            
            return p.getIndex() == this.index;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return index;
    }
    
    @Override
    public String toString() {
        return "[" + index + "] " + "#" + voice + ", @" + emmiter + ": " + length; 
    }
    
    
}
