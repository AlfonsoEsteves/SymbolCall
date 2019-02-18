package game;

import battle.BPlayer;
import battle.Battle;
import battle.Rnd;
	
public class BattleExecutor {

	public static int executeBattle(BPlayer[] players) {
		Battle battle = new Battle(players, Rnd.nextInt(2));
		int winner = -1;
		while (winner == -1) {
			if (battle.state == Battle.choosingActiveEffectState) {
				battle.players[battle.turn].computerAI.play(battle);
			} else if (battle.state == Battle.choosingTargetCardState) {
				battle.players[battle.decidingPlayer].computerAI.chooseTarget(battle);
			} else if (battle.state == Battle.executingActionState) {
				battle.executeAction();
			}
			winner = battle.checkWinner();
		}
		return winner;	
	}
	
}
