package battle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	// Note that some AIs need a random number generator
	public ComputerAI instantiateComputerAI(Random rnd) {
		return new BruteForceAI();
	}

}
