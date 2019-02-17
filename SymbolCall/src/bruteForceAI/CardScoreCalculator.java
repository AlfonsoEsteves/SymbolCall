package bruteForceAI;

import battle.Action;
import battle.BCard;
import battle.Battle;
import battle.Card;
import battle.Effect;

public class CardScoreCalculator {
	
	public static void calculateScores(Card card) {
		double sumFieldScore=AIValues.initialScore;
		double sumHandScore=AIValues.initialScore;
		for(int i=1;i<=AIValues.iterations;i++) {
			card.handScore=sumHandScore/(double)i;
			card.fieldScore=sumFieldScore/(double)i;
			calculateScoresAux(card);
			sumHandScore+=card.handScore;
			sumFieldScore+=card.fieldScore;
		}
	}
	
	private static void calculateScoresAux(Card card) {
		//I clean all the values except for the hand score and field score
		card.fieldDurableActive=0;
		card.fieldDurablePassive=0;
		card.fieldOnceActive=0;
		card.fieldOncePassive=0;
		card.handDurableActive=0;
		card.handDurablePassive=0;
		card.handOnceActive=0;
		card.handOncePassive=0;
		card.handTotalScoreActive=0;
		card.fieldTotalScoreActive=0;
		card.probLeaveHandEachTurn=0;
		card.probLeaveFieldEachTurn=0;
		
		calculateAtomicScores(card);
		calculateLongevity(card);
		card.handScore=Math.sqrt(card.handDurableActive*card.handLongevity+card.handOnceActive)+card.handDurablePassive*card.handLongevity+card.handOncePassive;
		card.fieldScore=Math.sqrt(card.fieldDurableActive*card.fieldLongevity+card.fieldOnceActive)+card.fieldDurablePassive*card.fieldLongevity+card.fieldOncePassive;
	}
	
	public static double calculateCurrentFieldScore(BCard card) {
		double turnsToGetKilled=AIValues.minTurnsToGetKilled+card.health*AIValues.turnsToGetKilledPerHealthPoint;
		double fieldLongevity=turnsToGetKilled*(1-card.model.probLeaveFieldEachTurn);
		return Math.sqrt(card.model.fieldDurableActive*fieldLongevity+card.model.fieldOnceActive)+card.model.fieldDurablePassive*fieldLongevity+card.model.fieldOncePassive;
	}

	//The active and passive effects are given a probability in different way
	//The passives effects depend on the amount of symbols they require
	//The active effects gets weighted according to how good the effect is
	private static void calculateLongevity(Card card) {
		for(Effect effect : card.effects) {
			double effectProbability=calculateEffectProbability(effect, card);
			double effectDurability=calculateEffectDurability(effect, card);
			if(effect.zone==Battle.handZone) {
				card.probLeaveHandEachTurn+=(1-card.probLeaveHandEachTurn)*effectProbability*(1-effectDurability);
			}
			else {
				card.probLeaveFieldEachTurn+=(1-card.probLeaveFieldEachTurn)*effectProbability*(1-effectDurability);
			}
		}
		double turnsToGetKilled=AIValues.minTurnsToGetKilled+card.maxHealth*AIValues.turnsToGetKilledPerHealthPoint;
		if(card.probLeaveHandEachTurn!=0) {
			card.handLongevity=1/card.probLeaveHandEachTurn;
		}
		else {
			card.handLongevity=AIValues.infiniteLongetivity;
		}
		card.fieldLongevity=turnsToGetKilled*(1-card.probLeaveFieldEachTurn);
	}

	private static double calculateEffectProbability(Effect effect, Card card) {
		if(effect.sequence.isEmpty()) {
			double effectScore=calculateEffectScore(effect, card);
			if(effect.zone==Battle.handZone) {
				return effectScore/card.handTotalScoreActive;
			}
			else {
				return effectScore/card.fieldTotalScoreActive;
			}
		}
		else {
			return 1.0/(AIValues.minSequenceUnlikelihood+effect.sequence.size()*AIValues.sequenceUnlikelihoodPerSymbol);
		}
	}

