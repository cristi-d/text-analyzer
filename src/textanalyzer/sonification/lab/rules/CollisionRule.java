package textanalyzer.sonification.lab.rules;

import java.util.HashMap;
import java.util.Map;
import textanalyzer.sonification.lab.reactor.models.Matter;
import textanalyzer.sonification.lab.reactor.models.Molecule.MoleculeLayer;

public abstract class CollisionRule {
	private static Map<Class<? extends CollisionRule>, CollisionRule> instances;
	
	protected CollisionRule() {
		;
	}
	
	static {
		instances = new HashMap<Class<? extends CollisionRule>, CollisionRule>();
	}
	
	public static CollisionRule getRule(Class<? extends CollisionRule> ruleClass) {
		if (instances.get(ruleClass) != null) {
			return instances.get(ruleClass);
		} else {
			try {
				CollisionRule instance = ruleClass.getDeclaredConstructor((Class<?>[]) null).newInstance((Object[])null);
				instances.put(ruleClass, instance);
				
				return instance;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	public abstract boolean canApply(Matter base, Matter addition, MoleculeLayer targetLayer);
	public abstract Matter apply(Matter base, Matter addition, MoleculeLayer targetLayer);
}
