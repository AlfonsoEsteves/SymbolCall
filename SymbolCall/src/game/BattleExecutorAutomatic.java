package game;

import battle.Battle;
import battle.Player;

public class BattleExecutorAutomatic extends BattleExecutor {
	
	public static BattleExecutorAutomatic instance = new BattleExecutorAutomatic();
	
	private BattleExecutorAutomatic() {}

	@Override
	public Battle executeBattle(Player p1, Player p2, int startingPlayer, int rndSeed) {

		Battle battle = new Battle(p1, p2, startingPlayer, rndSeed);
		while (battle.winner() == -1) {
			if (battle.state == Battle.choosingActiveEffectState) {
				battle.computerAIs[battle.turn].play(battle);
			} else if (battle.state == Battle.choosingTargetCardState) {
				battle.computerAIs[battle.turn].chooseTarget(battle);
			} else if (battle.state == Battle.executingActionState) {
				battle.executeAction(Battle.noneAISimulating);
			}
		}
		
		// Removing the AIs from the battle allows the Garvage Collector erase them
		// Remember that ThreadManager stores battles in a list after they finish
		battle.computerAIs = null;
		
		return battle;
	}

}
