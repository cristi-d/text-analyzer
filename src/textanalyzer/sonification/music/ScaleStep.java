package textanalyzer.sonification.music;

/**
 * For use in a later version: musical rules based on tonality theory
 * @author Cristi
 *
 */
public enum ScaleStep {
	First, Second, Third, Fourth, Fifth, Sixth, Seventh, NONE;
	
	public static boolean isStable(ScaleStep step) {
		
		switch (step) {
		case Fifth:
			case First:
				case Fourth:
					case Third:
			return true;
		default:
			return false;
		}
	}
}
