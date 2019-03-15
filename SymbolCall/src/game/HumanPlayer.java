package game;

import java.util.LinkedList;

import battle.Battle;
import battle.Card;
import battle.Rnd;
import loader.CardLoader;

public class HumanPlayer extends Player {
	
	public LinkedList<Card> inventory;
	
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
		Circumstances.instance.gold += 10;
	}
}
