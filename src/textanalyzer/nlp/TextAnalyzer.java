/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzer.nlp;

import textanalyzer.model.lang.PolyphonicRelationship;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import textanalyzer.model.doc.Document;
import textanalyzer.model.doc.Personage;
import textanalyzer.model.doc.Utterance;
import textanalyzer.model.doc.Voice;
import textanalyzer.model.lang.Word;
import textanalyzer.model.lang.WordId;
import textanalyzer.model.lang.WordOccurance;

/**
 *
 * @author Cristi
 */
public final class TextAnalyzer {
    
    private TextAnalyzer() {}
    
    public static List<Utterance> detectUtterances(Document doc) {
        
        List<String> paragraphs = doc.getParagraphs();
        
        StanfordCoreNLP pipeline = LanguageUtils.getStanfordPipeline();
        List<Utterance> utterances = new ArrayList<Utterance>();
        Utterance utterance;
        
        int offset = 0;
        int i = 0;
        
        for (String paragraph : paragraphs) {
            i++;
            int beginOffset = offset;
            int endOffset = offset + paragraph.length();
            
            if (paragraph.startsWith("\"")) {
                //Then it is most likely a dialogue line
                
                StringBuilder strBld = new StringBuilder();
                int quoteStartIndex = 0;
                int quoteEndIndex = paragraph.indexOf("\"", quoteStartIndex + 1);
                
                
                //The portion of the text where we may find the narrator's intervention
                String cueText = paragraph.substring(quoteEndIndex + 1);
                int additionalQuoteStartIndex = cueText.indexOf("\"");
                
                
                //If the dialogue is resumed after the narrator's intervention
                if (additionalQuoteStartIndex != -1) {
                    cueText = cueText.substring(0, additionalQuoteStartIndex);
                }
                
                if (cueText.indexOf('.') != -1) {
                    cueText = cueText.substring(0, cueText.indexOf('.'));
                }
                
                //In strBld we store the actual dialogue text
                strBld.append(paragraph.substring(quoteStartIndex + 1, quoteEndIndex));
                while ((quoteStartIndex = paragraph.indexOf("\"", quoteEndIndex + 1)) != -1
                        && (quoteEndIndex = paragraph.indexOf("\"", quoteStartIndex + 1)) != -1) {
                    strBld.append(" ").append(paragraph.substring(quoteStartIndex + 1, quoteEndIndex));
                }
                
                //Detect if the sentence contains any utterance marking verbs ("X said/replied/answered...")
                int cueVerbIndex = -1;
                GrammaticalStructure tree = null;
                if (cueText.trim().isEmpty() == false) {
                    Annotation annot = new Annotation(cueText);
                
                    pipeline.annotate(annot);

                    CoreMap sentence = annot.get(CoreAnnotations.SentencesAnnotation.class).get(0);
                    tree = LanguageUtils.getGrammaticalStructure(sentence);

                    List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
                    for (int j = 0; j < tokens.size(); j++) {
                        CoreLabel label = tokens.get(j);
                        
                        //We find the first verb 
                        if (LanguageUtils.isUtteranceCueWord(label.lemma())) {
                            cueVerbIndex = j + 1;
                            break;
                        }
                    }
                }
                
                
                //Detect utterance emmiter: Find the nominal subject of the cue verb
                String emmiter = null;
                if (cueVerbIndex != -1) {
                    for (TypedDependency dep : tree.typedDependencies()) {
                        int govIndex = dep.gov().label().index();
                        if (govIndex == cueVerbIndex && dep.reln().getShortName().equals("nsubj")) {
                            emmiter = dep.dep().label().word();
                        }
                    }
                } 
                               
                Personage personage = null;
                
                
                if (emmiter != null) {
                    personage = doc.getPersonageByAlias(emmiter);
                } 
                utterance = new Utterance(i, beginOffset, endOffset,
                        strBld.toString(), paragraph, Utterance.UtteranceType.DIALOGUE, personage);
            } else {
                //Narrative
                utterance = new Utterance(i, beginOffset, endOffset, paragraph, paragraph, Utterance.UtteranceType.NARRATIVE, doc.getNarrator());
            }
            
            utterances.add(utterance);
            offset += paragraph.length() + 1; //plus the '\n'
        }
        
        doc.setUtterances(utterances);
        
        return utterances;
    }
    
    public static List<Word> computeWordFrequencies(Document doc) {
        List<Utterance> utterances = doc.getUtterances();
        Map<WordId, Word> wordFrequencies = new HashMap<WordId, Word>();
        
        StanfordCoreNLP pipeline = LanguageUtils.getStanfordPipeline();
        
        for (Utterance utterance : utterances) {
            if (utterance.getEmmiter() != null) {
                Annotation annot = new Annotation(utterance.getParsedText());
                pipeline.annotate(annot);
                utterance.setParsedTextAnnotation(annot);
                
                for (CoreMap sentence : annot.get(CoreAnnotations.SentencesAnnotation.class)) {
                    for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                        Word.POS pos = Word.POS.fromStanfordTag(token.tag());
                        String lemma = token.lemma();
                        
                        if (LanguageUtils.isStopWord(lemma)) {
                            continue;
                        }
                        
                        if (pos != null) {
                            WordId wId = new WordId(lemma, pos);
                            Word w = wordFrequencies.get(wId);
                            if (w == null) {
                                w = new Word(lemma, pos);
                                wordFrequencies.put(wId, w);
                            } else {
                                w.incrementOccurances();
                            }
                        }
                    }
                }
            }
        }
        
