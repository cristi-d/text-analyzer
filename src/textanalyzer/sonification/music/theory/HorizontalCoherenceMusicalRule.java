package textanalyzer.sonification.music.theory;

import java.util.List;
import textanalyzer.sonification.music.MusicalPhrase;
import textanalyzer.sonification.music.PitchedSound;

public class HorizontalCoherenceMusicalRule extends MusicalRule {

	
	@Override
	public boolean complies(MusicalPhrase startPhrase, MusicalPhrase addition) {
		List<PitchedSound> phraseTopVoice = startPhrase.getVoice(0);
		List<PitchedSound> phraseBottomVoice = startPhrase.getVoice(1);
		List<PitchedSound> additionTopVoice = addition.getVoice(0);
		List<PitchedSound> additionBottomVoice = addition.getVoice(1);
		
		PitchedSound phraseTopVoiceLast = phraseTopVoice.get(phraseTopVoice.size() - 1);
		PitchedSound phraseBottomVoiceLast = phraseBottomVoice.get(phraseBottomVoice.size() - 1);
		
		PitchedSound additionTopVoiceFirst = additionTopVoice.get(0);
		PitchedSound additionBottomVoiceFirst = additionBottomVoice.get(0);
		
		boolean isTopBindingCoherent, isBottomBindingCoherent;
		
		isTopBindingCoherent = intervalComplies(phraseTopVoiceLast, additionTopVoiceFirst);
		isBottomBindingCoherent = intervalComplies(phraseBottomVoiceLast, additionBottomVoiceFirst);
		
		return isTopBindingCoherent || isBottomBindingCoherent;
	}

}
