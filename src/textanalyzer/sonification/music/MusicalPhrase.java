package textanalyzer.sonification.music;

import java.util.ArrayList;
import java.util.List;
import textanalyzer.sonification.lab.reactor.models.Atom;
import textanalyzer.sonification.lab.reactor.models.Matter;
import textanalyzer.sonification.lab.reactor.models.Molecule;

public class MusicalPhrase {
	private List<List<PitchedSound>> voices;
	
	public MusicalPhrase() {
		voices = new ArrayList<List<PitchedSound>>();
	}
	
	public static MusicalPhrase fromMatter(Matter m) {
		MusicalPhrase newMP = new MusicalPhrase();
		
		if (m instanceof Atom) {
			Atom a = (Atom) m;
			List<PitchedSound> topVoice = new ArrayList<PitchedSound>();
			List<PitchedSound> bottomVoice = new ArrayList<PitchedSound>();
			
			topVoice.add(a.getInfo());
			bottomVoice.add(PitchedSound.createRest());
			newMP.addVoice(topVoice);
			newMP.addVoice(bottomVoice);
		} else { 
			if (m instanceof Molecule) {
				List<Atom> atoms = ((Molecule)m).getAtoms();
				
				List<PitchedSound> topVoice = new ArrayList<PitchedSound>();
				List<PitchedSound> bottomVoice = new ArrayList<PitchedSound>();
				
				for (Atom a : atoms) {
					topVoice.add(a.getInfo());
					if (a.getVerticalLink() != null) {
					bottomVoice.add(a.getVerticalLink().getInfo());
					} else {
						bottomVoice.add(PitchedSound.createRest());
					}
				}
				
				newMP.addVoice(topVoice);
				newMP.addVoice(bottomVoice);
			} else {
				return null;
			}
		}
		
		return newMP;
	}
	
	public int getNrVoices() {
		return voices.size();
	}
	
	public List<PitchedSound> getVoice(int i) {
		if (voices.size() > i) {
			return voices.get(i);
		} else {
			return null;
		}
	}
	
	public void addVoice(List<PitchedSound> voice) {
		voices.add(voice);
	}
	
	public void setVoices(List<List<PitchedSound>> voices) {
		this.voices = voices;
	}
	
	@Override
	public String toString() {
		StringBuilder strBld = new StringBuilder();
		final int spacing = 5;
		
		for (PitchedSound a : getVoice(0)) {
			String aStr = a.toMusicString();
			strBld.append(aStr);
			for (int i = 0; i < spacing - aStr.length(); i++) {
				strBld.append(" ");
			}
		}
		
		strBld.append("\n");
		for (PitchedSound a : getVoice(1)) {
			
			String aStr = a.toMusicString();
			strBld.append(aStr);
			for (int i = 0; i < spacing - aStr.length(); i++) {
				strBld.append(" ");
			}
		}
		
		return strBld.toString();
	}
	
	public String toMusicalString() {
		StringBuilder strBld = new StringBuilder();
		
		strBld.append("V0 ");
				
		for (PitchedSound sound : getVoice(0)) {
			strBld.append(sound.toMusicString()).append(" ");
		}

		
		strBld.append("V1 ");
		
		for (PitchedSound sound : getVoice(1)) {
			strBld.append(sound.toMusicString()).append(" ");
		}
		
		return strBld.toString();
	}
}
