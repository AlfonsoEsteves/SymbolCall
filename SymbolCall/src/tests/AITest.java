package tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import battle.Battle;
import battle.Player;
import zAI.ZAI.ZAIFactory;
import bruteForceAI.BruteForceAI.BruteForceAIFactory;
import game.BattleExecutor;
import game.BattleExecutorAutomatic;
import game.Game;
import game.ThreadManager;
import loader.CardLoader;
import loader.DeckLoader;
import randomAI.RandomAI.RandomAIFactory;

public class AITest {

	public static final int repetitions = 1;

	public static LinkedList<TestedPlayer> brutePlayers;
	public static LinkedList<TestedPlayer> randomPlayers;

	public static void main(String[] args) {
		Game.instantiate();
		
		CardLoader.loadCards();
		DeckLoader.loadDecks();
		
		brutePlayers = new LinkedList<>();
		for (String deckName : DeckLoader.decks.keySet()) {
			TestedPlayer brutePlayer = new TestedPlayer(new ZAIFactory(), deckName, 0);
			brutePlayers.add(brutePlayer);
		}
		
		randomPlayers = new LinkedList<>();
		for (String deckName : DeckLoader.decks.keySet()) {
			TestedPlayer randomPlayer = new TestedPlayer(new BruteForceAIFactory(), deckName, 0);
			randomPlayers.add(randomPlayer);
		}

		/*randomPlayers = new LinkedList<>();
		for (String deckName : DeckLoader.decks.keySet()) {
			TestedPlayer randomPlayer = new TestedPlayer(new RandomAIFactory(), deckName, Game.instance.rnd.nextInt());
			randomPlayers.add(randomPlayer);
		}*/

		List<Player> players1 = new ArrayList<>();
		List<Player> players2 = new ArrayList<>();

		for (int i = 0; i < repetitions; i++) {
			for (TestedPlayer testPlayer1 : brutePlayers) {
				for (TestedPlayer testPlayer2 : randomPlayers) {
					players1.add(testPlayer1);
					players2.add(testPlayer2);
				}
			}
		}

		int size = players1.size();
		int[] startingPlayers = IntStream.generate(() -> 0).limit(size).toArray();
		int[] rndSeeds = IntStream.generate(() -> Game.instance.rnd.nextInt()).limit(size).toArray();
		BattleExecutor[] battleExecutors = Collections.nCopies(size, BattleExecutorAutomatic.instance).toArray(new BattleExecutor[size]);		
		long ini = System.nanoTime();
		List<Battle> battles = ThreadManager.instance.executeBattles(players1, players2, startingPlayers, rndSeeds, battleExecutors);
		long end = System.nanoTime();
		System.out.println("Time: " + ((end - ini) / 1000000000));
		
		for(Battle battle : battles) {
			processBattle(battle);
		}

		int wonByBrute = 0;
		for (TestedPlayer testPlayer1 : brutePlayers) {
			wonByBrute += testPlayer1.wonGames();
		}

		int wonByRandom = 0;
		for (TestedPlayer testPlayer1 : randomPlayers) {
			wonByRandom += testPlayer1.wonGames();
		}

		System.out.println("Won by brute: " + wonByBrute + "   " + 100 * wonByBrute / (wonByBrute + wonByRandom) + "%");
		System.out.println(
				"Won by random: " + wonByRandom + "   " + 100 * wonByRandom / (wonByBrute + wonByRandom) + "%");
	}
	
	public static void processBattle(Battle battle) {
		TestedPlayer player0 = (TestedPlayer) battle.players[0];
		TestedPlayer player1 = (TestedPlayer) battle.players[1];
		
		if (battle.winner() == 0) {
			player0.wonGamesAsFirst++;
		} else {
			player1.wonGamesAsSecond++;
		}
		player0.totalGamesAsFirst++;
		player1.totalGamesAsSecond++;
	}

	static String round(double x) {
		return (int) x + "." + (int) (x * 10) % 10;
	}
}
