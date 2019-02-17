package randomAI;

import battle.BCard;
import battle.Battle;
import battle.ComputerAI;
import battle.Effect;
import battle.Rnd;

public class RandomAI extends ComputerAI{

	@Override
	public void play(Battle battle) {
		if(Rnd.nextInt(2)==0) {
			if(!playFromZone(battle, Battle.handZone)) {
				if(!playFromZone(battle, Battle.fieldZone)) {
					battle.passTurn();
				}
			}
		}
		else {
			if(!playFromZone(battle, Battle.fieldZone)) {
				if(!playFromZone(battle, Battle.handZone)) {
					battle.passTurn();
				}
			}
		}
	}

	private boolean playFromZone(Battle battle, int zone) {
		if(!battle.zones[battle.turn][zone].isEmpty()) {
			int r1=Rnd.nextInt(battle.zones[battle.turn][zone].size());
			for(int i=0;i<battle.zones[battle.turn][zone].size();i++) {
				int c=battle.zones[battle.turn][zone].get((r1+i)%battle.zones[battle.turn][zone].size());
				BCard card=battle.cards[c];
				if(card.turn) {
					int r2=Rnd.nextInt(card.model.effects.size());
					for(int j=0;j<card.model.effects.size();j++) {
						int effectNumber=(r2+j)%card.model.effects.size();
						Effect effect=card.model.effects.get(effectNumber);
						if(card.turn && effect.zone==card.zone && effect.sequence.isEmpty()) {
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
		if(battle.choosingTargetStateAction.type==Battle.atkAction) {
			player=1-battle.decidingPlayer;
			zone=Battle.fieldZone;
		}
		else if(battle.choosingTargetStateAction.type==Battle.wdrAction) {
			player=1-battle.decidingPlayer;
			zone=Battle.fieldZone;
		}
		else if(battle.choosingTargetStateAction.type==Battle.invAction) {
			player=battle.decidingPlayer;
			zone=Battle.handZone;
		}
		else if(battle.choosingTargetStateAction.type==Battle.dscAction) {
			player=1-battle.decidingPlayer;
			zone=Battle.handZone;
		}
		else {
			throw new RuntimeException();
		}
		int target;
		if(battle.zones[player][zone].isEmpty()) {
			target=-1;
		}
		else {
			target=battle.zones[player][zone].get(Rnd.nextInt(battle.zones[player][zone].size()));
		}
		battle.setChosenTarget(target);
	}

}
