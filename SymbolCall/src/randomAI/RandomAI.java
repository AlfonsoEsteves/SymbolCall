package randomAI;

import java.util.Random;

import battle.BCard;
import battle.Battle;
import battle.ComputerAI;
import battle.Effect;
import battle.Rnd;

public class RandomAI extends ComputerAI {

	public static class RandomAIFactory extends ComputerAIFactory {
		public ComputerAI create(int player, int rndSeed) {
			return new RandomAI(player, rndSeed);
		}
	}

	private Random rnd;

	public RandomAI(int player, int rndSeed) {
		super(player);
		rnd = new Random(rndSeed);
	}

	@Override
	public void play(Battle battle) {
		if (rnd.nextInt(2) == 0) {
			if (!playFromZone(battle, Battle.handZone)) {
				if (!playFromZone(battle, Battle.fieldZone)) {
					battle.passTurn();
				}
			}
		} else {
			if (!playFromZone(battle, Battle.fieldZone)) {
				if (!playFromZone(battle, Battle.handZone)) {
					battle.passTurn();
				}
			}
		}
	}

	private boolean playFromZone(Battle battle, int zone) {
		if (!battle.zones[battle.turn][zone].isEmpty()) {
			int r1 = rnd.nextInt(battle.zones[battle.turn][zone].size());
			for (int i = 0; i < battle.zones[battle.turn][zone].size(); i++) {
				int c = battle.zones[battle.turn][zone].get((r1 + i) % battle.zones[battle.turn][zone].size());
				BCard card = battle.cards[c];
				if (card.turn) {
					int r2 = rnd.nextInt(card.model.effects.size());
					for (int j = 0; j < card.model.effects.size(); j++) {
						int effectNumber = (r2 + j) % card.model.effects.size();
						Effect effect = card.model.effects.get(effectNumber);
						if (card.turn && effect.zone == card.zone && effect.sequence.isEmpty()) {
							battle.executeActiveEffect(card.battleId, effectNumber);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public void chooseTarget(Battle battle) {
		int player;
		int zone;
		if (battle.choosingTargetStateAction.type == Battle.atkAction) {
			player = 1 - battle.turn;
			zone = Battle.fieldZone;
		} else if (battle.choosingTargetStateAction.type == Battle.wdrAction) {
			player = 1 - battle.turn;
			zone = Battle.fieldZone;
		} else if (battle.choosingTargetStateAction.type == Battle.invAction) {
			player = battle.turn;
			zone = Battle.handZone;
		} else if (battle.choosingTargetStateAction.type == Battle.dscAction) {
			player = 1 - battle.turn;
			zone = Battle.handZone;
		} else {
			throw new RuntimeException();
		}
		int target;
		if (battle.zones[player][zone].isEmpty()) {
			target = -1;
		} else {
			target = battle.zones[player][zone].get(rnd.nextInt(battle.zones[player][zone].size()));
		}
		battle.setChosenTarget(target, Battle.noneAISimulating);
	}

}
