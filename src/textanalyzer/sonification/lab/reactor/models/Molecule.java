package textanalyzer.sonification.lab.reactor.models;

import java.util.ArrayList;
import java.util.List;
import textanalyzer.sonification.music.PitchedSound;

public class Molecule implements Matter {
	private List<Atom> atoms;
	
	public enum MoleculeLayer {
		Top, Bottom;
	}
	
	public Molecule() {
		atoms = new ArrayList<Atom>();
	}
        
        public Molecule(Molecule copyThis)  {
            atoms = new ArrayList<Atom>();
            
            for (Atom atom : copyThis.getAtoms()) {
                atoms.add(new Atom(atom));
            }
        }

	public List<Atom> getAtoms() {
		return atoms;
	}
	
	public Atom getLastTopLayerAtom() {
		return atoms.get(atoms.size() - 1);
	}

	public void setAtoms(List<Atom> atoms) {
		this.atoms = atoms;
	}
	
	public void addAtom(Atom atom, MoleculeLayer layer) {
		switch (layer) {
		case Top:
			
			if (atoms.size() != 0) {
				Atom lastAtom = atoms.get(atoms.size() - 1);
				if (lastAtom.getVerticalLink() == null) {
					lastAtom.setVerticalLink(new Atom(PitchedSound.createRest()));
				}
			}
			atoms.add(atom);
			break;
		case Bottom:
			if (atoms.size() != 0) {
				atoms.get(atoms.size() - 1).setVerticalLink(atom);
			} else {
				atoms.add(atom);
			}
			break;
		}
	}
	
	public void linkTo(Molecule m) {
		atoms.addAll(m.getAtoms());
	}
	
	@Override 
	public String toString() {
		StringBuilder strBld = new StringBuilder();
		final int spacing = 5;
		
		for (Atom a : atoms) {
			String aStr = a.getInfo().toMusicString();
			strBld.append(aStr);
			for (int i = 0; i < spacing - aStr.length(); i++) {
				strBld.append(" ");
			}
		}
		
		strBld.append("\n");
		for (Atom a : atoms) {
			if (a.getVerticalLink() == null) {
				continue;
			}
			String aStr = a.getVerticalLink().getInfo().toMusicString();
			strBld.append(aStr);
			for (int i = 0; i < spacing - aStr.length(); i++) {
				strBld.append(" ");
			}
		}
		
		return strBld.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Molecule == false) {
			return false;
		}
		
		Molecule m = (Molecule) o;
		
		if (m.getAtoms().size() != atoms.size()) {
			return false;
		}
		
		for (int i = 0; i < atoms.size(); i++) {
			if (atoms.get(i).equals(m.getAtoms().get(i)) == false) {
				return false;
			}
		}
		
		return true;
	}
        
}

