package game;

import java.util.Random;

import battle.BPlayer;
import battle.Battle;

public class BattleExecutorAutomatic {
	
	public static BattleExecutorAutomatic instance = new BattleExecutorAutomatic();

	public Battle executeBattle(BPlayer p1, BPlayer p2, int startingPlayer, Random battleRandom) {

		System.out.println("battle started: " + p1.name + " vs " + p2.name);
		
		Battle battle = new Battle(p1, p2, startingPlayer, battleRandom);
		while (battle.winner() == -1) {
			if (battle.state == Battle.choosingActiveEffectState) {
				battle.computerAIs[battle.turn].play(battle);
			} else if (battle.state == Battle.choosingTargetCardState) {
				battle.computerAIs[battle.turn].chooseTarget(battle);
			} else if (battle.state == Battle.executingActionState) {
				battle.executeAction();
			}
		}
		
		System.out.println("battle finished: " + p1.name + " vs " + p2.name);
		return battle;
	}

}
