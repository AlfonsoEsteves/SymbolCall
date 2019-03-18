package game;

import java.util.Random;

import battle.BPlayer;
import battle.Battle;
import battle.ComputerAI;
import battle.Rnd;
import bruteForceAI.BruteForceAI;

public class BattleExecutorAutomatic {
	
	public static BattleExecutorAutomatic instance = new BattleExecutorAutomatic();

	public Battle executeBattle(BPlayer p1, BPlayer p2, int startingPlayer, Random battleRandom) {
		//I can not use a single instance of the ComputerAI due to parallelism
		//And I prefer not to have a ComputerAI in each player because
		//the node trees can consume a lot of memory
		ComputerAI computerAI = new BruteForceAI();
		
		Battle battle = new Battle(p1, p2, startingPlayer, battleRandom);
		while (battle.winner() == -1) {
			if (battle.state == Battle.choosingActiveEffectState) {
				computerAI.play(battle);
			} else if (battle.state == Battle.choosingTargetCardState) {
				computerAI.chooseTarget(battle);
			} else if (battle.state == Battle.executingActionState) {
				battle.executeAction();
			}
		}
		System.out.println("battle finished: " + p1.name + " vs " + p2.name);
		return battle;
	}

}
