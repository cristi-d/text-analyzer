package textanalyzer.sonification.lab.reactor;

import textanalyzer.sonification.lab.reactor.models.Matter;
import textanalyzer.sonification.lab.reactor.models.Molecule.MoleculeLayer;
import textanalyzer.sonification.lab.rules.CollisionRule;
import textanalyzer.sonification.lab.rules.generation.AtomAtomCollisionRule;
import textanalyzer.sonification.lab.rules.generation.MoleculeAtomCollisionRule;
import textanalyzer.sonification.lab.rules.generation.MoleculeMoleculeCollisionRule;

/**
 * Part of the Tank, for basic version.
 * Responsible with applying the recombination rules <- from musical theory
 * @author Cristi
 */
public final class GenericRandomizedReactorAlgorithm extends RandomizedReactorAlgorithm {

    protected GenericRandomizedReactorAlgorithm() {
            super();
    }
        
    @Override
    protected void initialize() {
            instance = this;
    }

    @Override
    protected Matter atomToAtomCollision(Matter base, Matter addition, MoleculeLayer layer) {
        return CollisionRule.getRule(AtomAtomCollisionRule.class).apply(base, addition, layer);
    }

    @Override
    protected Matter atomToMoleculeCollision(Matter base, Matter addition, MoleculeLayer layer) {
        return CollisionRule.getRule(MoleculeAtomCollisionRule.class).apply(base, addition, layer);
    }

    @Override
    protected Matter moleculeToMoleculeCollision(Matter base, Matter addition, MoleculeLayer layer) {
        return CollisionRule.getRule(MoleculeMoleculeCollisionRule.class).apply(base, addition, null);
    }

    @Override
    protected boolean isAtomToAtomCollisionPossible(Matter base, Matter addition, MoleculeLayer layer) {
        return CollisionRule.getRule(AtomAtomCollisionRule.class).canApply(base, addition, layer);
    }

    @Override
    protected boolean isAtomToMoleculeCollisionPossible(Matter base, Matter addition, MoleculeLayer layer) {
        return CollisionRule.getRule(MoleculeAtomCollisionRule.class).canApply(base, addition, layer);
    }

    @Override
    protected boolean isMoleculeToMoleculeCollisionPossible(Matter base, Matter addition, MoleculeLayer layer) {
        return CollisionRule.getRule(MoleculeMoleculeCollisionRule.class).canApply(base, addition, null);
    }
}
