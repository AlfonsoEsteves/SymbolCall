package game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import battle.Battle;
import battle.Card;
import battle.Rnd;
import loader.BossLoader;
import loader.CardLoader;
import loader.DeckLoader;
import loader.ImageLoader;

public class Game {

	public static final int cumputerPlayers = 49;

	public static Player humanPlayer;
	public static List<Player> players;

	public static Battle battle;

	public static void initialize() {
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

		humanPlayer = new Player("human_player", true);
		humanPlayer.deck = createRandomDeck();
		players.add(humanPlayer);
	}

	private static LinkedList<Card> createRandomDeck() {
		LinkedList<Card> deck = new LinkedList<>();
		for (int i = 0; i < Battle.deckSize; i++) {
			Card card = CardLoader.playerCards.get(Rnd.nextInt(CardLoader.playerCards.size()));
			deck.add(card);
		}
		return deck;
	}
}
