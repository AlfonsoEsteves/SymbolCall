package game;

import java.util.Random;

import battle.BPlayer;
import battle.Battle;
import battle.ComputerAI;
import bruteForceAI.BruteForceAI;

public class BattleExecutorManual {

	public static BattleExecutorManual instance = new BattleExecutorManual();

	public Battle executeBattle(BPlayer player1, BPlayer player2, int startingPlayer, Random battleRandom) {
		
		BPlayer humanPlayer = player1.isHuman() ? player1 : player2;
		BPlayer computerPlayer = player1.isHuman() ? player2 : player1;

		// Initialize battle
		Game.ins.battle = new Battle(humanPlayer, computerPlayer, startingPlayer, battleRandom);

		// The GUI is notified that it can go on with the battle
		ThreadManager.ins.humanBattleCanBeStarted.release();

		// The GUI should notify when the battle is over
		try {
			ThreadManager.ins.humanBattleHasFinished.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return Game.ins.battle;
	}

}
