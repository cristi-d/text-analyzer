package textanalyzer.sonification.lab.rules;

import java.util.ArrayList;
import java.util.List;
import textanalyzer.sonification.lab.reactor.models.Matter;


public final class CollisionRuleMatcher {
	private CollisionRuleMatcher() {
		;
	}
	
	//TODO: rewrite
	public static List<CollisionRule> matchRules(Matter base, Matter addition, CollisionRule... orderedRules) {
		List<CollisionRule> matchingRules = new ArrayList<CollisionRule>();
		
		for (CollisionRule rule : orderedRules) {
			if (rule.canApply(base, addition, null)) {
				matchingRules.add(rule);
			}
		}
		
		return matchingRules;
	}
}
