package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

import battle.BPlayer;
import battle.Battle;
import persistence.Persistence;

public class ThreadManager {
	
	//This does not count the AWT thread and ThreadManager thread itself
	public static final int numberOfThreads = 3;
	
	public static ThreadManager ins = new ThreadManager(); 
	
	private ExecutorService executorService;

	public Semaphore humanBattleCanBeStarted = new Semaphore(0);
	public Semaphore humanBattleHasFinished = new Semaphore(0);
	public Semaphore roundCanBeStarted = new Semaphore(0);
	public Semaphore roundHasFinished = new Semaphore(0);
	
	public ThreadManager() {
		executorService = Executors.newFixedThreadPool(numberOfThreads);
	}

	public void runGame() {
		while(true) {
			try {
				roundCanBeStarted.acquire();
				
				LinkedList<BPlayer> players1 = new LinkedList<>();
				LinkedList<BPlayer> players2 = new LinkedList<>();
				for(int i = 0; i < Game.ins.players.size() / 2; i++) {
					LeaguePlayer player1 = Game.ins.players.get(i * 2);
					LeaguePlayer player2 = Game.ins.players.get(i * 2 + 1);
					if (player1.isHuman() || player2.isHuman()) {
						players1.addFirst(player1);
						players2.addFirst(player2);
					}
					else {
						players1.addLast(player1);
						players2.addLast(player2);
					}
				}
				
				int[] startingPlayers = IntStream.generate(() -> Game.ins.rnd.nextInt(2)).limit(players1.size()).toArray();
				List<Battle> battles = executeBattles(players1, players2, startingPlayers);
				
				//Wait for battle to finish and process results
				for(Battle battle : battles) {
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

				Collections.sort(Game.ins.players);
				
				Game.ins.updateAvailableToBuy();

				// Autosave
				Persistence.serialize("autosave");
				
				roundHasFinished.release();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// Starts executing the battles in the order of the lists
	// If you want the player's battle to be initialized right away, put it first in the lists
	public List<Battle> executeBattles(List<BPlayer> players1, List<BPlayer> players2, int[] startingPlayers) {
		List<Battle> battles = new ArrayList<>();

		List<Future<Battle>> battleFutures = new ArrayList<>();
		
		// Initialize the rest of the battles
		for (int i = 0; i < players1.size(); i ++) {
			BPlayer player1 = players1.get(i);
			BPlayer player2 = players2.get(i);
			int startingPlayer = startingPlayers[i];
			Random battleRandom = Game.ins.rnd.newRandom();
			Future<Battle> battleFuture;
			if (player1.isHuman() || player2.isHuman()) {
				battleFuture = executorService.submit(() -> BattleExecutorManual.instance.executeBattle(player1, player2, startingPlayer, battleRandom));
			}
			else {
				battleFuture = executorService.submit(() -> BattleExecutorAutomatic.instance.executeBattle(player1, player2, startingPlayer, battleRandom));	
			}
			battleFutures.add(battleFuture);
		}
		
		//Wait for battle to finish and process results
		for(Future<Battle> battleFuture : battleFutures) {
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
