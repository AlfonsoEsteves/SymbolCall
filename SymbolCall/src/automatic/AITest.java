package automatic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import battle.BPlayer;
import battle.Battle;
import battle.ComputerAI;
import battle.Rnd;
import bruteForceAI.BruteForceAI;
import game.Game;
import game.ThreadManager;
import loader.CardLoader;
import loader.DeckLoader;
import randomAI.RandomAI;

@SuppressWarnings("serial")
public class AITest {

	public static final int repetitions = 3;

	public static LinkedList<TestedPlayer> brutePlayers;
	public static LinkedList<TestedPlayer> randomPlayers;

	public static void main(String[] args) {
		Game.ins.rnd = new Rnd();
		
		CardLoader.loadCards();
		DeckLoader.loadDecks();
		
		brutePlayers = new LinkedList<>();
		for (BPlayer player : DeckLoader.decks) {
			TestedPlayer brutePlayer = new TestedPlayer("BruteForceAI-" + player.name, player.deck) {
				@Override
				public ComputerAI instantiateComputerAI(Random rnd) {
					return new BruteForceAI();
				}
			};
			brutePlayers.add(brutePlayer);
		}

		randomPlayers = new LinkedList<>();
		for (BPlayer player : DeckLoader.decks) {
			TestedPlayer randomPlayer = new TestedPlayer("RandomAI-" + player.name, player.deck) {
				@Override
				public ComputerAI instantiateComputerAI(Random rnd) {
					return new RandomAI(rnd);
				}
			};
			randomPlayers.add(randomPlayer);
		}

		List<BPlayer> players1 = new ArrayList<>();
		List<BPlayer> players2 = new ArrayList<>();

		for (int i = 0; i < repetitions; i++) {
			for (TestedPlayer testPlayer1 : brutePlayers) {
				for (TestedPlayer testPlayer2 : randomPlayers) {
					players1.add(testPlayer1);
					players2.add(testPlayer2);
				}
			}
		}

		int[] startingPlayers = IntStream.generate(() -> 0).limit(players1.size()).toArray();
		long ini = System.nanoTime();
		List<Battle> battles = ThreadManager.ins.executeBattles(players1, players2, startingPlayers);
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
