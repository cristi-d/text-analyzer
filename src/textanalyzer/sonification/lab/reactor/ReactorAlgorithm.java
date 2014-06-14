package textanalyzer.sonification.lab.reactor;

import java.util.Collection;
import textanalyzer.sonification.exceptions.lab.reactor.NoReactionException;
import textanalyzer.sonification.lab.reactor.models.Atom;
import textanalyzer.sonification.lab.reactor.models.Matter;
import textanalyzer.sonification.lab.reactor.models.Molecule;

public abstract class ReactorAlgorithm {
	protected static ReactorAlgorithm instance;
	
	protected ReactorAlgorithm() {
		initialize();
	}
	
	/**
	 * 
	 */
	protected abstract void initialize();
	
	public static ReactorAlgorithm getInstance() {
		return instance;
	}
	
	/**
	 * The actual reactor algorithm. Must be overridden!
	 * @param sample The {@link Matter} sample: {@link Atom}s and {@link Molecule}s.
	 * @return The resulting {@link Matter}, as a {@link Collection}
	 * @throws NoReactionException If no reaction is possible
	 */
	public abstract Collection<Matter> mixMatter(Collection<Matter> sample) throws NoReactionException;
	
}
