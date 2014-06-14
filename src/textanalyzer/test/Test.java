/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package textanalyzer.test;

import java.io.File;
import textanalyzer.model.doc.Document;
import textanalyzer.sonification.MusicalStructureAnalyzer;
import textanalyzer.model.music.MusicalStructure;
import textanalyzer.nlp.TextAnalyzer;
import textanalyzer.sonification.Sonificator;



/**
 *
 * @author cristiand
 */

public class Test {
     
    public static void main(String args[]) throws Exception {
        Document doc = new Document(new File("./data/chap01.xml"));
        
        TextAnalyzer.detectVoiceOccurances(doc);
        TextAnalyzer.detectVoiceInteranimation(doc);
        
        MusicalStructure musicalStructure = MusicalStructureAnalyzer.computeMusicalStructure(doc);
        Sonificator.compose(musicalStructure);
    }
}
