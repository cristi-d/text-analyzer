/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package textanalyzer.test;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.util.CoreMap;
import java.io.File;
import java.util.Collection;
import java.util.List;
import textanalyzer.model.doc.Document;
import textanalyzer.model.doc.Utterance;
import textanalyzer.model.doc.Voice;
import textanalyzer.model.lang.WordOccurance;
import textanalyzer.nlp.LanguageUtils;
import textanalyzer.nlp.TextAnalyzer;



/**
 *
 * @author cristiand
 */

public class Test {
     
    public static void main(String args[]) throws Exception {
        Document doc = new Document(new File("./data/chap01.xml"));
        
//        TextAnalyzer.detectUtterances(doc);
        TextAnalyzer.detectVoiceOccurances(doc);
        TextAnalyzer.detectVoiceInteranimation(doc);
        
        Utterance utterance = doc.getUtterances().get(68);
        
        CoreMap sentence = utterance.getOriginalTextAnnotation().get(CoreAnnotations.SentencesAnnotation.class).get(3);
       
        List<WordOccurance> voiceOccurances = doc.getVoiceOccurances();
        
        boolean stop = false;
        for (int i = 0; i < voiceOccurances.size() - 1; i++) {
            if (stop) {
                break;
            }
            WordOccurance wOcc1 = voiceOccurances.get(i);
            
            for (int j = i + 1; j < voiceOccurances.size(); j++) {
                WordOccurance wOcc2 = voiceOccurances.get(j);
                CoreMap sentence1 = wOcc1.getUtterance().getOriginalTextAnnotation().get(CoreAnnotations.SentencesAnnotation.class).get(wOcc1.getSentenceIndex());
                CoreMap sentence2 = wOcc2.getUtterance().getOriginalTextAnnotation().get(CoreAnnotations.SentencesAnnotation.class).get(wOcc1.getSentenceIndex());
                
                
                
                if (wOcc1.getUtteranceIndex()== wOcc2.getUtteranceIndex() &&
                        wOcc1.getSentenceIndex() == wOcc2.getSentenceIndex()) {
                    LanguageUtils.getPolyphonicRelationshipBySemanticGraph(wOcc1, wOcc2);
                    stop = true;
                    break;
                }
            }
        }
        
        
        
        
//        for (SemanticGraphEdge edge : semanticGraph.getIncomingEdgesSorted(dep)) {
//            System.out.println(edge.getGovernor() + " ---> " + edge.getDependent() + "[" + edge.getRelation().toString() + "]");
//        }
//        
//        for (SemanticGraphEdge edge : semanticGraph.getIncomingEdgesSorted(gov)) {
//            System.out.println(edge.getGovernor() + " ---> " + edge.getDependent() + "[" + edge.getRelation().toString() + "]");
//        }
        
//        for (SemanticGraphEdge edge : semanticGraph.getShortestDirectedPathEdges(gov, dep)) {
//            System.out.println(edge.getRelation().toString());
//        }
//        for (int i = 0; i < semanticGraph.size(); i++) {
//            try {
//                System.out.println(semanticGraph.getNodeByIndex(i));
//            }
//            catch (IllegalArgumentException e) {
//                    continue;
//            }
//        }
        
    }
}
