package textanalyzer.sonification.lab.tools;

import java.util.HashMap;
import java.util.Map;
import textanalyzer.sonification.music.BasicInterval;
import textanalyzer.sonification.music.PitchedSound;

public final class TapeMeasure {
	private final static Map<String, Integer> noteOrdering;
	private static String stepsSharp[] = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
	private static String stepsFlat[] =  {"C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B"};
	
	static {
		noteOrdering = new HashMap<String, Integer>();
		
		
		for (int i = 0; i < 11; i++) {
			noteOrdering.put(stepsSharp[i], i);
			if (stepsFlat[i].equals(stepsSharp[i]) == false) {
				noteOrdering.put(stepsFlat[i], i);
			}
			
		}
	}
	
	private TapeMeasure() { 
		;
	}
	
	public static BasicInterval measureBasicInterval(PitchedSound from, PitchedSound to) {
		
		if (from.isRest() || to.isRest()) {
			return BasicInterval.NO_INTERVAL;
		}
		
		if (from.compareTo(to) <= 0) {
			return measureAscendingBasicInterval(from, to);
		} else {
			return measureAscendingBasicInterval(to, from);
		}
	}
	
	public static BasicInterval measureAscendingBasicInterval(PitchedSound from, PitchedSound to) {
		int fromOctave = from.getOctave().getValue();
		int toOctave = to.getOctave().getValue();
		int fromSemitones = -1;
		int toSemitones = -1;
		String fromNoteString = from.getNoteAndIntonationString();
		String toNoteString = to.getNoteAndIntonationString();
		
		String fromScale[], toScale[];
		
		if (fromNoteString.endsWith("b")) {
			fromScale = stepsFlat;
		} else {
			fromScale = stepsSharp;
		}
		
		if (toNoteString.endsWith("b")) {
			toScale = stepsFlat;
		} else {
			toScale = stepsSharp;
		}
		
		for (int i = 0; i <= 11 && (fromSemitones == -1 || toSemitones == -1); i++) {
			
			if (fromSemitones == -1) {
				if (fromScale[i].equals(fromNoteString)) {
					fromSemitones = i;
				}
			}
			
			if (toSemitones == -1) {
				if (toScale[i].equals(toNoteString)) {
					toSemitones = i;
				}
			}
		}
		
		int intervalWidth;
		intervalWidth = (toOctave - fromOctave) * 12 - fromSemitones + toSemitones;
		
		if (intervalWidth < 0) {
			System.out.println("ERROR: trying to measure interval " + from.toMusicString() + " -> " + to.toMusicString());
		}
		
		if (intervalWidth <= 12) {
			return BasicInterval.getInterval(intervalWidth);
		} else {
			return BasicInterval.UNRECOGNIZED;
		}
		
		
	}
	
}
