/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzer.model.doc;

import edu.stanford.nlp.util.CoreMap;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import textanalyzer.model.lang.Word;
import textanalyzer.model.lang.WordId;
import textanalyzer.model.lang.WordOccurance;

/**
 *
 * @author Cristi
 */
public class Document implements Serializable {
    private File projectFile;
    private String textFilePath;
    
    private String text;
    private List<CoreMap> sentences;
    private List<String> paragraphs;
    private Map<WordId, Word> wordMap;
    private int maxOccurs;
    private List<Word> wordList = null;
    
    private double lastThresh = 0.0;
    private List<Voice> definedVoices;
    private List<WordOccurance> voiceOccurances = null;
    private Map<String, Voice> voicesNameMap;
    
    private Personage narrator;
    private List<Personage> personages;
    private List<Utterance> utterances;
    private Map<String, Personage> personageAliasMap;
    private Map<String, Personage> personageNameMap;
    
    public static final String ROOT_TAG = "document";
    public static final String CHARACTERS_TAG = "characters";
    public static final String CHARACTER_TAG = "character";
    public static final String CHARACTER_NAME_TAG = "name";
    public static final String CHARACTER_ALIAS_TAG = "alias";
    public static final String VOICES_TAG = "voices";
    public static final String VOICE_TAG = "voice";
    public static final String VOICE_NAME_TAG = "name";
    public static final String VOICE_COLOR_TAG = "color";
    public static final String VOICE_TERM_TAG = "term";
    public static final String VOICE_TERM_LEMMA_TAG = "lemma";
    public static final String VOICE_TERM_POS_TAG = "pos";
    public static final String TEXT_FILE_TAG = "text-file";
    public static final String ANNOTATIONS_FILE_TAG = "annotations-file";
    public static final String UTTERANCES_TAG = "utterances";
    public static final String UTTERANCE_TAG = "utterance";
    public static final String UTTERANCE_EMMITER_ATTR = "emmiter";
    public static final String UTTERANCE_BEGINOFFSET_ATTR = "beginOffset";
    public static final String UTTERANCE_ENDOFFSET_ATTR = "endOffset";
    public static final String UTTERANCE_TYPE_ATTR = "type";
    public static final String UTTERANCE_ORIGINALTEXT_TAG = "originalText";
    public static final String UTTERANCE_PARSEDTEXT_TAG = "parsedText";
    public static final String WORDLIST_TAG = "word-list";
    public static final String WORD_TAG = "word";
    public static final String WORD_LEMMA_TAG = "lemma";
    public static final String WORD_POS_TAG = "pos";
    public static final String WORD_OCCURANCES_TAG = "occurances";
    
    private void initDataStructures() {
        definedVoices = new ArrayList<Voice>();
        voicesNameMap = new HashMap<String, Voice>();
        personages = new ArrayList<Personage>();
        personageAliasMap = new HashMap<String, Personage>();
        personageNameMap = new HashMap<String, Personage>();
        paragraphs = new ArrayList<String>();
        utterances = new ArrayList<Utterance>();
        wordMap = new HashMap<WordId, Word>();
        sentences = new ArrayList<CoreMap>();
        wordList = new ArrayList<Word>();
    }
    
