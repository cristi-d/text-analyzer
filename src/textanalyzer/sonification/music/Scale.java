package textanalyzer.sonification.music;

/**
 * For use in a later version: other scales than C made available 
 * @author Cristi
 *
 */
public enum Scale {
	C, UNDEFINED;
	
	public static ScaleStep getScaleStep(Scale scale, PitchedSound sound) {
		switch (scale) {
		case C:
			switch (sound.getNote()) {
			case A:
				return ScaleStep.Sixth;
			case B:
				return ScaleStep.Seventh;
			case C:
				return ScaleStep.First;
			case D:
				return ScaleStep.Second;
			case E:
				return ScaleStep.Third;
			case F:
				return ScaleStep.Fourth;
			case G:
				return ScaleStep.Fifth;
			default:
				return ScaleStep.NONE;
			}
		default:
			return ScaleStep.NONE;
		}
	}
}
