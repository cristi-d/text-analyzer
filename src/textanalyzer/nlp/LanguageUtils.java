/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzer.nlp;

import textanalyzer.model.lang.PolyphonicRelationship;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.util.CoreMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import textanalyzer.model.lang.Word;
import textanalyzer.model.lang.WordOccurance;

/**
 *
 * @author Cristi
 */
public final class LanguageUtils {
    private static final List<String> utteranceCueWords = new ArrayList<String>();
    private static final Set<String> stopWords = new HashSet<String>();
    private static final String UTTERANCE_CUEWORDS_FILE = "utterance-cues.txt";
    private static final String STOPWORDS_FILE = "stopWords.txt";
    private static final String WORDNET_DATABASE = "D:/Cristi/WordNet/dict/";
    private static final String DISCOURSE_MARKERS_FILE = "discourse-markers.xml";
    private static WordNetDatabase wordNet;
    private static LexicalizedParser depParser;
    private static GrammaticalStructureFactory gsf;
    private static StanfordCoreNLP pipeline;
    private static Morphology morphology;
    private static final String MARKER_XPATH = "//marker";
    private static final String MARKER_STEM = "stem";
    private static final String MARKER_TYPE = "type";
    private static Map<String, PolyphonicRelationship> discourseMarkers;
    
    private LanguageUtils() {}
    
