package textanalyzer.sonification.lab.reactor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import textanalyzer.sonification.exceptions.lab.reactor.NoReactionException;
import textanalyzer.sonification.exceptions.lab.reactor.ReactorAlgorithmSetupException;
import textanalyzer.sonification.lab.reactor.models.Atom;
import textanalyzer.sonification.lab.reactor.models.Matter;
import textanalyzer.sonification.lab.reactor.models.Molecule;
import textanalyzer.sonification.music.Note;
import textanalyzer.sonification.music.Octave;
import textanalyzer.sonification.music.PitchedSound;

/**
 * Here is where the atoms = notes live
 * Current version: just keep track of how many there are
 * Probable later version: more complex simulation - trajectory, collisions, energy, etc.
 * @author Cristi
 *
 */

public class Tank implements Runnable {
	private Collection<Matter> matter;
	private Random random;
	private int maxSteps;
        
	public Tank(Class<? extends ReactorAlgorithm> algorithmType) throws ReactorAlgorithmSetupException {
		matter = new ArrayList<Matter>();
		random = new Random(System.currentTimeMillis());
		
		ReactorAlgorithmFactory.setupReactorAlgorithm(algorithmType);
                maxSteps = -1;
	}
        
        public void setReactorAlgorithm(Class<? extends ReactorAlgorithm> algorithmType) throws ReactorAlgorithmSetupException {
            ReactorAlgorithmFactory.setupReactorAlgorithm(algorithmType);
        }
        
        public void setMatter(Collection<Matter> matter) {
            this.matter = matter;
        }
	
	public void addMatter(Collection<Matter> matter) {
		this.matter.addAll(matter);
	}
        
        public void addMatter(Matter matter) {
            this.matter.add(matter);
        }
        
        public void cleanup() {
            this.matter.clear();
        }
        
        public void setMaxSteps(int maxSteps) {
            this.maxSteps = maxSteps;
        }
	
	public void addRandomMatter(int size) {
	
            for (int i = 0; i < size; i++) {
                int noteIndex = random.nextInt(Note.values().length);
                Octave octaves[] = {Octave.Third, Octave.Fourth};
                int octaveIndex = random.nextInt(octaves.length);
                
                Note note = Note.values()[noteIndex];
                Octave oct = octaves[octaveIndex];
                
                matter.add(new Atom(PitchedSound.create(note, oct)));
//                if (note != Note.C && note != Note.F) {
//                    matter.add(new Atom(PitchedSound.create(note, oct, Intonation.Flat)));
//                }
//                if (note != Note.B && note != Note.E) {
//                    matter.add(new Atom(PitchedSound.create(note, oct, Intonation.Sharp)));
//                }
            }
            
//		for (Note note : Note.values()) {
//			int copies = 4;
//			
//			
//			
//			for (Octave oct : octaves) {
//				for (int i = 0; i < copies; i++) {
////					if (note.equals(Note.F) || note.equals(Note.C)) {
////						matter.add(new Atom(PitchedSound.create(note, oct)));
////						matter.add(new Atom(PitchedSound.create(note, oct, Intonation.Sharp)));
////						continue;
////					}
////					
////					if (note.equals(Note.B) || note.equals(Note.E)) {
////						matter.add(new Atom(PitchedSound.create(note, oct)));
////						matter.add(new Atom(PitchedSound.create(note, oct, Intonation.Flat)));
////						continue;
////					}
////						
////					for (Intonation into : Intonation.values()) {
////						matter.add(new Atom(PitchedSound.create(note, oct, into)));
////					}
//					
//					matter.add(new Atom(PitchedSound.create(note, oct)));
//                                        if (note != Note.C && note != Note.F) {
//                                            matter.add(new Atom(PitchedSound.create(note, oct, Intonation.Flat)));
//                                        }
//                                        if (note != Note.B && note != Note.E) {
//                                            matter.add(new Atom(PitchedSound.create(note, oct, Intonation.Sharp)));
//                                        }
//				
//					
//					
//				}
//			}
//		}
	}
	
	@Override
	public void run() {
		
                int step = 0;
		while (matter.size() > 1 && (maxSteps == -1 || step < maxSteps)) {
			
			Collection<Matter> sample = new ArrayList<Matter>();
			int nr = 2 + random.nextInt(matter.size() / 2);
			List<Boolean> hits = new ArrayList<Boolean>();
			
			for (int i = 0; i < nr; i++) {
				hits.add(true);
			}
			
			for (int i = nr; i < matter.size(); i++) {
				hits.add(false);
			}
			
			Collections.shuffle(hits);
			
			Iterator<Matter> matterIt = matter.iterator();
			Iterator<Boolean> hitsIt = hits.iterator();
			
			while (matterIt.hasNext()) {
				if (hitsIt.next()) {
					sample.add(matterIt.next());
				} else {
					matterIt.next();
				}
			}
			
			
			
			try {
				Collection<Matter> result = ReactorAlgorithm.getInstance().mixMatter(sample);
                                if (result.isEmpty() == false) {
                                    Iterator<Matter> sampleIt = sample.iterator();
			
                                    while (sampleIt.hasNext()) {
                                            matter.remove(sampleIt.next());
                                    }
                                }
                                matter.addAll(result);
			} catch (NoReactionException e) {
				;
			}
                        
                        step++;
		}
		
		synchronized (this) {
			this.notify();
		}
	}
	
	public Collection<Matter> getMatter() {
		return matter;
	}
	
	public Molecule getLargestMolecule() {
		int maxSize = 0;
		Molecule largestMolecle = null;
		
		for (Matter m : matter) {
			if (m instanceof Molecule) {
				Molecule molecule = (Molecule) m;
				if (molecule.getAtoms().size() > maxSize) {
					maxSize = molecule.getAtoms().size();
					largestMolecle = molecule;
				}
			}
		}
		
		return largestMolecle;
	}
}
