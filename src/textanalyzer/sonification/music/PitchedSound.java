package textanalyzer.sonification.music;

public class PitchedSound implements Comparable<PitchedSound> {
	private Note note;
	private Octave octave;
	private Intonation intonation;
	private String musicString;
	private String noteAndIntonationString;
	
	private PitchedSound(Note note, Octave octave, Intonation intonation) {
		this.note = note;
		this.octave = octave;
		this.intonation = intonation;

		if (note.equals(Note.Rest) == false) {
			StringBuilder strBld = new StringBuilder();
			strBld.append(note).append(intonation).append(octave);

			musicString = strBld.toString();
		} else {
			musicString = "R";
		}
		
		StringBuilder strBld;
		strBld = new StringBuilder();
		strBld.append(note).append(intonation);
		
		noteAndIntonationString = strBld.toString();
	}
	
	public static PitchedSound create(Note note, Octave oct, Intonation into) {
		return new PitchedSound(note, oct, into);
	}
	
	public static PitchedSound create(Note note, Octave oct) {
		return new PitchedSound(note, oct, Intonation.Natural);
	}
	
	public static PitchedSound createRest() {
		return new PitchedSound(Note.Rest, null, null);
	}
	
	public Note getNote() {
		return note;
	}
	
	public String getNoteAndIntonationString() {
		return noteAndIntonationString;
	}
	
	public Octave getOctave() {
		return octave;
	}
	
	public Intonation getIntonation() {
		return intonation;
	}
	
	public String toMusicString() {
		return musicString;
	}
	
	public boolean isRest() {
		return note.equals(Note.Rest);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof PitchedSound == false) {
			return false;
		}
		
		PitchedSound ps = (PitchedSound) o;
		return note.equals(ps.getNote()) && intonation.equals(ps.getIntonation()) && octave.equals(ps.getOctave());
	}

	@Override
	public int compareTo(PitchedSound o) {
		
		if (this.getOctave().compareTo(o.getOctave()) != 0) {
			return this.getOctave().compareTo(o.getOctave());
		}
		
		
		if (this.getNote().compareTo(o.getNote()) != 0) {
			return this.getNote().compareTo(o.getNote());
		}
		
		return this.getIntonation().compareTo(o.getIntonation());

	}
}
