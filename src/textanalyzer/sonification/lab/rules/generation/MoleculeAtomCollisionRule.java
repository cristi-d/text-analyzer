package textanalyzer.sonification.lab.rules.generation;

import textanalyzer.sonification.lab.rules.CollisionRule;
import textanalyzer.sonification.lab.reactor.models.Atom;
import textanalyzer.sonification.lab.reactor.models.Matter;
import textanalyzer.sonification.lab.reactor.models.Molecule;
import textanalyzer.sonification.lab.reactor.models.Molecule.MoleculeLayer;
import textanalyzer.sonification.music.MusicalPhrase;
import textanalyzer.sonification.music.PitchedSound;
import textanalyzer.sonification.music.theory.HorizontalCoherenceMusicalRule;
import textanalyzer.sonification.music.theory.HorizontalStabilityMusicalRule;
import textanalyzer.sonification.music.theory.MusicalRule;
import textanalyzer.sonification.music.theory.VerticalCoherenceMusicalRule;

public class MoleculeAtomCollisionRule extends CollisionRule {

	@Override
	public boolean canApply(Matter base, Matter addition, MoleculeLayer targetLayer) {
		Molecule baseMolecule = (Molecule) base;
		Atom additionAtom = (Atom) addition;
		PitchedSound baseLastSound = baseMolecule.getLastTopLayerAtom().getInfo();
		PitchedSound additionAtomSound = additionAtom.getInfo();
		
		Class<? extends MusicalRule> ruleClass = null;
		
		MusicalPhrase basePhrase = MusicalPhrase.fromMatter(baseMolecule);
		MusicalPhrase additionPhrase = MusicalPhrase.fromMatter(additionAtom);
		
		if (targetLayer == null) {
			return (MusicalRule.getRule(VerticalCoherenceMusicalRule.class).intervalComplies(baseLastSound, additionAtomSound)
					|| MusicalRule.getRule(HorizontalCoherenceMusicalRule.class).intervalComplies(baseLastSound, additionAtomSound))
					&& MusicalRule.getRule(HorizontalStabilityMusicalRule.class).complies(basePhrase, additionPhrase);
		}
		
		switch (targetLayer) {
		case Bottom:
			ruleClass = VerticalCoherenceMusicalRule.class;
			break;
		case Top:
			ruleClass = HorizontalCoherenceMusicalRule.class;
			break;
		}
		
		
		
		return MusicalRule.getRule(ruleClass).intervalComplies(baseLastSound, additionAtomSound) &&
				MusicalRule.getRule(HorizontalStabilityMusicalRule.class).complies(basePhrase, additionPhrase);
	}

	@Override
	public Matter apply(Matter base, Matter addition, MoleculeLayer targetLayer) {
		Molecule baseMolecule = (Molecule) base;
		Atom additionAtom = (Atom) addition;
		
		baseMolecule.addAtom(additionAtom, targetLayer);
		
		return baseMolecule;
	}


}
