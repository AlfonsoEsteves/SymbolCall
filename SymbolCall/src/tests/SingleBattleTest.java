package tests;

import battle.Battle;
import battle.Player;
import game.BattleExecutorManual;
import game.Game;
import gui.MainFrame;
import zAI.ZAI.ZAIFactory;

public class SingleBattleTest {
	
	public static void main(String[] args) {		
		Game.instantiate();
		Player player1 = new Player(null, "Swamp", 0);
		Player player2 = new Player(new ZAIFactory(), "Defensive_pr_yl_bl", 0);
		int startingPlayer = 0;
		int rndSeed = 5;
		
		MainFrame.instantiate();
		
		Battle battle = BattleExecutorManual.instance.executeBattle(player1, player2, startingPlayer, rndSeed);
		
		System.out.println("Battle finished, winner: " + battle.winner());
	}

}
