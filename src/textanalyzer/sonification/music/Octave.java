package textanalyzer.sonification.music;

public enum Octave {
	Second, Third, Fourth, Fifth, Sixth;
	
	public static Octave next(Octave oct) {
		if (oct.ordinal() < values().length - 1) {
			return values()[oct.ordinal() + 1];
		} else {
			return oct;
		}
	}
	
	public static Octave previous(Octave oct) {
		if (oct.ordinal() > 0) {
			return values()[oct.ordinal() - 1];
		} else {
			return oct;
		}
 	}
	
	
	
	public int getValue() {
		return 2 + ordinal();
	}
	
	@Override
	public String toString() {
		return String.valueOf(2 + ordinal());
	}
	
	
}
