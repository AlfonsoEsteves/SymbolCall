package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

import battle.Battle;
import battle.Player;
import persistence.Persistence;

public class ThreadManager {

	// This does not count the AWT thread and ThreadManager thread itself
	public static final int numberOfThreads = 4;

	public static ThreadManager instance = new ThreadManager();

	private ExecutorService executorService;

	// Requested by the BattleExecutorManual
	// Provided by the UI
	// This semaphore is to make the BattleExecutorManual wait for the battle to be
	// finished in the UI.
	public Semaphore humanBattleHasFinished = new Semaphore(0);

	// Requested by the ThreadManager
	// Provided by UI
	// I need this semaphore because I should not execute the round directly from
	// the UI thread. Instead, I should lock the UI thread and start the round in a
	// different thread.
	// This is because this thread may have to execute a manual battle, and the UI
	// needs to be able to run the battle.
	public Semaphore roundCanBeStarted = new Semaphore(0);

	// Requested by the UI
	// Provided by the ThreadManager
	// This semaphore makes sure that the round has finished when the player
	// finishes his battle. So the management UI can be shown.
	public Semaphore roundHasFinished = new Semaphore(0);

	public ThreadManager() {
		executorService = Executors.newFixedThreadPool(numberOfThreads);
	}

	public void runGame() {
		while (true) {
			try {
				roundCanBeStarted.acquire();

				LinkedList<Player> players1 = new LinkedList<>();
				LinkedList<Player> players2 = new LinkedList<>();
				for (int i = 0; i < Game.instance.players.size() / 2; i++) {
					LeaguePlayer player1 = Game.instance.players.get(i * 2);
					LeaguePlayer player2 = Game.instance.players.get(i * 2 + 1);
					if (player1.isHuman()) {
						players1.addFirst(player1);
						players2.addFirst(player2);
					}
					else if (player2.isHuman()) {
						players1.addFirst(player2);
						players2.addFirst(player1);
					}
					else {
						players1.addLast(player1);
						players2.addLast(player2);
					}
				}

				int size = players1.size();
				int[] startingPlayers = IntStream.generate(() -> Game.instance.rnd.nextInt(2)).limit(size).toArray();
				int[] rndSeeds = IntStream.generate(() -> Game.instance.rnd.nextInt()).limit(size).toArray();
				BattleExecutor[] battleExecutors = Collections.nCopies(size, BattleExecutorAutomatic.instance)
						.toArray(new BattleExecutor[size]);
				battleExecutors[0] = BattleExecutorManual.instance;
				List<Battle> battles = executeBattles(players1, players2, startingPlayers, rndSeeds, battleExecutors);

				// Wait for battle to finish and process results
				for (Battle battle : battles) {
					LeaguePlayer player0 = (LeaguePlayer) battle.players[0];
					LeaguePlayer player1 = (LeaguePlayer) battle.players[1];
					if (battle.winner() == 0) {
						player0.addWin();
						player1.addDefeat();
					} else {
						player1.addWin();
						player0.addDefeat();
					}
				}

				Collections.sort(Game.instance.players);

				Game.instance.updateAvailableToBuy();

				// Autosave
				Persistence.serialize("autosave");

				roundHasFinished.release();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// Starts executing the battles in the order of the lists
	// If you want the player's battle to be initialized right away, put it first in
	// the lists
	public List<Battle> executeBattles(List<Player> players1, List<Player> players2, int[] startingPlayers,
			int[] rndSeeds, BattleExecutor[] battleExecutors) {
		List<Battle> battles = new ArrayList<>();

		List<Future<Battle>> battleFutures = new ArrayList<>();

		// Initialize the rest of the battles
		for (int i = 0; i < players1.size(); i++) {
			Player player1 = players1.get(i);
			Player player2 = players2.get(i);
			int startingPlayer = startingPlayers[i];
			int rndSeed = rndSeeds[i];
			Future<Battle> battleFuture;

			BattleExecutor battleExecutor = battleExecutors[i];
			battleFuture = executorService
					.submit(() -> battleExecutor.executeBattle(player1, player2, startingPlayer, rndSeed));

			battleFutures.add(battleFuture);
		}

		// Wait for battle to finish and process results
		for (Future<Battle> battleFuture : battleFutures) {
			Battle battle = null;
			try {
				battle = battleFuture.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			battles.add(battle);
		}

		return battles;
	}
}