        List<Word> words = new ArrayList<Word>();
        words.addAll(wordFrequencies.values());
        Collections.sort(words, new Comparator<Word>() {
            @Override
            public int compare(Word o1, Word o2) {
                if (o1.getOccurances() < o2.getOccurances()) {
                    return 1;
                } else {
                    if (o1.getOccurances() == o2.getOccurances()) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            }
        });
        
        doc.setWordsList(words);
        
        return words;
    }
    
    
    public static void detectVoiceOccurances(Document doc) {
        List<WordOccurance> occurances = new ArrayList<WordOccurance>();
        StanfordCoreNLP pipeline = LanguageUtils.getStanfordPipeline();
        
        for (Voice voice : doc.getVoices()) {
            voice.getOccurances().clear();
        }
        
        for (Utterance utterance : doc.getUtterances()) {
            if (utterance.getEmmiter() != null) {
                Annotation annot = new Annotation(utterance.getOriginalText());
                pipeline.annotate(annot);
                utterance.setOriginalTextAnnotation(annot);
                
                for (int sentenceIndex = 0; sentenceIndex < annot.get(CoreAnnotations.SentencesAnnotation.class).size(); sentenceIndex++) {
                    CoreMap sentence = annot.get(CoreAnnotations.SentencesAnnotation.class).get(sentenceIndex);
                    for (int tokenIndex = 0; tokenIndex < sentence.get(CoreAnnotations.TokensAnnotation.class).size(); tokenIndex++) {
                        CoreLabel token = sentence.get(CoreAnnotations.TokensAnnotation.class).get(tokenIndex);
                        Word.POS tokenPOS = Word.POS.fromStanfordTag(token.tag());
                        if (tokenPOS != null) {
                            for (Voice voice : doc.getVoices()) {
                                for (Word term : voice.getAssociatedTerms()) {
                                    if (token.lemma().equals(term.getLemma()) && 
                                    term.getPos().equals(tokenPOS)) {
                                    WordOccurance occurance = new WordOccurance(term, voice, 
                                            utterance.getBeginOffset() + token.beginPosition(), 
                                            utterance.getBeginOffset() + token.endPosition(), 
                                            utterance, sentenceIndex, tokenIndex);

                                    voice.addOccurance(occurance);
                                    occurances.add(occurance);
                                    break;
                                  }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        Collections.sort(occurances, new Comparator<WordOccurance>() {

            @Override
            public int compare(WordOccurance o1, WordOccurance o2) {
                if (o1.getSentenceIndex() < o2.getSentenceIndex()) {
                    return -1;
                }
                
                if (o1.getSentenceIndex() == o2.getSentenceIndex()) {
                    return o1.getTokenIndex() < o2.getTokenIndex() ? -1 : (o1.getTokenIndex() == o2.getTokenIndex() ? 0 : 1);
                } else {
                    return 1;
                }
            }
        });
        
        doc.setVoiceOccurances(occurances);
    }
    
    public static void detectVoiceInteranimation(Document doc) throws Exception {
        List<WordOccurance> occurances = doc.getVoiceOccurances();
        PrintWriter writer = new PrintWriter(new File("polyphonic_relationships.txt"));
        int lastUtteranceIndex = 0;
        int currentUtteranceIndex = 0;
        
        for (int i = 0; i < occurances.size() - 1; i++) {
            WordOccurance wOcc1 = occurances.get(i);
            WordOccurance wOcc2 = occurances.get(i + 1);
            
            
            if (wOcc1.getUtteranceIndex() == wOcc2.getUtteranceIndex() && wOcc1.getSentenceIndex() == wOcc2.getSentenceIndex()) {
                currentUtteranceIndex = wOcc1.getUtteranceIndex();
                if (currentUtteranceIndex != lastUtteranceIndex) {
                    writer.println("[Utterance #" + currentUtteranceIndex + "]: " + wOcc1.getUtterance().getOriginalText());
                    lastUtteranceIndex = currentUtteranceIndex;
                }
                
                int wOcc1Index = wOcc1.getTokenIndex();
                int wOcc2Index = wOcc2.getTokenIndex();
                
                Annotation annot = new Annotation(wOcc1.getUtterance().getOriginalText());
                LanguageUtils.getStanfordPipeline().annotate(annot);
                CoreMap sentence = annot.get(CoreAnnotations.SentencesAnnotation.class).get(wOcc1.getSentenceIndex());               
                
                for (int j = wOcc1Index + 1; j < wOcc2Index; j++) {
                    CoreLabel token = sentence.get(CoreAnnotations.TokensAnnotation.class).get(j);
                    String stem = LanguageUtils.getStem(token.originalText());
                    
                    
                    writer.println("[Co-occurence] in [Utterance #" + wOcc1.getUtteranceIndex() + "]: [Voice \"" + wOcc1.getVoice().getName()+ "\"] " + wOcc1.getWord().getLemma() + " (" + wOcc1.getTokenIndex() + ") [...] [Token] " + token + " (" + j + ") [...] [Voice \"" + wOcc2.getVoice().getName() + "\"] " + wOcc2.getWord().getLemma() + " (" + wOcc2.getTokenIndex() +")");
                    PolyphonicRelationship rel = LanguageUtils.isDiscourseMarker(stem);
                    if (rel != null) {
                        
                        switch (rel) {
                            case UNITY:
                                writer.println("[Detected Polyphonic Relationship]: " + wOcc1.getVoice().getName() + " UNITY " + wOcc2.getVoice().getName());
                                break;
                            case DIFFERENCE:
                                writer.println("[Detected Polyphonic Relationship]: " + wOcc1.getVoice().getName() + " DIFFERENCE " + wOcc2.getVoice().getName());
                                break;
                            default:
                                throw new AssertionError(rel.name());
                        }
                    }
                }
            }   
        }
        writer.close();
    }
}