	private static void calculateAtomicScores(Card card) {
		for(Effect effect : card.effects) {
			double effectScore=calculateEffectScore(effect, card);
			double effectDurability=calculateEffectDurability(effect, card);
			if(effect.zone==Battle.handZone) {
				if(effect.sequence.isEmpty()) {
					card.handDurableActive+=effectScore*effectScore*effectDurability;
					card.handOnceActive+=effectScore*effectScore*(1-effectDurability);
					card.handTotalScoreActive+=effectScore;
				}
				else {
					double effectProbability=calculateEffectProbability(effect, card);
					card.handDurablePassive+=effectScore*effectProbability*effectDurability;
					card.handOncePassive+=effectScore*effectProbability*(1-effectDurability);
				}
			}
			else {
				if(effect.sequence.isEmpty()) {
					card.fieldDurableActive+=effectScore*effectScore*effectDurability;
					card.fieldOnceActive+=effectScore*effectScore*(1-effectDurability);
					card.fieldTotalScoreActive+=effectScore;
				}
				else {
					double effectProbability=calculateEffectProbability(effect, card);
					card.fieldDurablePassive+=effectScore*effectProbability*effectDurability;
					card.fieldOncePassive+=effectScore*effectProbability*(1-effectDurability);
				}
			}
		}
	}

	//The effect durability is:
	//1 if it doesn't have actions that would move the card
	//0.X if it is a field effect that self damages the card
	//0 if it has an action that certainly moves it
	private static double calculateEffectDurability(Effect effect, Card card) {
		if(effect.zone==Battle.handZone) {
			for(Action action : effect.actions) {
				if(action.info==Battle.slTarget) {
					if(action.type==Battle.dscAction) {
						return 0;
					}
					if(action.type==Battle.invAction) {
						return 0;
					}
				}
			}
			return 1;
		}
		else {
			double durability=1;
			for(Action action : effect.actions) {
				if(action.info==Battle.slTarget) {
					if(action.type==Battle.wdrAction) {
						return 0;
					}
					if(action.type==Battle.atkAction) {
						durability-=(double)action.amount/(double)card.maxHealth;
					}
				}
			}
			return durability;
		}
	}

	private static double calculateEffectScore(Effect effect, Card card) {
		double score=0;
		for(Action action : effect.actions) {
			score+=calculateEffectActionScore(action, card);
		}
		if(effect.sequence.isEmpty()) {
			if(score>AIValues.minActiveScore) {
				return score;
			}
			else {
				return AIValues.minActiveScore;
			}
		}
		else {
			return score/(effect.sequence.size()+1);
		}
	}

	private static double calculateEffectActionScore(Action action, Card card) {
		if(action.type==Battle.atkAction) {
			if(action.info==Battle.chTarget) {
				return action.amount*AIValues.fieldDamagefactor;
			}
			if(action.info==Battle.opTarget) {
				return action.amount;
			}
			if(action.info==Battle.owTarget) {
				return -action.amount;
			}
			if(action.info==Battle.slTarget) {
				return -((card.fieldScore*action.amount)/card.maxHealth);
			}
		}
		if(action.type==Battle.dscAction) {
			if(action.info==Battle.chTarget) {
				return AIValues.chooseDsc;
			}
			if(action.info==Battle.slTarget) {
				return -card.handScore;
			}
		}
		if(action.type==Battle.wdrAction) {
			if(action.info==Battle.chTarget) {
				return AIValues.chooseWdr;
			}
			if(action.info==Battle.slTarget) {
				return card.handScore-card.fieldScore;
			}
		}
		if(action.type==Battle.invAction) {
			if(action.info==Battle.chTarget) {
				return AIValues.chooseInv;
			}
			if(action.info==Battle.slTarget) {
				return card.fieldScore-card.handScore;
			}
		}
		return 0;
	}
}
