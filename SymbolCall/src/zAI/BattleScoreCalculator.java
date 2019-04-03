package zAI;

import battle.Battle;
import bruteForceAI.AIValues;

public class BattleScoreCalculator {

	public static double calculateInitialScore(Battle scenario, int AISimulating) {
		int winner = scenario.winner();
		if (winner != -1) {
			return winner;
		} else {
			double[] scores = new double[2];
			for (int i = 0; i < 2; i++) {
				scores[i] += scenario.healths[i] * AIValues.healthFactor;
				for (int c : scenario.zones[i][Battle.fieldZone]) {
					scores[i] += CardScoreCalculator.calculateCurrentFieldScore(scenario.cards[c]);
				}
				for (int c : scenario.zones[i][Battle.handZone]) {
					if (scenario.cards[c].visible || scenario.cards[c].player == AISimulating) {
						scores[i] += scenario.cards[c].model.handScore;
					}
					else {
						scores[i] += AIValues.initialScore;
					}
				}
			}
			return scores[1] / (scores[0] + scores[1]);
		}
	}

}
