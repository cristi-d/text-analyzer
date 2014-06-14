/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package textanalyzer.model.music;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author cristiand
 */
public class MusicalStructure {
    private List<PhraseRelationship> phraseRelationships;
    private Map<Phrase, PhraseRelationship> phraseRelationshipMap;
    private List<Phrase> phrases;
    private String[] voices;
    private String[] personages;
    
    public MusicalStructure(String[] voices, String[] personages, List<PhraseRelationship> phraseRelationships, List<Phrase> phrases) {
        this.phraseRelationships = phraseRelationships;
        this.phrases = phrases;
        this.voices = voices;
        this.personages = personages;
        
        phraseRelationshipMap = new HashMap<Phrase, PhraseRelationship>();
        
        for (PhraseRelationship rel : phraseRelationships) {
            phraseRelationshipMap.put(rel.getSource(), rel);
        }
    }

    public List<PhraseRelationship> getPhraseRelationships() {
        return phraseRelationships;
    }

    public List<Phrase> getPhrases() {
        return phrases;
    }
    
    public PhraseRelationship getRelationship(Phrase phrase) {
        return phraseRelationshipMap.get(phrase);
    }

    public String[] getVoices() {
        return voices;
    }

    public String[] getPersonages() {
        return personages;
    }
}
