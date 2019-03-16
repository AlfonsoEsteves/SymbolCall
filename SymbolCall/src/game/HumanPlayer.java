package game;

import java.util.LinkedList;

import battle.Card;
import battle.Rnd;
import loader.CardLoader;

public class HumanPlayer extends Player {
	
	public LinkedList<Card> inventory;
	
	public HumanPlayer() {
	}
	
	public HumanPlayer(String name) {
		super(name, true);
		inventory = new LinkedList<>();
		for (int i = 0; i < 5; i++) {
			Card card = CardLoader.playerCards.get(Rnd.nextInt(CardLoader.playerCards.size()));
			inventory.add(card);
		}
	}
	
	@Override
	public void addWin() {
		super.addWin();
		Game.ins.gold += 10;
	}
	
	@Override
	public void addDefeat() {
		super.addDefeat();
		Game.ins.gold += 3;
	}
}
