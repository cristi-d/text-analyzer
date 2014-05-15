/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzer.model.doc;

import edu.stanford.nlp.pipeline.Annotation;

/**
 *
 * @author Cristi
 */
public class Utterance {
    private Personage emmiter;
    private String parsedText;
    private String originalText;
    private UtteranceType type;
    private int beginOffset, endOffset;
    private Annotation parsedTextAnnotation;
    private Annotation originalTextAnnotation;
    private int utteranceIndex;
    
    public enum UtteranceType {
        NARRATIVE, DIALOGUE;
    }
    
    public Utterance(int utteranceIndex, int beginOffset, int endOffset, String parsedText, String originalText, UtteranceType type, Personage emmiter) {
        this.originalText = originalText;
        this.type = type;
        this.emmiter = emmiter;
        this.endOffset = endOffset;
        this.parsedText = parsedText;
        this.utteranceIndex = utteranceIndex;
        this.beginOffset = beginOffset;
    }
    
    public int getUtteranceIndex() {
        return utteranceIndex;
    }

    public Annotation getParsedTextAnnotation() {
        return parsedTextAnnotation;
    }

    public void setParsedTextAnnotation(Annotation parsedTextAnnotation) {
        this.parsedTextAnnotation = parsedTextAnnotation;
    }

    public Annotation getOriginalTextAnnotation() {
        return originalTextAnnotation;
    }

    public void setOriginalTextAnnotation(Annotation originalTextAnnotation) {
        this.originalTextAnnotation = originalTextAnnotation;
    }
    
    public UtteranceType getType() {
        return type;
    }

    public int getBeginOffset() {
        return beginOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }
    
    public String getParsedText() {
        return parsedText;
    }

    public String getOriginalText() {
        return originalText;
    }

    public void setEmmiter(Personage personage) {
        this.emmiter = personage;
    }
    
    public Personage getEmmiter() {
        return emmiter;
    }
    
    public String getEmmiterString() {
        if (emmiter == null) {
            return "";
        } else {
            return emmiter.getName();
        }
    }
}
