package textanalyzer.sonification.music.theory;

import java.util.List;
import textanalyzer.sonification.lab.tools.Microscope;
import textanalyzer.sonification.music.BasicInterval;
import textanalyzer.sonification.music.MusicalPhrase;

public class HorizontalStabilityMusicalRule extends MusicalRule {

	@Override
	public boolean complies(MusicalPhrase phrase, MusicalPhrase addition) {
		List<BasicInterval> topVoiceIntervals = Microscope.detectHorizontalIntervals(phrase.getVoice(0));
		int totalLength = topVoiceIntervals.size();
		int totalSemitones = 0;
		
		for (BasicInterval interval : topVoiceIntervals) {
			totalSemitones += BasicInterval.getSemitones(interval);
		}
		
		if (addition != null) {
			topVoiceIntervals = Microscope.detectHorizontalIntervals(addition.getVoice(0));
			totalLength += topVoiceIntervals.size();
			
			for (BasicInterval interval : topVoiceIntervals) {
				totalSemitones += BasicInterval.getSemitones(interval);
			}
			
		}
		
		
		
		if (totalSemitones > BasicInterval.getSemitones(BasicInterval.FifthPerf) * totalLength) {
			return false;
		}
		
		
		return true;
	}

}
