package game;

import java.util.ArrayList;
import java.util.Collections;
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

	public static List<Player> players;

	public static void initialize() {
		CardLoader.loadCards();
		ImageLoader.loadImages();
		BossLoader.loadBosses();
		DeckLoader.loadDecks();

		players = new ArrayList<>();

		for (int i = 0; i < 50; i++) {
			Player player = createRandomPlayer();
			players.add(player);
		}
	}

	public static void newRound() {
		for (int i = 0; i < players.size(); i += 2) {
			Player p1 = players.get(i);
			Player p2 = players.get(i + 1);
			BPlayer[] battlingPlayers = { p1.player, p2.player };
			int winner = BattleExecutor.executeBattle(battlingPlayers);
			if (winner == 0) {
				p1.addWin();
				p2.addDefeat();
			} else {
				p2.addWin();
				p1.addDefeat();
			}
		}
		Collections.sort(players);
	}
	
	private static Player createRandomPlayer() {
		BPlayer bPlayer = new BPlayer();
		bPlayer.name = "player_"+ Rnd.nextInt(1000);
		for(int i=0;i<Battle.deckSize;i++) {
			Card card = CardLoader.playerCards.get(Rnd.nextInt(CardLoader.playerCards.size()));
			bPlayer.deck.add(card);
		}
		Player player = new Player();
		player.player = bPlayer;
		return player;
	}
}
