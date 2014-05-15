/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package textanalyzer.test;

import java.io.File;
import textanalyzer.model.doc.Document;
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
    }
}
