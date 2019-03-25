package battle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bruteForceAI.BruteForceAI;

@SuppressWarnings("serial")
public class BPlayer implements Serializable{

	public String name;
	public List<Card> deck;
	
	public BPlayer() {}

	public BPlayer(String name) {
		this.name = name;
		deck = new ArrayList<>();
	}

	public boolean isHuman() {
		return false;
	}
	
	// Automated testing can make a single player have multiple battles at the same time.
	// So it will need a new instance of a ComputerAI for each battle
	public ComputerAI instantiateComputerAI() {
		return new BruteForceAI();
	}

}
