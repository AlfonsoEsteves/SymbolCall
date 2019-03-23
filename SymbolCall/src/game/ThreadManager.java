package game;

import java.util.ArrayList;
import java.util.Collections;
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
				
				executeRound();
				Game.ins.updateAvailableToBuy();

				// Autosave
				Persistence.serialize("save");
				
				roundHasFinished.release();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void executeRound() throws InterruptedException, ExecutionException {

		List<Future<Battle>> battleFutures = new ArrayList<>();
		
		// Initialize human battle
		Random humanBattleRandom = Rnd.newRandom();
		Future<Battle> humanBattleFuture = executorService.submit(() -> BattleExecutorManual.instance.executeBattleOfHumanPlayer(Rnd.nextInt(2), humanBattleRandom));
		battleFutures.add(humanBattleFuture);
		
		// Initialize the rest of the battles
		for (int i = 0; i < Game.ins.players.size(); i += 2) {
			Player p1 = Game.ins.players.get(i);
			Player p2 = Game.ins.players.get(i + 1);
			if (!p1.isHuman() && !p2.isHuman()) {
				Random battleRandom = Rnd.newRandom();
				Future<Battle> battleFuture = executorService.submit(() -> BattleExecutorAutomatic.instance.executeBattle(p1, p2, Rnd.nextInt(2), battleRandom));
				battleFutures.add(battleFuture);
			}
		}
		
		//Wait for battle to finish and process results
		for(Future<Battle> battleFuture : battleFutures) {
			Battle battle = battleFuture.get();
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
	}
}
