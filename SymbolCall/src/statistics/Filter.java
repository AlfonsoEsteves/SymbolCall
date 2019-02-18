package statistics;

import java.util.LinkedList;

import battle.Action;
import battle.Battle;
import battle.Card;
import battle.Effect;
import battle.BPlayer;
import loader.DeckLoader;

public class Filter {
	
	public static int maxActions=4;
	
	public static int effectCount=4;
	
	public static int minStrong=15;
	
	public static int action1=Battle.dscAction;
	public static int action2=Battle.wdrAction;
	
	public static int symbol1=Battle.ylSymbol;
	public static int symbol2=-1;//Battle.rdSymbol;
	public static int symbol3=-1;//Battle.aqSymbol;
	
	public static LinkedList<Card> filter(LinkedList<Card> list){
		LinkedList<Card> result=new LinkedList<>();
		for(Card card : list) {
			if(hasSymbol(card)) {
				result.add(card);
			}
		}
		return result;
	}
	
	public static boolean hasEffectsCount(Card card) {
		return card.effects.size()==effectCount;
	}
	
	public static boolean isStrong(Card card) {
		return card.maxHealth>=minStrong;
	}
	
	public static boolean hasActionCount(Card card) {
		int max=0;
		for(Effect effect : card.effects) {
			if(effect.actions.size()>max) {
				max=effect.actions.size();
			}
		}
		return max==maxActions;
	}
	
	public static boolean hasAction(Card card) {
		for(Effect effect : card.effects) {
			for(Action action : effect.actions) {
				if(action.type==Filter.action1 || action.type==Filter.action2) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean hasSymbol(Card card) {
		for(Effect effect : card.effects) {
			for(Integer integer : effect.sequence) {
				if(integer==symbol1 || integer==symbol2 || integer==symbol3) {
					return true;
				}
			}
			for(Action action : effect.actions) {
				if(action.type==Battle.cllAction) {
					if(action.info==symbol1 || action.info==symbol2 || action.info==symbol3) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean hasNotInvokingPassive(Card card) {
		for(Effect effect : card.effects) {
			boolean invokes=false;
			for(Action action : effect.actions) {
				if(action.type==Battle.invAction) {
					if(action.info==Battle.slTarget) {
						invokes=true;
					}
				}
			}
			if(!invokes && !effect.sequence.isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isTrap(Card card) {
		for(Effect effect : card.effects) {
			for(Action action : effect.actions) {
				if(action.type==Battle.atkAction || action.type==Battle.wdrAction || action.type==Battle.dscAction) {
					if(action.info==Battle.trTarget) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean isChain(Card card) {
		for(Effect effect : card.effects) {
			if(!effect.sequence.isEmpty()) {
				for(Action action : effect.actions) {
					if(action.type==Battle.cllAction) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean multipleChooseAttacks(Card card) {
		for(Effect effect : card.effects) {
			int count=0;
			for(Action action : effect.actions) {
				if(action.type==Battle.atkAction && action.info==Battle.chTarget) {
					count++;
				}
			}
			if(count>1) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean wanted(Card card) {
		boolean unwanted=false;
		for(BPlayer player : DeckLoader.decks) {
			for(Card card2 : player.deck) {
				if(card2.name.equals(card.name)) {
					unwanted=true;
				}
			}
		}
		return unwanted;
	}
	
	public static boolean none(Card card) {
		return true;
	}

}