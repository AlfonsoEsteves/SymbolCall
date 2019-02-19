package automatic;

import battle.Battle;
import game.BattleExecutor;

public class AutoBattle {

	public static void executeBattle(TestedPlayer player0, TestedPlayer player1) {
		Battle battle = BattleExecutor.executeBattle(player0.player, player1.player);
		if (battle.winner() == 0) {
			player0.wonGamesAsFirst++;
			player0.winTurnsCount += battle.turnCount;
			player1.loseTurnsCount += battle.turnCount;
		} else {
			player1.wonGamesAsSecond++;
			player1.winTurnsCount += battle.turnCount;
			player0.loseTurnsCount += battle.turnCount;
		}
		player0.totalGamesAsFirst++;
		player1.totalGamesAsSecond++;

		int drawnCards0 = 0;
		int drawnCards1 = 0;
		for (int i = 0; i < Battle.deckSize; i++) {
			if (battle.cards[i].drawn) {
				drawnCards0++;
			}
			if (battle.cards[Battle.deckSize + i].drawn) {
				drawnCards1++;
			}
		}

		for (int i = 0; i < Battle.deckSize; i++) {
			if (battle.cards[i].drawn) {
				player0.totalGamesByCards[i] += 1.0 / (double) drawnCards0;
				if (battle.winner() == 0) {
					player0.wonGamesByCards[i] += 1.0 / (double) drawnCards0;
				}
			}
			if (battle.cards[Battle.deckSize + i].drawn) {
				player1.totalGamesByCards[i] += 1.0 / (double) drawnCards1;
				if (battle.winner() == 1) {
					player1.wonGamesByCards[i] += 1.0 / (double) drawnCards1;
				}
			}
		}
	}
}