    static {
        
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse");//, ner, dcoref");
//        props.setProperty("ner.model", "edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz");
        pipeline = new StanfordCoreNLP(props);
        
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(STOPWORDS_FILE)));
            String line;
            
            while ((line = reader.readLine()) != null) {
                stopWords.add(line.trim().toLowerCase());
            }
        } catch (IOException ex) {
            Logger.getLogger(LanguageUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(LanguageUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }   
        
        try {
            reader = new BufferedReader(new FileReader(UTTERANCE_CUEWORDS_FILE));
            String line;
            
            while ((line = reader.readLine()) != null) {
                utteranceCueWords.add(line.trim());
            }
        } catch (IOException ex) {
            Logger.getLogger(LanguageUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                //dummy
            }
        }

        System.setProperty("wordnet.database.dir", WORDNET_DATABASE);
        wordNet = WordNetDatabase.getFileInstance();
        
        depParser = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
        TreebankLanguagePack tlp = new PennTreebankLanguagePack();
        gsf = tlp.grammaticalStructureFactory();
        
        
        //LOAD discourse markers
        loadDiscourseMarkers();
        
        morphology = new Morphology();
    }
    
    public static PolyphonicRelationship isDiscourseMarker(String stem) {
        return discourseMarkers.get(stem);
    }
    
    public static String getStem(String word) {
        return morphology.stem(word);
    }
    
    private static void loadDiscourseMarkers() {
        discourseMarkers = new HashMap<String, PolyphonicRelationship>();
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder(); 
            org.w3c.dom.Document doc = db.parse(new File(DISCOURSE_MARKERS_FILE));

            XPath xPath = XPathFactory.newInstance().newXPath();

            NodeList nodes = (NodeList) xPath.compile(MARKER_XPATH).evaluate(doc, XPathConstants.NODESET);

            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                String stem = null;
                PolyphonicRelationship rel = null;
                
                
                for (int j = 0; j < node.getChildNodes().getLength(); j++) {
                    Node child = node.getChildNodes().item(j);
                    
                    if (child.getNodeName().equals(MARKER_STEM)) {
                        stem = child.getTextContent().trim();
                    }
                    
                    if (child.getNodeName().equals(MARKER_TYPE)) {
                        rel = PolyphonicRelationship.valueOf(child.getTextContent().trim());
                    }
                    
                    if (stem != null && rel != null) {
                        break;
                    }
                }
                
                if (stem != null && rel != null) {
                    discourseMarkers.put(stem, rel);
                }
                
            }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(LanguageUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(LanguageUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LanguageUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(LanguageUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static StanfordCoreNLP getStanfordPipeline() {
        return pipeline;
    }
    
    public static boolean isUtteranceCueWord(String lemma) {
        return utteranceCueWords.contains(lemma);
    }
    
    public static List<String> getUtteranceCueWords() {
        return utteranceCueWords;
    }
    
    
    public static PolyphonicRelationship getPolyphonicRelationshipBySemanticGraph(WordOccurance wOcc1, WordOccurance wOcc2) {
        //TODO: token index is not the same as word index :( Find a way to map token index to word index
        
        
        System.out.println("Comparing words " + wOcc1.getWord() + " & " + wOcc2.getWord() + " from sentences " + wOcc1.getSentenceIndex() + " & " + wOcc2.getSentenceIndex() );
        if (wOcc1.getSentenceIndex() != wOcc2.getSentenceIndex()) {
            if (wOcc1.getVoice().getName().equals(wOcc2.getVoice().getName())) {
                return PolyphonicRelationship.UNITY;
            } else {
                return PolyphonicRelationship.INDEPENDENT;
            }
        } else {
            CoreMap sentence = wOcc1.getUtterance().getOriginalTextAnnotation().get(CoreAnnotations.SentencesAnnotation.class).get(wOcc1.getSentenceIndex());
            System.out.println("Sentence: " + sentence);
            System.out.println(wOcc1.getWord().toString() + "(" + wOcc1.getTokenIndex()+ ") & " + wOcc2.getWord().toString() + "(" + wOcc2.getTokenIndex()+ ")");
            SemanticGraph graph = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
            
            int wOcc1EndOffset = sentence.get(CoreAnnotations.TokensAnnotation.class).get(wOcc1.getTokenIndex()).endPosition();
            int wOcc2EndOffset = sentence.get(CoreAnnotations.TokensAnnotation.class).get(wOcc2.getTokenIndex()).endPosition();
            
            int index1 = sentence.toString().substring(0, wOcc1EndOffset).split("[ ,.!?\"']").length;
            int index2 = sentence.toString().substring(0, wOcc2EndOffset).split("[ ,.!?\"']").length;
            
            System.out.println("Sentence (reconstructed): " + graph.toRecoveredSentenceStringWithIndexMarking());
            
            IndexedWord word1 = null;
            IndexedWord word2 = null; 
            
            for (IndexedWord w : graph.getAllNodesByWordPattern(wOcc1.getWord().getLemma())) {
                if (w.endPosition() == wOcc1EndOffset) {
                    word1 = w;
                    break;
                }
            }
            
            for (IndexedWord w : graph.getAllNodesByWordPattern(wOcc2.getWord().getLemma())) {
                if (w.endPosition() == wOcc2EndOffset) {
                    word2 = w;
                    break;
                }
            }
            
            
            
            List<IndexedWord> word1ToRoot = graph.getPathToRoot(word1);
            List<IndexedWord> word2ToRoot = graph.getPathToRoot(word2);
            
            word1ToRoot.add(0, word1);
            word2ToRoot.add(0, word2);
            
            List<IndexedWord> word1ToCommon = new ArrayList<IndexedWord>();
            List<IndexedWord> word2ToCommon = new ArrayList<IndexedWord>();
            
            List<IndexedWord> smallerList = word1ToRoot, largerList = word2ToRoot, 
                    smallerListToCommon = word1ToCommon, largerListToCommon = word2ToCommon;
            
            if (word1ToRoot.size() > word2ToRoot.size()) {
                smallerList = word2ToRoot;
                smallerListToCommon = word2ToCommon;
                
                largerList = word1ToRoot;
                largerListToCommon = word1ToCommon;
            } 
            
            for (int i = 0; i < smallerList.size(); i++) {
                    IndexedWord w1 = smallerList.get(i);
                    smallerListToCommon.add(w1);
                    int w2Index = -1;
                    if ((w2Index = largerList.indexOf(w1)) != -1) {
                        largerListToCommon.addAll(largerList.subList(0, w2Index + 1));
                        break;
                    }
                }
            
            
            System.out.println("\n" + graph.toList() + "\n");
            
            System.out.println("Word 1 to common: ");
            IndexedWord previous = word1;
            for (int i = 1; i < word1ToCommon.size(); i++) {
                IndexedWord word = word1ToCommon.get(i);
                SemanticGraphEdge edge = graph.getEdge(word, previous);
                System.out.println(word + " --[" + edge.getRelation().toString() + "]--> " + previous);
            }
            
            System.out.println("Word 2 to common: ");
            previous = word2;
            for (int i = 1; i < word2ToCommon.size(); i++) {
                IndexedWord word = word2ToCommon.get(i);
                SemanticGraphEdge edge = graph.getEdge(word, previous);
                System.out.println(word + " --[" + edge.getRelation().toString() + "]--> " + previous);
            }
            
        }
        return null;
    }
    
    public static SemanticGraph getSentenceSemanticGraph(CoreMap sentence) {
        SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
        
        return dependencies;
    }
    public static Tree getSentenceTree(CoreMap sentence) {
        Tree tree = depParser.apply(sentence.get(CoreAnnotations.TokensAnnotation.class));

        return tree;
    }
    
    public static GrammaticalStructure getGrammaticalStructure(CoreMap sentence) {
        Tree tree = depParser.apply(sentence.get(CoreAnnotations.TokensAnnotation.class));
        return gsf.newGrammaticalStructure(tree);
    }
    
    public static List<Word> getRelatedWords(String query, List<Word> allTerms) {
        List<Word> related;
        if (query != null && query.isEmpty() == false) {
            related = new ArrayList<Word>();
        
            Synset[] synsets = wordNet.getSynsets(query);
            
            for (Synset synset : synsets) {
                for (String form : synset.getWordForms()) {
                    for (Word w : allTerms) {
                        if (w.getLemma().equals(form)) {
                            if (related.contains(w) == false) {
                                related.add(w);
                                break;
                            }
                        }
                    }
                }
            }
        } else {
            related = allTerms;
        }
        
        Collections.sort(related, new Comparator<Word>() {
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
        
        return related;
    }
   
    public static Synset[] getSynsets(String lemma, Word.POS pos) {
        SynsetType synType = null;
        
        if (pos == null) {
            return new Synset[0];
        }
        
        switch (pos) {
            case ADJECTIVE:
                synType = SynsetType.ADJECTIVE;
                break;
            case ADVERB:
                synType = SynsetType.ADVERB;
                break;
            case NOUN:
                synType = SynsetType.NOUN;
                break;
            case VERB:
                synType = SynsetType.VERB;
                break;
        }
        
        return wordNet.getSynsets(lemma, synType);
    }
    
    public static boolean isPunctuation(String word) {
        String trimmed = word.trim();
        
        return trimmed.equals("...") || trimmed.equals(".") || trimmed.equals("!") ||
                trimmed.equals("?") || trimmed.equals(",") || trimmed.equals(":") ||
                trimmed.equals(";") || trimmed.equals("-") || trimmed.equals("\""); 
    }
    
    public static boolean isStopWord(String word) {
        return stopWords.contains(word.toLowerCase());
    }
            
}
