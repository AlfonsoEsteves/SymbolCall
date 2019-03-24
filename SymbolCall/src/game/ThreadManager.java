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

import battle.Battle;
import battle.Rnd;
import persistence.Persistence;

public class ThreadManager {
	
	//This does not count the AWT thread and ThreadManager thread itself
	public static final int numberOfThreads = 2;
	
	public static ThreadManager ins = new ThreadManager(); 
	
	private ExecutorService executorService;

	public Semaphore humanBattleCanBeStarted = new Semaphore(0);
	public Semaphore humanBattleHasFinished = new Semaphore(0);
	public Semaphore roundCanBeStarted = new Semaphore(0);
	public Semaphore roundHasFinished = new Semaphore(0);

	public void runGame() {
		executorService = Executors.newFixedThreadPool(numberOfThreads);
		while(true) {
			try {
				roundCanBeStarted.acquire();
				
				LinkedList<Player> players1 = new LinkedList<>();
				LinkedList<Player> players2 = new LinkedList<>();
				for(int i = 0; i < Game.ins.players.size() / 2; i++) {
					Player player1 = Game.ins.players.get(i * 2);
					Player player2 = Game.ins.players.get(i * 2 + 1);
					if (player1.isHuman() || player2.isHuman()) {
						players1.addFirst(player1);
						players2.addFirst(player2);
					}
					else {
						players1.addLast(player1);
						players2.addLast(player2);
					}
				}
				
				List<Battle> battles = executeBattles(players1, players2);
				
				//Wait for battle to finish and process results
				for(Battle battle : battles) {
					Player player0 = (Player) battle.players[0];
					Player player1 = (Player) battle.players[1];
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
	public List<Battle> executeBattles(List<Player> players1, List<Player> players2) {
		List<Battle> battles = new ArrayList<>();

		List<Future<Battle>> battleFutures = new ArrayList<>();
		
		// Initialize the rest of the battles
		for (int i = 0; i < players1.size(); i ++) {
			Player player1 = players1.get(i);
			Player player2 = players2.get(i);
			Random battleRandom = Game.ins.rnd.newRandom();
			Future<Battle> battleFuture;
			if (player1.isHuman() || player2.isHuman()) {
				battleFuture = executorService.submit(() -> BattleExecutorManual.instance.executeBattle(player1, player2, Game.ins.rnd.nextInt(2), battleRandom));
			}
			else {
				battleFuture = executorService.submit(() -> BattleExecutorAutomatic.instance.executeBattle(player1, player2, Game.ins.rnd.nextInt(2), battleRandom));	
			}
			battleFutures.add(battleFuture);
		}
		
		//Wait for battle to finish and process results
		for(Future<Battle> battleFuture : battleFutures) {
			Battle battle;
			try {
				battle = battleFuture.get();
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException(e);
			}
			battles.add(battle);
		}
		
		return battles;
	}
}
