package game;

import battle.Player;
import battle.Battle;

public class BattleExecutorManual {

	public static BattleExecutorManual instance = new BattleExecutorManual();

	public Battle executeBattle(Player player1, Player player2, int startingPlayer, int rndSeed) {
		
		Player humanPlayer = player1.isHuman() ? player1 : player2;
		Player computerPlayer = player1.isHuman() ? player2 : player1;

		// Initialize battle
		Game.instance.battle = new Battle(humanPlayer, computerPlayer, startingPlayer, rndSeed);

		// The GUI is notified that it can go on with the battle
		ThreadManager.ins.humanBattleCanBeStarted.release();

		// The GUI should notify when the battle is over
		try {
			ThreadManager.ins.humanBattleHasFinished.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return Game.instance.battle;
	}

}
