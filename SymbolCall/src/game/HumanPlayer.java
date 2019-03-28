package game;

import java.util.LinkedList;
import java.util.Random;

import battle.Card;
import battle.ComputerAI;

@SuppressWarnings("serial")
public class HumanPlayer extends LeaguePlayer {
	
	public LinkedList<Card> inventory;
	
	public HumanPlayer() {
	}
	
	public HumanPlayer(int seed) {
		super(null, seed);
		// I use seed + 1 to avoid selecting the same cards for the deck and for the inventory
		Random rnd = new Random(seed + 1);
		inventory = new LinkedList<>();
		for (int i = 0; i < 5; i++) {
			Card card = Game.instance.playerCards.get(rnd.nextInt(Game.instance.playerCards.size()));
			inventory.add(card);
		}
	}

	@Override
	public void addWin() {
		super.addWin();
		Game.instance.gold += 10;
	}
	
	@Override
	public void addDefeat() {
		super.addDefeat();
		Game.instance.gold += 3;
	}
	
}
