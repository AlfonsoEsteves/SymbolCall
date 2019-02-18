package battle;

import java.util.LinkedList;

public class BPlayer {
	
	public String name;
	public LinkedList<Card> deck;
	public ComputerAI computerAI;
	
	public BPlayer() {
		deck=new LinkedList<>();
	}
	
}
