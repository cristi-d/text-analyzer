package textanalyzer.sonification.lab.rules.generation;

import textanalyzer.sonification.lab.rules.CollisionRule;
import textanalyzer.sonification.lab.reactor.models.Matter;
import textanalyzer.sonification.lab.reactor.models.Molecule;
import textanalyzer.sonification.lab.reactor.models.Molecule.MoleculeLayer;
import textanalyzer.sonification.music.MusicalPhrase;
import textanalyzer.sonification.music.theory.HorizontalCoherenceMusicalRule;
import textanalyzer.sonification.music.theory.HorizontalStabilityMusicalRule;
import textanalyzer.sonification.music.theory.MusicalRule;

public class MoleculeMoleculeCollisionRule extends CollisionRule {

	@Override
	public boolean canApply(Matter base, Matter addition, MoleculeLayer targetLayer) {
		MusicalPhrase basePhrase = MusicalPhrase.fromMatter(base);
		MusicalPhrase additionPhrase = MusicalPhrase.fromMatter(addition);
		
		return MusicalRule.getRule(HorizontalCoherenceMusicalRule.class).complies(basePhrase, additionPhrase)
				&& MusicalRule.getRule(HorizontalStabilityMusicalRule.class).complies(basePhrase, additionPhrase);
	}

	@Override
	public Matter apply(Matter base, Matter addition, MoleculeLayer targetLayer) {
		Molecule baseMolecule = (Molecule) base;
		Molecule additionMolecule = (Molecule) addition;
		
		baseMolecule.linkTo(additionMolecule);
		return baseMolecule;
	}

}
