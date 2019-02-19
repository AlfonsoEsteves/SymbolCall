package game;

import battle.BPlayer;
import battle.Battle;
import battle.Rnd;

public class BattleExecutor {

	public static Battle executeBattle(BPlayer p1, BPlayer p2) {
		Battle battle = new Battle(p1, p2, Rnd.nextInt(2));
		int winner = -1;
		while (winner == -1) {
			if (battle.state == Battle.choosingActiveEffectState) {
				battle.players[battle.turn].computerAI.play(battle);
			} else if (battle.state == Battle.choosingTargetCardState) {
				battle.players[battle.decidingPlayer].computerAI.chooseTarget(battle);
			} else if (battle.state == Battle.executingActionState) {
				battle.executeAction();
			}
			winner = battle.winner();
		}
		return battle;
	}

}
