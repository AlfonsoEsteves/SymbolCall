package game;

import java.util.concurrent.Semaphore;

import battle.BPlayer;
import battle.Battle;
import battle.Rnd;
import gui.MainFrame;

public class BattleExecutor {
	
	public static Battle battle;
	public static int battleWinner;
	public static Semaphore battleSemaphore = new Semaphore(0);
	
	public static int executeBattle(BPlayer[] players) {
		battle = new Battle(players, Rnd.nextInt(2));
		if(players[0].computerAI == null || players[1].computerAI == null) {
			MainFrame.instance.changeToBattleBox();
			try {
				battleSemaphore.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return battleWinner;
		}
		else {
			return executeBattleAutomatically();
		}
	}
	
	private static int executeBattleAutomatically() {
		int winner=-1;
		while(winner==-1) {
			if(battle.state==Battle.choosingActiveEffectState) {
				battle.players[battle.turn].computerAI.play(battle);
			}
			else if(battle.state==Battle.choosingTargetCardState) {
				battle.players[battle.decidingPlayer].computerAI.chooseTarget(battle);
			}
			else if(battle.state==Battle.executingActionState) {
				battle.executeAction();
			}
			winner=battle.checkWinner();
		}
		return winner;
	}
}
