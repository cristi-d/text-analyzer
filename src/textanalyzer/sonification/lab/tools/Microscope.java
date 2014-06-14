package textanalyzer.sonification.lab.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import textanalyzer.sonification.lab.reactor.models.Atom;
import textanalyzer.sonification.lab.reactor.models.Molecule;
import textanalyzer.sonification.lab.reactor.models.Molecule.MoleculeLayer;
import textanalyzer.sonification.music.BasicInterval;
import textanalyzer.sonification.music.Note;
import textanalyzer.sonification.music.Octave;
import textanalyzer.sonification.music.PitchedSound;
import textanalyzer.sonification.music.Scale;

/**
 * Utility class useful for analyzing a musical pattern
 * @author Cristi
 *
 */
public final class Microscope {


	private Microscope() {
		;
	}
	
	public static int scaleIndex(Atom atom, Scale scale) {
		return scaleIndex(atom.getInfo(), scale);
	}
	
	public static int scaleIndex(PitchedSound sound, Scale scale) {
		if (sound.isRest()) {
			return 0;
		}
		
		return sound.getNote().ordinal();
	}
	
	
	public static Map<Molecule.MoleculeLayer, List<BasicInterval>> detectHorizontalIntervals(Molecule m) {
		Map<Molecule.MoleculeLayer, List<BasicInterval>> result = new HashMap<Molecule.MoleculeLayer, List<BasicInterval>>();
		
		
		for (MoleculeLayer layer : MoleculeLayer.values()) {
			List<Atom> atoms = null;
			
			switch (layer) {
				case Top:
					atoms = m.getAtoms();
					break;
				case Bottom:
					atoms = new ArrayList<Atom>();
					
					for (Atom a : m.getAtoms()) {
						atoms.add(a.getVerticalLink());
					}
					break;
			}
			
			List<BasicInterval> intervals = new ArrayList<BasicInterval>();
			Atom prevAtom;
			Iterator<Atom> atIt = atoms.iterator();
			
			prevAtom = atIt.next();
			
			while (atIt.hasNext()) {
				Atom currentAtom = atIt.next();
				PitchedSound prevSound, currentSound;
				prevSound = prevAtom.getInfo();
				currentSound = currentAtom.getInfo();
				
				if (prevSound.isRest() || currentSound.isRest()) {
					intervals.add(BasicInterval.NO_INTERVAL);
					continue;
				}
				
				if (prevSound.compareTo(currentSound) <= 0) {
					intervals.add(TapeMeasure.measureAscendingBasicInterval(prevSound, currentSound));
				} else {
					intervals.add(TapeMeasure.measureAscendingBasicInterval(currentSound, prevSound));
				}
				
				prevAtom = currentAtom;
			}
			result.put(layer, intervals);
		}
		
		
		
		
		return result;
	}
	
	public static List<BasicInterval> detectHorizontalIntervals(List<PitchedSound> sounds) {

		List<BasicInterval> intervals = new ArrayList<BasicInterval>();
		PitchedSound prevSound;
		Iterator<PitchedSound> atIt = sounds.iterator();
		
		prevSound = atIt.next();
		
		while (atIt.hasNext()) {
			PitchedSound currentSound = atIt.next();

			if (prevSound.isRest() || currentSound.isRest()) {
				intervals.add(BasicInterval.NO_INTERVAL);
				continue;
			}
			
			if (prevSound.compareTo(currentSound) <= 0) {
				intervals.add(TapeMeasure.measureAscendingBasicInterval(prevSound, currentSound));
			} else {
				intervals.add(TapeMeasure.measureAscendingBasicInterval(currentSound, prevSound));
			}
			
			prevSound = currentSound;
		}
	
		return intervals;
	}
	
	public static List<BasicInterval> detectVerticalIntervals(Molecule m) {
		List<BasicInterval> intervals = new ArrayList<BasicInterval>();
		Iterator<Atom> atIt = m.getAtoms().iterator();
		
		while (atIt.hasNext()) {
			Atom atom = atIt.next();
			Atom counter = atom.getVerticalLink();
			
			if (atom.getInfo().isRest() || counter.getInfo().isRest()) {
				intervals.add(BasicInterval.NO_INTERVAL);
				continue;
			}
			
			if (atom.getInfo().compareTo(counter.getInfo()) <= 0) {
				intervals.add(TapeMeasure.measureAscendingBasicInterval(atom.getInfo(), counter.getInfo()));
			} else {
				intervals.add(TapeMeasure.measureAscendingBasicInterval(counter.getInfo(), atom.getInfo()));
			}
		}
		
		return intervals;
	}
	
	public static List<BasicInterval> detectVerticalIntervals(List<List<PitchedSound>> soundLayers) {
		List<BasicInterval> intervals = new ArrayList<BasicInterval>();
		int size = soundLayers.get(0).size();
		
		List<PitchedSound> top = soundLayers.get(0);
		List<PitchedSound> bottom = soundLayers.get(1);
		
		for (int i = 0; i < size; i++) {
			PitchedSound sound = top.get(i);
			PitchedSound counterSound = bottom.get(i);
			
			if (sound.isRest() || counterSound.isRest()) {
				intervals.add(BasicInterval.NO_INTERVAL);
				continue;
			}
			
			if (sound.compareTo(counterSound) <= 0) {
				intervals.add(TapeMeasure.measureAscendingBasicInterval(sound, counterSound));
			} else {
				intervals.add(TapeMeasure.measureAscendingBasicInterval(counterSound, sound));
			}
		}
			
		return intervals;
	}
	
	
	
	public static void main(String args[]) {
		Molecule m = new Molecule();
		List<Atom> aL = new ArrayList<Atom>();
		Atom a;
		
		
		a = new Atom(PitchedSound.create(Note.C, Octave.Fourth));
		a.setVerticalLink(new Atom(PitchedSound.create(Note.E, Octave.Fourth)));
		aL.add(a);
		
		a = new Atom(PitchedSound.create(Note.F, Octave.Fourth));
		a.setVerticalLink(new Atom(PitchedSound.createRest()));
		aL.add(a);
		
		a = new Atom(PitchedSound.create(Note.G, Octave.Fourth));
		a.setVerticalLink(new Atom(PitchedSound.create(Note.F, Octave.Fourth)));
		aL.add(a );
		
		a = new Atom(PitchedSound.createRest());
		a.setVerticalLink(new Atom(PitchedSound.create(Note.G, Octave.Fourth)));
		aL.add(a);
		
		a = new Atom(PitchedSound.create(Note.C, Octave.Fifth));
		a.setVerticalLink(new Atom(PitchedSound.create(Note.C, Octave.Fourth)));
		aL.add(a);
		
		m.setAtoms(aL);
		System.out.println("Pattern:\n" + m);
		System.out.println("\nHorizontal intervals:");
		Map<MoleculeLayer, List<BasicInterval>> intervals = Microscope.detectHorizontalIntervals(m);
		System.out.println("\t- Top layer:\n\t" + intervals.get(MoleculeLayer.Top));
		System.out.println();
		System.out.println("\t- Bottom layer:\n\t" + intervals.get(MoleculeLayer.Bottom));
		
		System.out.println("\nVertical intervals:\n" + Microscope.detectVerticalIntervals(m));
	}

}
