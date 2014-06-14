/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package textanalyzer.sonification.lab.rules.modulation;

import java.util.List;
import textanalyzer.sonification.lab.reactor.models.Atom;
import textanalyzer.sonification.lab.reactor.models.Matter;
import textanalyzer.sonification.lab.reactor.models.Molecule;
import textanalyzer.sonification.lab.rules.CollisionRule;
import textanalyzer.sonification.music.theory.HorizontalCoherenceMusicalRule;
import textanalyzer.sonification.music.theory.MusicalRule;
import textanalyzer.sonification.music.theory.VerticalCoherenceMusicalRule;

/**
 *
 * @author cristiand
 */
public class ThemeModulationRule extends CollisionRule {
    
    
    @Override
    public boolean canApply(Matter base, Matter addition, Molecule.MoleculeLayer targetLayer) {
        boolean canApply = false;
        
        if (base instanceof Molecule) {
            if (addition instanceof Atom) {
                Atom newAtom = (Atom) addition;
                Molecule baseMolecule = (Molecule) base;
                
                List<Atom> topAtoms = baseMolecule.getAtoms();
                
                for (int i = 1; i < topAtoms.size() - 1; i++) {
                    Atom topAtom = topAtoms.get(i);
                    Atom bottomAtom = topAtom.getVerticalLink();
                    
                    if (bottomAtom == null) {
                        canApply = true;
                        continue;
                    }

                    Atom previousTopAtom = topAtoms.get(i - 1);
                    Atom nextTopAtom = topAtoms.get(i + 1);
                    if (MusicalRule.getRule(HorizontalCoherenceMusicalRule.class).intervalComplies(previousTopAtom.getInfo(), newAtom.getInfo()) &&
                            MusicalRule.getRule(HorizontalCoherenceMusicalRule.class).intervalComplies(newAtom.getInfo(), nextTopAtom.getInfo()) &&
                            MusicalRule.getRule(VerticalCoherenceMusicalRule.class).intervalComplies(newAtom.getInfo(), bottomAtom.getInfo())) {
                        canApply = true;
                        break;
                    }


                    Atom previousBottomAtom = topAtoms.get(i - 1).getVerticalLink();
                    Atom nextBottomAtom = topAtoms.get(i + 1).getVerticalLink();
                    if (previousBottomAtom != null && nextBottomAtom != null &&
                            MusicalRule.getRule(HorizontalCoherenceMusicalRule.class).intervalComplies(previousBottomAtom.getInfo(), newAtom.getInfo()) &&
                            MusicalRule.getRule(HorizontalCoherenceMusicalRule.class).intervalComplies(newAtom.getInfo(), nextBottomAtom.getInfo()) &&
                            MusicalRule.getRule(VerticalCoherenceMusicalRule.class).intervalComplies(topAtom.getInfo(), newAtom.getInfo())) {
                        canApply = true;
                        break;
                    }
                }
            } 
        }
        
        return canApply;
    }

    @Override
    public Matter apply(Matter base, Matter addition, Molecule.MoleculeLayer targetLayer) {
        Atom newAtom = (Atom) addition;
        Molecule baseMolecule = (Molecule) base;

        List<Atom> topAtoms = baseMolecule.getAtoms();

        for (int i = 1; i < topAtoms.size() - 1; i++) {
            Atom topAtom = topAtoms.get(i);
            Atom bottomAtom = topAtom.getVerticalLink();
            boolean didApply = false;
            
            
            
            switch (targetLayer) {
                case Top:
                    if (bottomAtom == null) {
                        continue;
                    }
                    
                    Atom previousTopAtom = topAtoms.get(i - 1);
                    Atom nextTopAtom = topAtoms.get(i + 1);
                    if (MusicalRule.getRule(HorizontalCoherenceMusicalRule.class).intervalComplies(previousTopAtom.getInfo(), newAtom.getInfo()) &&
                            MusicalRule.getRule(HorizontalCoherenceMusicalRule.class).intervalComplies(newAtom.getInfo(), nextTopAtom.getInfo()) &&
                            MusicalRule.getRule(VerticalCoherenceMusicalRule.class).intervalComplies(newAtom.getInfo(), bottomAtom.getInfo())) {
                        
                        //Set new horizontal links
                        previousTopAtom.setHorizontalLink(newAtom);
                        newAtom.setHorizontalLink(nextTopAtom);
                        
                        //Set new vertical links
                        newAtom.setVerticalLink(bottomAtom);
                        didApply = true;
                        
                        baseMolecule.getAtoms().remove(i);
                        baseMolecule.getAtoms().add(i, newAtom);
                    }
                    break;
                case Bottom:
                    Atom previousBottomAtom = topAtoms.get(i - 1).getVerticalLink();
                    Atom nextBottomAtom = topAtoms.get(i + 1).getVerticalLink();
                    if (previousBottomAtom != null && nextBottomAtom != null) {
                        if (MusicalRule.getRule(HorizontalCoherenceMusicalRule.class).intervalComplies(previousBottomAtom.getInfo(), newAtom.getInfo()) &&
                            MusicalRule.getRule(HorizontalCoherenceMusicalRule.class).intervalComplies(newAtom.getInfo(), nextBottomAtom.getInfo()) &&
                            MusicalRule.getRule(VerticalCoherenceMusicalRule.class).intervalComplies(topAtom.getInfo(), newAtom.getInfo())) {
                            //Set new horizontal links
                           previousBottomAtom.setHorizontalLink(newAtom);
                           newAtom.setHorizontalLink(nextBottomAtom);

                           //Set new vertical links
                           topAtom.setVerticalLink(newAtom);
                           didApply = true;
                        }
                    } else {
                        if (MusicalRule.getRule(VerticalCoherenceMusicalRule.class).intervalComplies(topAtom.getInfo(), newAtom.getInfo())) {
                            //Set new vertical links
                           topAtom.setVerticalLink(newAtom);
                           didApply = true;
                        }
                    }

                    break;
            }
            
            if (didApply) {
                break;
            }
        }
        return base;
    }
    
}
