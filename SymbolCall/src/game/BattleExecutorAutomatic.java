package game;

import battle.BPlayer;
import battle.Battle;
import battle.Rnd;

public class BattleExecutorAutomatic {
	
	public static BattleExecutorAutomatic instance = new BattleExecutorAutomatic();

	public Battle executeBattle(BPlayer p1, BPlayer p2) {
		Battle battle = new Battle(p1, p2, Rnd.nextInt(2));
		while (battle.winner() == -1) {
			if (battle.state == Battle.choosingActiveEffectState) {
				battle.players[battle.turn].computerAI.play(battle);
			} else if (battle.state == Battle.choosingTargetCardState) {
				battle.players[battle.decidingPlayer].computerAI.chooseTarget(battle);
			} else if (battle.state == Battle.executingActionState) {
				battle.executeAction();
			}
		}
		return battle;
	}

}
