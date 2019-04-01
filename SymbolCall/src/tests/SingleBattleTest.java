package tests;

import battle.Battle;
import battle.Player;
import battle.ComputerAI.NullComputerAIFactory;
import zAI.ZAI.ZAIFactory;
import game.BattleExecutorManual;
import game.Game;
import gui.MainFrame;

public class SingleBattleTest {
	
	public static void main(String[] args) {		
		Game.instantiate();
		Player player1 = new Player(new NullComputerAIFactory(), "White_dragon", 0);
		Player player2 = new Player(new ZAIFactory(), "Defensive_pr_yl_bl", 0);
		int startingPlayer = 0;
		int rndSeed = 3;
		
		MainFrame.instantiate();
		
		Battle battle = BattleExecutorManual.instance.executeBattle(player1, player2, startingPlayer, rndSeed);
		
		System.out.println("Battle finished, winner: " + battle.winner());
	}

}