    /*
     * Load from project file (.xml)
     */
    public Document(File file) throws Exception {
        initDataStructures();
        
        this.projectFile = file;
        
        textFilePath = null;
        BufferedReader reader = null;
        
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            org.w3c.dom.Document xmlDoc = dBuilder.parse(file);
            xmlDoc.normalize();
            
            //READ THE LIST OF PERSONAGES
            NodeList characterNodes = xmlDoc.getElementsByTagName(CHARACTER_TAG);
            for (int i = 0; i < characterNodes.getLength(); i++) {
                Node characterNode = characterNodes.item(i);
                List<String> aliases = new ArrayList<String>();
                String name = null;
                NodeList children = characterNode.getChildNodes();
                
                for (int j = 0; j < children.getLength(); j++) {
                    Node node = children.item(j);
                    if (node.getNodeName().trim().equals(CHARACTER_NAME_TAG)) {
                        name = node.getTextContent().trim();
                        continue;
                    }
                    
                    if (node.getNodeName().trim().equals(CHARACTER_ALIAS_TAG)) {
                        aliases.add(node.getTextContent().trim());
                    }
                }
                
                Personage pers = new Personage(name, aliases);
                personages.add(pers);
                personageNameMap.put(pers.getName(), pers);
                for (String alias : pers.getAliases()) {
                    personageAliasMap.put(alias, pers);
                }
            }
            
            if (xmlDoc.getElementsByTagName(TEXT_FILE_TAG).getLength() == 1) {
                Node node = xmlDoc.getElementsByTagName(TEXT_FILE_TAG).item(0);
                textFilePath = node.getTextContent().trim();
            }
            
            //READ VOICES
            for (int i = 0; i < xmlDoc.getElementsByTagName(VOICE_TAG).getLength(); i++) {
                Node nodeI = xmlDoc.getElementsByTagName(VOICE_TAG).item(i);
                Color color = null;
                String name = null;
                List<Word> terms = new ArrayList<Word>();
                
                NodeList nodeIChildren = nodeI.getChildNodes();
                for (int j = 0; j < nodeIChildren.getLength(); j++) {
                    Node nodeJ = nodeIChildren.item(j);
                    if (nodeJ.getNodeName().equals(VOICE_NAME_TAG)) {
                        name = nodeJ.getTextContent().trim();
                        continue;
                    }
                    
                    if (nodeJ.getNodeName().equals(VOICE_COLOR_TAG)) {
                        color = new Color(Integer.parseInt(nodeJ.getTextContent().trim()));
                        continue;
                    }
                    
                    if (nodeJ.getNodeName().equals(VOICE_TERM_TAG)) {
                        NodeList nodeJChildren = nodeJ.getChildNodes();
                        String lemma = null;
                        Word.POS pos = null;
                        for (int k = 0; k < nodeJChildren.getLength(); k++) {
                            Node nodeK = nodeJChildren.item(k);
                            if (nodeK.getNodeName().equals(VOICE_TERM_LEMMA_TAG)) {
                                lemma = nodeK.getTextContent().trim();
                                continue;
                            }
                            if (nodeK.getNodeName().equals(VOICE_TERM_POS_TAG)) {
                                pos = Word.POS.valueOf(nodeK.getTextContent().trim());
                            }
                        }
                        terms.add(new Word(lemma, pos));
                    }
                }
                
                Voice newVoice = new Voice(name, color);
                for (Word w : terms) {
                    newVoice.addAssociatedTerm(w);
                }
                definedVoices.add(newVoice);
            }
            
            //READ UTTERANCES
            NodeList utteranceTags = xmlDoc.getElementsByTagName(UTTERANCE_TAG);
            for (int i = 0; i < utteranceTags.getLength(); i++) {
                Node node = utteranceTags.item(i);
                NamedNodeMap attrs = node.getAttributes();
                
                Personage emmiter;
                int beginOffset, endOffset;
                Utterance.UtteranceType type;
                String originalText, parsedText;
                
                beginOffset = Integer.parseInt(attrs.getNamedItem(UTTERANCE_BEGINOFFSET_ATTR).getTextContent());
                endOffset = Integer.parseInt(attrs.getNamedItem(UTTERANCE_ENDOFFSET_ATTR).getTextContent());
                type = Utterance.UtteranceType.valueOf(attrs.getNamedItem(UTTERANCE_TYPE_ATTR).getTextContent());
                
                Node emmiterAttr = attrs.getNamedItem(UTTERANCE_EMMITER_ATTR);
                if (emmiterAttr != null) {
                    emmiter = personageNameMap.get(emmiterAttr.getTextContent().trim());
                } else {
                    emmiter = null;
                }
                
                originalText = ((Element)node).getElementsByTagName(UTTERANCE_ORIGINALTEXT_TAG).item(0).getTextContent().trim();
                if (((Element)node).getElementsByTagName(UTTERANCE_PARSEDTEXT_TAG).getLength() != 0) {
                    parsedText = ((Element)node).getElementsByTagName(UTTERANCE_PARSEDTEXT_TAG).item(0).getTextContent().trim();
                } else {
                    parsedText = originalText;
                }
                
                Utterance newUtterance = new Utterance(i, beginOffset, endOffset, parsedText, originalText, type, emmiter);
                utterances.add(newUtterance);
            }
            
            //READ WORD LIST
            NodeList wordTags = xmlDoc.getElementsByTagName(WORD_TAG);
            for (int i = 0; i < wordTags.getLength(); i++) {
                Element wordNode = (Element) wordTags.item(i);
                Node lemmaNode, posNode, occsNode;
                
                lemmaNode = wordNode.getElementsByTagName(WORD_LEMMA_TAG).item(0);
                posNode = wordNode.getElementsByTagName(WORD_POS_TAG).item(0);
                occsNode = wordNode.getElementsByTagName(WORD_OCCURANCES_TAG).item(0);
                
                String lemma = lemmaNode.getTextContent().trim();
                Word.POS pos = Word.POS.valueOf(posNode.getTextContent().trim());
                int occurances = Integer.parseInt(occsNode.getTextContent().trim());
                
                Word word = new Word(lemma, pos, occurances);
                wordList.add(word);
            }
            
            reader = null;
            
            maxOccurs = 0;
            
            
            //READ TEXT FILE
            reader = new BufferedReader(new FileReader(textFilePath));
            String line;
            StringBuilder strBld = new StringBuilder();
            
            while ((line = reader.readLine()) != null) {
                strBld.append(line).append("\n");
                paragraphs.add(line);
            }
            
            reader.close();
            text = strBld.toString();
        } catch (FileNotFoundException ex) {
            throw new Exception();
        } finally {
            try {
                reader.close();
            } catch (Exception ex) {
                ;
            }
        }
    }
    
    public Document(File textFile, File projectFile) throws Exception {
        initDataStructures();
        this.textFilePath = textFile.getPath();
        this.projectFile = projectFile;
        
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(textFile));
            String line;
            StringBuilder strBld = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                strBld.append(line).append("\n");
                paragraphs.add(line);
            }

            reader.close();
            text = strBld.toString();
        } catch (FileNotFoundException ex) {
            throw new Exception();
        } finally {
            try {
                reader.close();
            } catch (Exception ex) {
                ;
            }
        }
        
    }
    
    
    
    /*
     * Save to project file
     */
    public void save() throws Exception {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            org.w3c.dom.Document xmlDoc = db.newDocument();
            
            Element root = xmlDoc.createElement(ROOT_TAG);
            xmlDoc.appendChild(root);
            
            //ADD CHARACTERS LIST
            Element charactersNode = xmlDoc.createElement(CHARACTERS_TAG);
            root.appendChild(charactersNode);
            for (Personage p : personages) {
                Element characterNode = xmlDoc.createElement(CHARACTER_TAG);
                charactersNode.appendChild(characterNode);
                
                Element nameNode = xmlDoc.createElement(CHARACTER_NAME_TAG);
                nameNode.appendChild(xmlDoc.createTextNode(p.getName()));
                characterNode.appendChild(nameNode);
                
                for (String alias : p.getAliases()) {
                    Element aliasNode = xmlDoc.createElement(CHARACTER_ALIAS_TAG);
                    aliasNode.appendChild(xmlDoc.createTextNode(alias));
                    characterNode.appendChild(aliasNode);
                }
            }
            
            //ADD VOICES LIST
            Element voicesNode = xmlDoc.createElement(VOICES_TAG);
            root.appendChild(voicesNode);
            for (Voice voice : definedVoices) {
                Element voiceNode = xmlDoc.createElement(VOICE_TAG);
                voicesNode.appendChild(voiceNode);
                
                Element nameNode = xmlDoc.createElement(VOICE_NAME_TAG);
                nameNode.appendChild(xmlDoc.createTextNode(voice.getName()));
                voiceNode.appendChild(nameNode);
                
                Element colorNode = xmlDoc.createElement(VOICE_COLOR_TAG);
                colorNode.appendChild(xmlDoc.createTextNode(voice.getHighlightColor().getRGB() + ""));
                voiceNode.appendChild(colorNode);
                
                for (Word term : voice.getAssociatedTerms()) {
                    Element termNode = xmlDoc.createElement(VOICE_TERM_TAG);
                    voiceNode.appendChild(termNode);
                    
                    Element lemmaNode = xmlDoc.createElement(VOICE_TERM_LEMMA_TAG);
                    lemmaNode.appendChild(xmlDoc.createTextNode(term.getLemma()));
                    termNode.appendChild(lemmaNode);
                    
                    Element posNode = xmlDoc.createElement(VOICE_TERM_POS_TAG);
                    posNode.appendChild(xmlDoc.createTextNode(term.getPos().toString()));
                    termNode.appendChild(posNode);
                }
            }
            
            //ADD UTTERANCES LIST
            Element utterancesNode = xmlDoc.createElement(UTTERANCES_TAG);
            root.appendChild(utterancesNode);
            for (Utterance utterance : utterances) {
                Element utteranceNode = xmlDoc.createElement(UTTERANCE_TAG);
                utterancesNode.appendChild(utteranceNode);
                
                utteranceNode.setAttribute(UTTERANCE_BEGINOFFSET_ATTR, utterance.getBeginOffset() + "");
                utteranceNode.setAttribute(UTTERANCE_ENDOFFSET_ATTR, utterance.getEndOffset() + "");
                
                if (utterance.getEmmiter() != null) {
                    utteranceNode.setAttribute(UTTERANCE_EMMITER_ATTR, utterance.getEmmiter().getName());    
                }
                
                utteranceNode.setAttribute(UTTERANCE_TYPE_ATTR, utterance.getType().toString());
                
                Element originalTextNode = xmlDoc.createElement(UTTERANCE_ORIGINALTEXT_TAG);
                utteranceNode.appendChild(originalTextNode);
                originalTextNode.appendChild(xmlDoc.createTextNode(utterance.getOriginalText()));
                
                if (utterance.getParsedText() != null && 
                        utterance.getParsedText().equals(utterance.getOriginalText()) == false) {
                    Element parsedTextNode = xmlDoc.createElement(UTTERANCE_PARSEDTEXT_TAG);
                    utteranceNode.appendChild(parsedTextNode);
                    parsedTextNode.appendChild(xmlDoc.createTextNode(utterance.getParsedText()));
                }
            }
            
            
            Element textFileNode = xmlDoc.createElement(TEXT_FILE_TAG);
            root.appendChild(textFileNode);
            textFileNode.appendChild(xmlDoc.createTextNode(textFilePath));
            
            //ADD WORDS LIST
            Element wordsNode = xmlDoc.createElement(WORDLIST_TAG);
            root.appendChild(wordsNode);
            for (Word w : wordList) {
                Element wordNode = xmlDoc.createElement(WORD_TAG);
                wordsNode.appendChild(wordNode);
                
                Element lemmaNode = xmlDoc.createElement(WORD_LEMMA_TAG);
                lemmaNode.appendChild(xmlDoc.createTextNode(w.getLemma()));
                wordNode.appendChild(lemmaNode);
                
                Element posNode = xmlDoc.createElement(WORD_POS_TAG);
                posNode.appendChild(xmlDoc.createTextNode(w.getPos().toString()));
                wordNode.appendChild(posNode);
                
                Element occsNode = xmlDoc.createElement(WORD_OCCURANCES_TAG);
                occsNode.appendChild(xmlDoc.createTextNode(w.getOccurances() + ""));
                wordNode.appendChild(occsNode);
            }
            
            //Save to file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(xmlDoc);
            StreamResult result = new StreamResult(projectFile);

            transformer.transform(source, result);
                
            
        } catch (Exception e) {
            throw new Exception();
        }
    }

    public Personage getNarrator() {
        return narrator;
    }

    public void setNarrator(Personage narrator) {
        this.narrator = narrator;
    }
    
    public Personage getPersonageByAlias(String alias) {
        return personageAliasMap.get(alias);
    }
    
    public Personage getPersonageByName(String name) {
        return personageNameMap.get(name);
    }
    
    public void removePersonageByIndex(int index) {
        Personage p = this.personages.remove(index);
        personageNameMap.remove(p.getName());
        
        for (String alias : p.getAliases()) {
            personageAliasMap.remove(alias);
        }
    }
    
    public List<Personage> getPersonages() {
        return personages;
    }
    
    public void setUtterances(List<Utterance> utterances) {
        this.utterances = utterances;
    }
    
    public List<Utterance> getUtterances() {
        return utterances;
    }
    
    public List<Utterance> getMarkedUtterances() {
        List<Utterance> marked = new ArrayList<Utterance>();
        
        for (Utterance u : utterances) {
            if (u.getEmmiter() != null) {
                marked.add(u);
            }
        }
        
        return marked;
    }
    
    public List<String> getParagraphs() {
        return paragraphs;
    }
    
    public List<CoreMap> getSentences() {
        return sentences;
    }
    
    public String getText() {
        return text;
    }
    
    
    public List<WordOccurance> getVoiceOccurances() {
        return voiceOccurances;
    }
    
    public void setVoiceOccurances(List<WordOccurance> occurances) {
        this.voiceOccurances = occurances;
    }
    
    public void addPersonage(String name) {
        Personage pers = new Personage(name);
        personages.add(pers);
        personageNameMap.put(name, pers);
    }
    
    public void addPersonageAlias(int index, String alias) {
        Personage pers = personages.get(index);
        pers.addAlias(alias);
        
        personageAliasMap.put(alias, pers);
    }
    
    public void removePersonageAlias(int index, String alias) {
        Personage pers = personages.get(index);
        pers.removeAlias(alias);
        
        personageAliasMap.remove(alias);
    }
    
    public void addVoice(String name, Color color) {
        Voice v = new Voice(name, color);
        addVoice(v);
    }
    
    public void addVoice(Voice v) {
        definedVoices.add(v);
        voicesNameMap.put(v.getName(), v);
    }
    
    public Voice removeVoice(String name) {
        Voice v = voicesNameMap.get(name);
        
        if (v != null) {
            definedVoices.remove(v);
        }
        
        return v;
    }
    
    public Voice removeVoice(int index) {
        Voice v = definedVoices.get(index);
        
        definedVoices.remove(v);
        if (voiceOccurances != null) {
            for (WordOccurance occurance : v.getOccurances()) {
                voiceOccurances.remove(occurance);
            }
        } 
        
        
        return v;
    }
    
    public List<Voice> getVoices() {
        return definedVoices;
    }
    
    public Voice getVoice(int index) {
        return definedVoices.get(index);
    }
    
    public Voice getVoice(String name) {
        return voicesNameMap.get(name);
    }
    
    public String getSentenceText(int index) {
        return sentences.get(index).toString();
    }
       
    public int getNrSentences() {
        return sentences.size();
    }
    
    public List<Word> getWordsList() {
        return wordList;
    }
    
    public void setWordsList(List<Word> words) {
        this.wordList = words;
    }

    public final List<Word> detectFrequentWords(double threshold) {
        if (wordList != null && lastThresh == threshold) {
            return wordList;
        } 
        
        
        int minOccurs = (int) (threshold * maxOccurs);
        List<Word> temp = new ArrayList<Word>();
        
        for (WordId wId : wordMap.keySet()) {
            Word w = wordMap.get(wId);
            if (w.getOccurances() >= minOccurs) {
                temp.add(w);
            }
        }
        
        wordList = temp;
        lastThresh = threshold;
        
        Collections.sort(wordList, new Comparator<Word>() {
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
        
        return temp;
    }
    
    public File getProjectFile() {
        return projectFile;
    }
    
    @Override
    public String toString() {        
        return text;
    }
}
