/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package textanalyzer.sonification.lab.reactor;

import textanalyzer.sonification.lab.reactor.models.Matter;
import textanalyzer.sonification.lab.reactor.models.Molecule;
import textanalyzer.sonification.lab.rules.CollisionRule;
import textanalyzer.sonification.lab.rules.modulation.ThemeModulationRule;

/**
 *
 * @author cristiand
 */
public class ModulatorRandomizedReactorAlgorithm extends RandomizedReactorAlgorithm {

    @Override
    protected Matter atomToAtomCollision(Matter base, Matter addition, Molecule.MoleculeLayer layer) {
        return CollisionRule.getRule(ThemeModulationRule.class).apply(base, addition, layer);
    }

    @Override
    protected Matter atomToMoleculeCollision(Matter base, Matter addition, Molecule.MoleculeLayer layer) {
        return CollisionRule.getRule(ThemeModulationRule.class).apply(base, addition, layer);
    }

    @Override
    protected Matter moleculeToMoleculeCollision(Matter base, Matter addition, Molecule.MoleculeLayer layer) {
        return CollisionRule.getRule(ThemeModulationRule.class).apply(base, addition, null);
    }

    @Override
    protected boolean isAtomToAtomCollisionPossible(Matter base, Matter addition, Molecule.MoleculeLayer layer) {
        return CollisionRule.getRule(ThemeModulationRule.class).canApply(base, addition, layer);
    }

    @Override
    protected boolean isAtomToMoleculeCollisionPossible(Matter base, Matter addition, Molecule.MoleculeLayer layer) {
        return CollisionRule.getRule(ThemeModulationRule.class).canApply(base, addition, layer);
    }

    @Override
    protected boolean isMoleculeToMoleculeCollisionPossible(Matter base, Matter addition, Molecule.MoleculeLayer layer) {
        return CollisionRule.getRule(ThemeModulationRule.class).canApply(base, addition, layer);
    }
    
}
