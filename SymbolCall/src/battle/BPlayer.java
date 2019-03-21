package battle;

import java.io.Serializable;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class BPlayer implements Serializable{

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
