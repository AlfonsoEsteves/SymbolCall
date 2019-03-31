package tests;

import battle.Battle;
import battle.Player;
import zAI.ZAI.ZAIFactory;
import game.BattleExecutorAutomatic;
import game.BattleExecutorManual;
import game.Game;
import gui.MainFrame;

public class SingleBattleTest {
	
	public static void main(String[] args) {		
		Game.instantiate();
		Player player1 = new Player(new ZAIFactory(), "Activo_fuerte_rd_bl_yl", 0);
		Player player2 = new Player(new ZAIFactory(), "White dragon", 0);
		int startingPlayer = 0;
		int rndSeed = 1749553447;
		
		MainFrame.instantiate();
		
		Battle battle = BattleExecutorManual.instance.executeBattle(player1, player2, startingPlayer, rndSeed);
		
		System.out.println("Battle finished, winner: " + battle.winner());
	}

}
