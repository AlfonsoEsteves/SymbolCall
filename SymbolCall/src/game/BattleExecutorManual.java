package game;

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

	public Battle executeBattleOfHumanPlayer() {

		// Find the player rival
		int index = Game.players.indexOf(Game.humanPlayer);
		if (index % 2 == 0) {
			index++;
		} else {
			index--;
		}
		Player rival = Game.players.get(index);

		// Initialize battle
		Game.battle = new Battle(Game.humanPlayer, rival, Rnd.nextInt(2));

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

		return Game.battle;
	}

}
