package battle;

import java.util.LinkedList;

public class Player {
	
	public String name;
	public LinkedList<Card> deck;
	public ComputerAI computerAI;
	
	public Player() {
		deck=new LinkedList<>();
	}
	
}
