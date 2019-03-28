package battle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import battle.ComputerAI.ComputerAIFactory;
import game.Game;
import loader.DeckLoader;

@SuppressWarnings("serial")
public class Player implements Serializable{

	public String name;
	public List<Card> deck;
	public ComputerAIFactory computerAIFactory;
	
	public Player() {}
	
	public Player(ComputerAIFactory computerAIFactory, String deckName, int seed) {
		this.computerAIFactory = computerAIFactory;
		if(computerAIFactory == null) {
			name = "Human_";
		}
		else {
			name = "Computer_";
		}
		if(deckName != null) {
			name += deckName;
			deck = DeckLoader.decks.get(deckName);
		}
		else {
			name += seed;
			Random rnd = new Random(seed);
			deck = new LinkedList<>();
			for (int i = 0; i < Battle.deckSize; i++) {
				Card card = Game.instance.playerCards.get(rnd.nextInt(Game.instance.playerCards.size()));
				deck.add(card);
			}
		}
	}

	public Player(String name) {
		this.name = name;
		deck = new ArrayList<>();
	}

	public boolean isHuman() {
		return computerAIFactory == null;
	}

	public ComputerAI instantiateComputerAI(int player, int rndSeed) {
		if(computerAIFactory != null) {
			return computerAIFactory.create(player, rndSeed);
		}
		else {
			return null;
		}
	}

}
