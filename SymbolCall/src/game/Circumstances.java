package game;

import battle.Card;
import battle.Rnd;
import loader.CardLoader;

public class Circumstances {
	
	public static final int cardCost = 27;
	
	public static Circumstances instance = new Circumstances();

	public int gold;
	
	public Card availableToBuy;
	
	public Circumstances() {
		gold = 0;
		availableToBuy = CardLoader.playerCards.get(Rnd.nextInt(CardLoader.playerCards.size())); 
	}
	
	public void updateAvailableToBuy() {
		availableToBuy = CardLoader.playerCards.get(Rnd.nextInt(CardLoader.playerCards.size()));
	}
	
	public void buyAvailableToBuy() {
		if(gold > cardCost) {
			gold -= cardCost;
			Game.humanPlayer.inventory.add(availableToBuy);
			availableToBuy = CardLoader.playerCards.get(Rnd.nextInt(CardLoader.playerCards.size()));
		}
	}
}
