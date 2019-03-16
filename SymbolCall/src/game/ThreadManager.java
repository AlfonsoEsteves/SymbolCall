package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import battle.Battle;
import gui.MainFrame;

public class ThreadManager implements Runnable {
	
	//This does not count the AWT thread and ThreadManager thread itself
	public static final int numberOfThreads = 2;
	
	public static ThreadManager instance = new ThreadManager();
	
	private ExecutorService executorService;

	@Override
	public void run() {
		executorService = Executors.newFixedThreadPool(numberOfThreads);
		while(true) {
			try {
				synchronized(ThreadManager.instance) {
					ThreadManager.instance.wait();
				}
				executeRound();
				Game.ins.updateAvailableToBuy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void executeRound() throws InterruptedException, ExecutionException {

		List<Future<Battle>> battleFutures = new ArrayList<>();
		
		// Initialize human battle
		Future<Battle> humanBattleFuture = executorService.submit(() -> BattleExecutorManual.instance.executeBattleOfHumanPlayer());
		battleFutures.add(humanBattleFuture);
		
		// Initialize the rest of the battles
		for (int i = 0; i < Game.ins.players.size(); i += 2) {
			Player p1 = Game.ins.players.get(i);
			Player p2 = Game.ins.players.get(i + 1);
			if (!p1.isHuman() && !p2.isHuman()) {
				Future<Battle> battleFuture = executorService.submit(() -> BattleExecutorAutomatic.instance.executeBattle(p1, p2));
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
		
		synchronized(MainFrame.instance) {
			// Notify the main frame that all the battles of the round
			// have completed
			MainFrame.instance.notify();
		}
	}
}
