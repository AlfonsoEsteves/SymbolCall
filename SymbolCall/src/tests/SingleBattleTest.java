package tests;

import battle.Battle;
import battle.Player;
import bruteForceAI.BruteForceAI.BruteForceAIFactory;
import game.BattleExecutorManual;
import game.Game;
import gui.MainFrame;

public class SingleBattleTest {
	
	public static void main(String[] args) {		
		Game.instantiate();
		
		Player player1 = new Player(new BruteForceAIFactory(), "Swamp", 0);
		Player player2 = new Player(new BruteForceAIFactory(), "Master_bl_gr", 0);
		int startingPlayer = 0;
		int rndSeed = 227364480;
		
		MainFrame.instantiate();
		
		Battle battle = BattleExecutorManual.instance.executeBattle(player1, player2, startingPlayer, rndSeed);
		
		System.out.println("Battle finished, winner: " + battle.winner());
	}

}
