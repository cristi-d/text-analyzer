package textanalyzer.sonification.music;

public enum Note {
	C, D, E, F, G, A, B, Rest;
	
	@Override
	public String toString() {
		if (this.equals(Rest)) {
			return "-";
		} else {
			return super.toString();
		}
	}
}
