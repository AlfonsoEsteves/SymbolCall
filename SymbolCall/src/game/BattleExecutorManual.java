package game;

import java.util.Random;

import battle.Battle;
import battle.ComputerAI;
import battle.Rnd;
import bruteForceAI.BruteForceAI;
import gui.MainFrame;

public class BattleExecutorManual {

	public static BattleExecutorManual instance = new BattleExecutorManual();

	// This computerAI should only be used in the human player battles (to avoid it
	// being used multiple times at the same time).
	public static ComputerAI computerAI = new BruteForceAI();

	public Battle executeBattleOfHumanPlayer(int startingPlayer, Random battleRandom) {

		// Find the player rival
		int index = Game.ins.players.indexOf(Game.ins.humanPlayer);
		if (index % 2 == 0) {
			index++;
		} else {
			index--;
		}
		Player rival = Game.ins.players.get(index);

		// Initialize battle
		Game.ins.battle = new Battle(Game.ins.humanPlayer, rival, startingPlayer, battleRandom);

		// The GUI is notified that it can go on with the battle
		synchronized (MainFrame.instance) {
			MainFrame.instance.notify();
		}

		// The GUI should notify when the battle is over
		try {
			synchronized (BattleExecutorManual.instance) {
				BattleExecutorManual.instance.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return Game.ins.battle;
	}

}
