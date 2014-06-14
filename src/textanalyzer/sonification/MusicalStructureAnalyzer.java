/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package textanalyzer.sonification;

import java.util.ArrayList;
import java.util.List;
import textanalyzer.model.doc.Document;
import textanalyzer.model.doc.Utterance;
import textanalyzer.model.lang.WordOccurance;
import textanalyzer.model.music.MusicalStructure;
import textanalyzer.model.music.Phrase;
import textanalyzer.model.music.PhraseRelationship;

/**
 *
 * @author cristiand
 */
public final class MusicalStructureAnalyzer {
    private MusicalStructureAnalyzer() {
        ;
    }
    
    public static MusicalStructure computeMusicalStructure(Document doc) {
        try {
            
            int maxUtteranceLength = Integer.MIN_VALUE, minUtteranceLength = Integer.MAX_VALUE;
            int structureLengthUnit;
            
            for (Utterance utt : doc.getUtterances()) {
                if (utt.getOriginalText().length() > maxUtteranceLength) {
                    maxUtteranceLength = utt.getOriginalText().length();
                }
                
                if (utt.getOriginalText().length() < minUtteranceLength) {
                    minUtteranceLength = utt.getOriginalText().length();
                }
            }
            
            structureLengthUnit = (maxUtteranceLength - minUtteranceLength) / 4;
            
            //TODO use polyphonic relationships
            List<Phrase> musicalPhrases = new ArrayList<Phrase>();
            
            for (Utterance utterance : doc.getUtterances()) {
                List<WordOccurance> voiceOccurances = utterance.getVoiceOccurances();
                if (voiceOccurances.isEmpty() == false) {
                    Phrase phrase = new Phrase(utterance.getUtteranceIndex(), utterance.getEmmiter(), 
                            voiceOccurances.get(0).getVoice(), utterance.getOriginalText().length() / structureLengthUnit);
                    musicalPhrases.add(phrase);
                }
            }
            
            List<PhraseRelationship> musicalPhraseRelationships = new ArrayList<PhraseRelationship>();
            
            for (int i = 0; i < musicalPhrases.size() - 1; i++) {
                Phrase src, dst;
                src = musicalPhrases.get(i);
                dst = musicalPhrases.get(i + 1);
                
                if (dst.getIndex() == src.getIndex() + 1) {
                    //TODO: set polyphonic relationship here
                    musicalPhraseRelationships.add(new PhraseRelationship(src, dst, PhraseRelationship.Type.NONE));
                }
            }
            
            String[] voices = new String[doc.getVoices().size()];
            for (int i = 0; i < voices.length; i++) {
                voices[i] = doc.getVoice(i).getName();
            }
            
            String[] personages = new String[doc.getPersonages().size()];
            for (int i = 0; i < personages.length; i++) {
                personages[i] = doc.getPersonages().get(i).getName();
            }
            
            return new MusicalStructure(voices, personages, musicalPhraseRelationships, musicalPhrases);
        } catch (Exception ex) {
            ex.printStackTrace();
            
            return null;
        }
    }
}
