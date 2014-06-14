package textanalyzer.sonification.lab.rules.generation;

import textanalyzer.sonification.lab.rules.CollisionRule;
import textanalyzer.sonification.lab.reactor.models.Atom;
import textanalyzer.sonification.lab.reactor.models.Matter;
import textanalyzer.sonification.lab.reactor.models.Molecule;
import textanalyzer.sonification.lab.reactor.models.Molecule.MoleculeLayer;
import textanalyzer.sonification.music.PitchedSound;
import textanalyzer.sonification.music.theory.HorizontalCoherenceMusicalRule;
import textanalyzer.sonification.music.theory.MusicalRule;

public class AtomAtomCollisionRule extends CollisionRule {

	@Override
	public boolean canApply(Matter base, Matter addition, MoleculeLayer targetLayer) {
		PitchedSound from = ((Atom) base).getInfo();
		PitchedSound to = ((Atom) addition).getInfo();
		
		return MusicalRule.getRule(HorizontalCoherenceMusicalRule.class).intervalComplies(from, to);
	}

	@Override
	public Matter apply(Matter base, Matter addition, MoleculeLayer targetLayer) {
		Atom baseAtom = (Atom) base;
		Atom additionAtom = (Atom) addition;
		Molecule molecule = null;
		
		switch (targetLayer) {
			case Bottom:
				molecule = new Molecule();
				molecule.addAtom(baseAtom, MoleculeLayer.Top);
				molecule.addAtom(additionAtom, MoleculeLayer.Bottom);
				break;
			case Top:
				molecule = new Molecule();
				molecule.addAtom(baseAtom, MoleculeLayer.Top);
				molecule.addAtom(additionAtom, MoleculeLayer.Top);
				break;
		}
		
		return molecule;
	}

}
