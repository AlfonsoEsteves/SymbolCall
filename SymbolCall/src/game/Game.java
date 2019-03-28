package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import battle.Player;
import battle.Battle;
import battle.Card;
import battle.Rnd;
import bruteForceAI.BruteForceAI;
import bruteForceAI.BruteForceAI.BruteForceAIFactory;
import loader.BossLoader;
import loader.CardLoader;
import loader.DeckLoader;
import loader.ImageLoader;

@SuppressWarnings("serial")
public class Game implements Serializable{
	
	public static Game instance;
	
	public static void instantiate() {
		instance = new Game();
		
		CardLoader.loadCards();
		ImageLoader.loadImages();
		BossLoader.loadBosses();
		DeckLoader.loadDecks();

		instance.rnd = new Rnd();
	}
	
	public static final int initialGold = 15;
	public static final int cardCost = 12;
	public static final int cumputerPlayers = 49;
	
	public Rnd rnd;

	public HumanPlayer humanPlayer;
	
	public LinkedList<Player> bosses;
	
	public LinkedList<Card> playerCards;
	
	public LinkedList<Card> computerCards;
	
	public List<LeaguePlayer> players;

	public int gold;
	
	public Card availableToBuy;

	public transient Battle battle;
	
	public void initializeCircumstances() {
		players = new ArrayList<>();

		for (int i = 0; i < cumputerPlayers; i++) {
			LeaguePlayer player = new LeaguePlayer(new BruteForceAIFactory(), rnd.nextInt());
			players.add(player);
		}

		humanPlayer = new HumanPlayer(rnd.nextInt(1000000));
		players.add(humanPlayer);
		
		gold = initialGold;
		availableToBuy = Game.instance.playerCards.get(rnd.nextInt(Game.instance.playerCards.size()));
	}
	
	public void updateAvailableToBuy() {
		availableToBuy = Game.instance.playerCards.get(rnd.nextInt(Game.instance.playerCards.size()));
	}
	
	public void buyAvailableToBuy() {
		if(gold > cardCost) {
			gold -= cardCost;
			Game.instance.humanPlayer.inventory.add(availableToBuy);
			availableToBuy = Game.instance.playerCards.get(rnd.nextInt(Game.instance.playerCards.size()));
		}
	}
}
