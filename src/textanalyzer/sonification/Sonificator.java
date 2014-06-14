/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package textanalyzer.sonification;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfugue.MusicStringParser;
import org.jfugue.MusicXmlRenderer;
import org.jfugue.Pattern;
import org.jfugue.Player;
import textanalyzer.model.music.MusicalStructure;
import textanalyzer.model.music.Phrase;
import textanalyzer.sonification.lab.reactor.GenericRandomizedReactorAlgorithm;
import textanalyzer.sonification.lab.reactor.ModulatorRandomizedReactorAlgorithm;
import textanalyzer.sonification.lab.reactor.Tank;
import textanalyzer.sonification.lab.reactor.models.Molecule;
import textanalyzer.sonification.music.MusicalPhrase;
import textanalyzer.sonification.music.PitchedSound;

/**
 *
 * @author cristiand
 */
public final class Sonificator {
    private Sonificator() {
        
    }
    
    private static void startTank(Tank tank) {
        new Thread(tank).start();
            
                synchronized (tank) {
                    try {
                        tank.wait();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
    }
    
    private static Pattern buildPattern(String instrument1, String instrument2, MusicalPhrase phrase) {
        StringBuilder strBld = new StringBuilder();
				
        strBld.append("V0 I[").append(instrument1).append("] ");

        for (PitchedSound sound : phrase.getVoice(0)) {
                strBld.append(sound.toMusicString()).append(" ");
        }

        strBld.append("V1 I[").append(instrument2).append("] ");

        for (PitchedSound sound : phrase.getVoice(1)) {
                strBld.append(sound.toMusicString()).append(" ");
        }

        Pattern pattern = new Pattern(strBld.toString());
        
        return pattern;
    }
    
    public static void writeMusicXML(String filename, Pattern song) {
		MusicXmlRenderer renderer = new MusicXmlRenderer();
		MusicStringParser parser = new MusicStringParser();
		parser.addParserListener(renderer);
		parser.parse(song);
		
		try {
			File outFile = new File(filename);
			PrintWriter writer;
			writer = new PrintWriter(outFile);
			writer.println(renderer.getMusicXMLString());
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
    
    public static MusicalPhrase compose(MusicalStructure structure) {
        
        Tank tank;
        try {
            tank = new Tank(GenericRandomizedReactorAlgorithm.class);
            
            //Generate themes
            Map<String, Molecule> voiceThemes = new HashMap<String, Molecule>();
            tank.setMaxSteps(10);
            for (String voice : structure.getVoices()) {
                tank.cleanup();
                tank.addRandomMatter(100);
                
                startTank(tank);

                Molecule molecule = (Molecule) tank.getLargestMolecule();
                voiceThemes.put(voice, molecule);
            }
            
            List<MusicalPhrase> musicalPhrases = new ArrayList<MusicalPhrase>();
            List<Pattern> patterns = new ArrayList<Pattern>();
            
            tank.setReactorAlgorithm(ModulatorRandomizedReactorAlgorithm.class);
            tank.setMaxSteps(25);
            for (Phrase phrase : structure.getPhrases()) {
                Molecule theme = voiceThemes.get(phrase.getVoice());
                
                tank.cleanup();
                tank.addMatter(new Molecule(theme));
                tank.addRandomMatter(30);
                
                startTank(tank);
                
                Molecule m = (Molecule) tank.getLargestMolecule();
                
                if (m == null) {
                    System.out.println("Molecule is null!");
                }
                
                patterns.add(buildPattern("Flute", "Piano", MusicalPhrase.fromMatter(m)));
                
                
            }
            
            Player player = new Player();
            Pattern song = new Pattern();
            
            for (Pattern pattern : patterns) {
                System.out.println(pattern.toString());
                song.add(pattern);
            }
            
            writeMusicXML("song.xml", song);
            player.play(song);

            
            
//            if (m == null) {
//                    System.out.println("No molecule created... weird!");
//            } else {
//                    MusicalPhrase phrase = MusicalPhrase.fromMatter(m);
//
//                    System.out.println(phrase);
//
//                    StringBuilder strBld = new StringBuilder();
//
//                    strBld.append("V0 I[Cello] ");
//
//                    for (PitchedSound sound : phrase.getVoice(0)) {
//                            strBld.append(sound.toMusicString()).append(" ");
//                    }
//
//                    strBld.append("V1 I[Flute]");
//
//                    for (PitchedSound sound : phrase.getVoice(1)) {
//                            strBld.append(sound.toMusicString()).append(" ");
//                    }
//
//                    Pattern song = new Pattern(strBld.toString());
//                    Player player = new Player();
//                    player.play(song);
//				
//            }
        } catch (Exception e) {
            e.printStackTrace();;
        }
        
        return null;
    }
       
    public static void main(String args[]) {
        Tank tank;
        try {
            tank = new Tank(GenericRandomizedReactorAlgorithm.class);
            tank.addRandomMatter(100);

            new Thread(tank).start();

            synchronized (tank) {
                try {
                    tank.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            Molecule m = (Molecule) tank.getLargestMolecule();
            
            if (m == null) {
				System.out.println("No molecule created... weird!");
			} else {
				MusicalPhrase phrase = MusicalPhrase.fromMatter(m);
				
				System.out.println(phrase);
				
				StringBuilder strBld = new StringBuilder();
				
				strBld.append("V0 I[Cello] ");
						
				for (PitchedSound sound : phrase.getVoice(0)) {
					strBld.append(sound.toMusicString()).append(" ");
				}
				
				strBld.append("V1 I[Flute]");
				
				for (PitchedSound sound : phrase.getVoice(1)) {
					strBld.append(sound.toMusicString()).append(" ");
				}
				
				Pattern song = new Pattern(strBld.toString());
				Player player = new Player();
				player.play(song);
				
            }
        } catch (Exception e) {
            e.printStackTrace();;
        }
    }
}
