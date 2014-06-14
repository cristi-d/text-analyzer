package textanalyzer.sonification.lab.reactor.models;

import textanalyzer.sonification.music.PitchedSound;

public class Atom implements Matter {
	private PitchedSound info;
	private Atom verticalLink, horizontalLink;
	
        public Atom(Atom copyThis) {
            this.info = PitchedSound.create(copyThis.getInfo().getNote(), copyThis.getInfo().getOctave(), copyThis.getInfo().getIntonation());
            this.verticalLink = copyThis.getVerticalLink();
            
            
            if (this.verticalLink != null) {
                this.verticalLink = new Atom(this.verticalLink);
            }
        }
        
	public Atom(PitchedSound info) {
		this.info = info;
	}
	
	public PitchedSound getInfo() {
		return info;
	}
	
	public Atom getVerticalLink() {
		return verticalLink;
	}

	public void setVerticalLink(Atom verticalLink) {
		this.verticalLink = verticalLink;
	}

	public Atom getHorizontalLink() {
		return horizontalLink;
	}

	public void setHorizontalLink(Atom horizontalLink) {
		this.horizontalLink = horizontalLink;
	}

	@Override
	public String toString() {
		return info.toMusicString();
	}
		
	@Override
	public boolean equals(Object o) {
		if (o instanceof Atom == false) {
			return false;
		}
		
		Atom a = (Atom) o;
		return a.getInfo().equals(info);
	}
}
