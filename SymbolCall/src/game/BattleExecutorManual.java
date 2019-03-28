package game;

import battle.Player;
import gui.MainFrame;
import battle.Battle;

public class BattleExecutorManual extends BattleExecutor {

	public static BattleExecutorManual instance = new BattleExecutorManual();
	
	private BattleExecutorManual() {}

	@Override
	public Battle executeBattle(Player player1, Player player2, int startingPlayer, int rndSeed) {

		Game.instance.battle = new Battle(player1, player2, startingPlayer, rndSeed);

		MainFrame.instance.enterBox(MainFrame.instance.battleBox);

		// The GUI should notify when the battle is over
		try {
			ThreadManager.instance.humanBattleHasFinished.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return Game.instance.battle;
	}

}
