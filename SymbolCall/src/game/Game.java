package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import battle.Battle;
import battle.Card;
import battle.Rnd;
import bruteForceAI.BruteForceAI;
import loader.BossLoader;
import loader.CardLoader;
import loader.DeckLoader;
import loader.ImageLoader;

public class Game {

	public static Player humanPlayer;
	public static List<Player> players;
	
	public static Battle battle;

	public static void initialize() {
		CardLoader.loadCards();
		ImageLoader.loadImages();
		BossLoader.loadBosses();
		DeckLoader.loadDecks();

		players = new ArrayList<>();

		for (int i = 0; i < 49; i++) {
			Player player = createRandomPlayer();
			players.add(player);
		}
		
		humanPlayer = new Player("human_player", null);
		players.add(humanPlayer);
	}
	
	public static void initializeHumanPlayerBattle() {
		Player rival = null;
		for (int i = 0; i < players.size(); i += 2) {
			Player p1 = players.get(i);
			Player p2 = players.get(i + 1);
			if(p1.isHumanPlayer()) {
				rival = p2;
			}
			if(p2.isHumanPlayer()) {
				rival = p1;
			}
		}
		battle = new Battle(humanPlayer, rival, Rnd.nextInt(2));
	}

	public static void executeRound(Battle playedBattle) {
		for (int i = 0; i < players.size(); i += 2) {
			Player p1 = players.get(i);
			Player p2 = players.get(i + 1);
			Battle battle;
			if(p1.isHumanPlayer() || p2.isHumanPlayer()) {
				battle = playedBattle;
			}
			else {
				battle = BattleExecutor.executeBattle(p1, p2);
			}
			if (battle.winner() == 0) {
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
		Player player = new Player("player_"+ Rnd.nextInt(1000), BruteForceAI.getInstance());
		for(int i=0;i<Battle.deckSize;i++) {
			Card card = CardLoader.playerCards.get(Rnd.nextInt(CardLoader.playerCards.size()));
			player.deck.add(card);
		}
		return player;
	}
}
