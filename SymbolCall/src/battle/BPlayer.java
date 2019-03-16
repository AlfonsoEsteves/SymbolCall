package battle;

import java.util.LinkedList;

public class BPlayer {

	public String name;
	public boolean human;
	public LinkedList<Card> deck;
	
	public BPlayer() {}

	public BPlayer(String name, boolean human) {
		this.name = name;
		this.human = human;
		deck = new LinkedList<>();
	}

	public boolean isHuman() {
		return human;
	}

}
