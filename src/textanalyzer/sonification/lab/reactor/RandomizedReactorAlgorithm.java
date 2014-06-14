/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package textanalyzer.sonification.lab.reactor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import textanalyzer.sonification.exceptions.lab.reactor.NoReactionException;
import textanalyzer.sonification.lab.reactor.models.Atom;
import textanalyzer.sonification.lab.reactor.models.Matter;
import textanalyzer.sonification.lab.reactor.models.Molecule;

/**
 *
 * @author cristiand
 */
public abstract class RandomizedReactorAlgorithm extends ReactorAlgorithm {
    public final int TIMEOUT = 5000;
    @Override
    protected void initialize() {
            instance = this;
            System.out.println("RandomizedReactorAlgorithm instance is " + this.getClass().getName());
    }

    public Collection<Matter> mixMatter(Collection<Matter> sample) throws NoReactionException {
		Collection<Matter> result = new ArrayList<Matter>();
		List<Matter> indexedSample = new ArrayList<Matter>(sample);
		
		Random random = new Random();
	
		
		
		for (int round = 0; round < TIMEOUT; round++) {
			if (indexedSample.size() < 2) {
				break;
			}
			
			int hit1 = random.nextInt(indexedSample.size());
			int hit2 = hit1;
			while (hit1 == hit2) {
				hit2 = random.nextInt(indexedSample.size());
			}
			
			Matter m1 = indexedSample.get(hit1);
			Matter m2 = indexedSample.get(hit2);
			
			if (hit1 > hit2) {
				indexedSample.remove(hit1);
				indexedSample.remove(hit2);
			} else {
				indexedSample.remove(hit2);
				indexedSample.remove(hit1);
			}
			
			
			if (m1 instanceof Atom && m2 instanceof Atom) {
				if (isAtomToAtomCollisionPossible(m1, m2, null)) {
					Molecule.MoleculeLayer layer = Molecule.MoleculeLayer.values()[random.nextInt(Molecule.MoleculeLayer.values().length)];
					
					result.add(atomToAtomCollision(m1, m2, layer));
				}
				continue;
			}
			
			if (m1 instanceof Atom && m2 instanceof Molecule || 
					m1 instanceof Molecule && m2 instanceof Atom) {
				Matter base, addition;
				
				if (m1 instanceof Molecule) {
					base = m1;
					addition = m2;
				} else {
					base = m2;
					addition = m1;
				}
				
				if (isAtomToMoleculeCollisionPossible(base, addition, null)) {
					Molecule.MoleculeLayer layer = Molecule.MoleculeLayer.values()[random.nextInt(Molecule.MoleculeLayer.values().length)];
					
					result.add(atomToMoleculeCollision(base, addition, layer));
				}
				continue;
			}
			
			if (m1 instanceof Molecule && m2 instanceof Molecule) {
				if (isMoleculeToMoleculeCollisionPossible(m1, m2, null)) {
					result.add(moleculeToMoleculeCollision(m1, m2, null));
				}
				continue;
			}
		}
		
		return result;
	}
    
    protected abstract boolean isAtomToAtomCollisionPossible(Matter base, Matter addition, Molecule.MoleculeLayer layer);
    
    protected abstract boolean isAtomToMoleculeCollisionPossible(Matter base, Matter addition, Molecule.MoleculeLayer layer);
    
    protected abstract boolean isMoleculeToMoleculeCollisionPossible(Matter base, Matter addition, Molecule.MoleculeLayer layer);
    
    protected abstract Matter atomToAtomCollision(Matter base, Matter addition, Molecule.MoleculeLayer layer);
    
    protected abstract Matter atomToMoleculeCollision(Matter base, Matter addition, Molecule.MoleculeLayer layer);    
    
    protected abstract Matter moleculeToMoleculeCollision(Matter base, Matter addition, Molecule.MoleculeLayer layer);    
}
