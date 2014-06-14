package textanalyzer.sonification.music;

public enum Intonation {
	Flat, Natural, Sharp;
	
	@Override 
	public String toString() {
		switch (this) {
		case Flat:
			return "b";
		case Natural:
			return "";
		case Sharp:
			return "#";
		default:
			return "";
		}
	}
	
}
