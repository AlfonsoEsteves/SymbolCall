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
	
	public HumanPlayer(String name) {
		super(name);
		inventory = new LinkedList<>();
		for (int i = 0; i < 5; i++) {
			Card card = Game.ins.playerCards.get(Game.ins.rnd.nextInt(Game.ins.playerCards.size()));
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

	@Override
	public boolean isHuman() {
		return true;
	}

	@Override
	public ComputerAI instantiateComputerAI(Random rnd) {
		return null;
	}
	
}
