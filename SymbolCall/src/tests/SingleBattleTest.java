package tests;

import java.util.Arrays;

import battle.Player;
import bruteForceAI.BruteForceAI.BruteForceAIFactory;
import game.Game;
import game.ThreadManager;
import gui.MainFrame;

public class SingleBattleTest {

	public static void main(String[] args) {		
		Game.instantiate();
		
		Player player1 = new Player(new BruteForceAIFactory(), "Defensive_pr_yl_bl", 0);
		Player player2 = new Player(new BruteForceAIFactory(), "Activo_fuerte_rd_bl_yl", 0);
		int startingPlayer = 0;
		int rndSeed = 412961991;
		
		MainFrame.instantiate();
		
		ThreadManager.ins.executeBattles(Arrays.asList(player1), Arrays.asList(player2), new int[] {startingPlayer}, new int[]{rndSeed});

		try {
			ThreadManager.ins.humanBattleCanBeStarted.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		MainFrame.instance.enterBox(MainFrame.instance.battleBox);
	}

}
