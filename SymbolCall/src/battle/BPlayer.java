package battle;

import java.util.LinkedList;

import bruteForceAI.BruteForceAI;

public class BPlayer {
	
	public String name;
	public ComputerAI computerAI;
	public LinkedList<Card> deck;
	
	public BPlayer(String name, BruteForceAI computerAI) {
		this.name = name;
		this.computerAI = computerAI;
		deck=new LinkedList<>();
	}

	public boolean isHumanPlayer() {
		return computerAI == null;
	}
	
}
