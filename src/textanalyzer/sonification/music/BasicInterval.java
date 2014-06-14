package textanalyzer.sonification.music;

public enum BasicInterval {
	UnisonPerf, 
	SecondMin, 
	SecondMaj, 
	ThirdMin, 
	ThirdMaj, 
	FourthPerf, 
	FourthAug,
	FifthPerf,
	SixthMin, 
	SixthMaj,
	SeventhMin, 
	SeventhMaj,
	OctavePerf,
	UNRECOGNIZED,
	NO_INTERVAL;
	private static final int lastIntervalOrdinal = 12;
	
	public static BasicInterval getInterval(int semitones) {
		if (semitones <= lastIntervalOrdinal) {
			return values()[semitones];
		} else {
			return UNRECOGNIZED;
		}
	}
	
	public static int getSemitones(BasicInterval interval) {
		
		if (interval == null || interval.equals(UNRECOGNIZED) || interval.equals(NO_INTERVAL)) {
			return 0;
		}
		
		for (int i = 0; i < BasicInterval.values().length; i++) {
			if (BasicInterval.values()[i].equals(interval)) {
				return i;
			}
		}
		
		return 0;
	}
	
}