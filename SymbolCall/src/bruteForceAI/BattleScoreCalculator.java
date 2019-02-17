package bruteForceAI;

import battle.Battle;

public class BattleScoreCalculator {

	public static double calculateInitialScore(Battle scenario) {
		int winner=scenario.checkWinner();
		if(winner!=-1) {
			return winner;
		}
		else {
			double[] scores=new double[2];
			for(int i=0;i<2;i++) {
				scores[i]+=scenario.healths[i]*AIValues.healthFactor;
				for(int c : scenario.zones[i][Battle.fieldZone]) {
					scores[i]+=CardScoreCalculator.calculateCurrentFieldScore(scenario.cards[c]);
				}
				for(int c : scenario.zones[i][Battle.handZone]) {
					scores[i]+=scenario.cards[c].model.handScore;
				}
			}
			return scores[1]/(scores[0]+scores[1]);
		}
	}
	
}
