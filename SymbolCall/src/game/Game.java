package game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import battle.BPlayer;
import battle.Battle;
import battle.Card;
import battle.Rnd;
import loader.BossLoader;
import loader.CardLoader;
import loader.DeckLoader;
import loader.ImageLoader;

public class Game {
	
	public static Game ins = new Game();
	
	public static final int initialGold = 15;
	public static final int cardCost = 12;

	public static final int cumputerPlayers = 49;

	public HumanPlayer humanPlayer;
	
	public LinkedList<BPlayer> bosses;
	
	public LinkedList<Card> playerCards;
	
	public LinkedList<Card> computerCards;
	
	public List<Player> players;

	public int gold;
	
	public Card availableToBuy;

	public transient Battle battle;
	
	public Game() {}

	public void initialize() {
		CardLoader.loadCards();
		ImageLoader.loadImages();
		BossLoader.loadBosses();
		DeckLoader.loadDecks();

		players = new ArrayList<>();

		for (int i = 0; i < cumputerPlayers; i++) {
			Player player = new Player("player_" + Rnd.nextInt(1000), false);
			player.deck = createRandomDeck();
			players.add(player);
		}

		humanPlayer = new HumanPlayer("human_player");
		humanPlayer.deck = createRandomDeck();
		players.add(humanPlayer);
		
		gold = initialGold;
		availableToBuy = Game.ins.playerCards.get(Rnd.nextInt(Game.ins.playerCards.size()));
	}

	private LinkedList<Card> createRandomDeck() {
		LinkedList<Card> deck = new LinkedList<>();
		for (int i = 0; i < Battle.deckSize; i++) {
			Card card = Game.ins.playerCards.get(Rnd.nextInt(Game.ins.playerCards.size()));
			deck.add(card);
		}
		return deck;
	}
	
	public void updateAvailableToBuy() {
		availableToBuy = Game.ins.playerCards.get(Rnd.nextInt(Game.ins.playerCards.size()));
	}
	
	public void buyAvailableToBuy() {
		if(gold > cardCost) {
			gold -= cardCost;
			Game.ins.humanPlayer.inventory.add(availableToBuy);
			availableToBuy = Game.ins.playerCards.get(Rnd.nextInt(Game.ins.playerCards.size()));
		}
	}
}
