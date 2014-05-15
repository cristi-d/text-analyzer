/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzer.model.lang;

import java.awt.Color;
import java.io.Serializable;
import textanalyzer.model.doc.Utterance;
import textanalyzer.model.doc.Voice;

/**
 *
 * @author Cristi
 */
public class WordOccurance implements Comparable<WordOccurance>, Serializable {
    private Word word;
    private int globalBeginOffset, globalEndOffset;
    private int tokenBeginOffset, tokenEndOffset;
    private int sentenceIndex, tokenIndex;
    private Color highlight;
    private Voice voice;
    private Utterance utterance;
    
    public WordOccurance(Word word, Voice voice, int globalBeginOffset, int globalEndOffset, Utterance utterance, int sentenceIndex, int tokenIndex) {
        this(word, voice, globalBeginOffset, globalEndOffset, utterance);
        this.sentenceIndex = sentenceIndex;
        this.tokenIndex = tokenIndex;
    }
    
    public WordOccurance(Word word, Voice voice, int globalBeginOffset, int globalEndOffset, Utterance utterance) {
        this.word = word;
        this.voice = voice;
        this.globalBeginOffset = globalBeginOffset;
        this.globalEndOffset = globalEndOffset;
        this.tokenBeginOffset = globalBeginOffset - utterance.getBeginOffset();
        this.tokenEndOffset = globalEndOffset - utterance.getEndOffset();
        
        this.highlight = voice.getHighlightColor();
        this.utterance = utterance;
    }

    public int getSentenceIndex() {
        return sentenceIndex;
    }

    public int getTokenIndex() {
        return tokenIndex;
    }

    public Utterance getUtterance() {
        return utterance;
    }
    
    public int getUtteranceIndex() {
        return utterance.getUtteranceIndex();
    }
    
    public Color getHighlight() {
        return highlight;
    }
    
    public Word getWord() {
        return word;
    }
    
    public void setVoice(Voice voice) {
        this.voice = voice;
    }
    
    public Voice getVoice() {
        return voice;
    }

    public int getBeginOffset() {
        return globalBeginOffset;
    }

    public int getEndOffset() {
        return globalEndOffset;
    }

    public int getTokenBeginOffset() {
        return tokenBeginOffset;
    }

    public void setTokenBeginOffset(int tokenBeginOffset) {
        this.tokenBeginOffset = tokenBeginOffset;
    }

    public int getTokenEndOffset() {
        return tokenEndOffset;
    }

    public void setTokenEndOffset(int tokenEndOffset) {
        this.tokenEndOffset = tokenEndOffset;
    }
    
    
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof WordOccurance == false) {
            return false;
        }
        
        WordOccurance other = (WordOccurance) o;
        
        return other.getWord().equals(word);
    }

    @Override
    public int compareTo(WordOccurance o) {
        if (this.globalEndOffset > o.getEndOffset()) {
            return 1;
        }
        
        if (this.globalEndOffset < o.getEndOffset()) {
            return -1;
        }
        
        return 0;
    }

}
