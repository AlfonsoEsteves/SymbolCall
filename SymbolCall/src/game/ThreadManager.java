package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import battle.Battle;

public class ThreadManager implements Runnable {
	
	public static ThreadManager instance = new ThreadManager();
	
	private ExecutorService executorService;

	@Override
	public void run() {
		executorService = Executors.newFixedThreadPool(10);
		while(true) {
			try {
				synchronized(ThreadManager.instance) {
					ThreadManager.instance.wait();
				}
				executeRound();
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
		for (int i = 0; i < Game.players.size(); i += 2) {
			Player p1 = Game.players.get(i);
			Player p2 = Game.players.get(i + 1);
			if (!p1.isHumanPlayer() && !p2.isHumanPlayer()) {
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

		Collections.sort(Game.players);
	}
}