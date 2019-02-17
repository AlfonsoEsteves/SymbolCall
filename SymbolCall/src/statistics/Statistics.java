package statistics;

import java.util.LinkedList;

import battle.Action;
import battle.Battle;
import battle.Card;
import battle.Effect;
import loader.BossLoader;
import loader.CardLoader;
import loader.DeckLoader;

public class Statistics {
	
	public static void main(String args[]) {
		CardLoader.loadCards();
		BossLoader.loadBosses();
		DeckLoader.loadDecks();
		
		LinkedList<Card> set=Filter.filter(CardLoader.playerCards);
		
		showStatistics(set);
	}
	
	private static void showStatistics(LinkedList<Card> cards){
		System.out.println("Cards count: "+cards.size());
		symbolsStatistics(cards);
		health(cards);
	}

	private static void health(LinkedList<Card> cards) {		
		System.out.println();
		System.out.println("Amount of cards by health:");
		int[] health=new int[100];
		for(Card card : cards) {
			health[card.maxHealth]++;
		}
		for(int i=0;i<100;i++) {
			if(health[i]>0) {
				System.out.println(i+" health: "+health[i]);
			}
		}
	}

	private static void symbolsStatistics(LinkedList<Card> cards) {
		System.out.println();
		System.out.println("Needed and produced symbols:");
		int[] needed=new int[6];
		int[] producedAct=new int[6];
		int[] producedPas=new int[6];
		for(Card model : cards) {
			for(Effect effect : model.effects) {
				if(effect.sequence.isEmpty()) {
					for(Action action : effect.actions) {
						if(action.type==Battle.cllAction) {
							producedAct[action.info]++;
						}
					}
				}
				else {
					for(Integer symbol : effect.sequence) {
						needed[symbol]++;
					}
					for(Action action : effect.actions) {
						if(action.type==Battle.cllAction) {
							producedPas[action.info]++;
						}
					}
				}
			}
		}
		int totalNeeded=0;
		int totalProducedAct=0;
		int totalProducedPas=0;
		for(int i=0;i<6;i++) {
			totalNeeded+=needed[i];
			totalProducedAct+=producedAct[i];
			totalProducedPas+=producedPas[i];
			System.out.println("  "+symbolString(i)+"    => Needed: "+needed[i]+"   Added by actives: "+producedAct[i]+"   Added by passive "+producedPas[i]);
		}
		System.out.println("  total => Needed: "+totalNeeded+"   Added by actives: "+totalProducedAct+"   Added by passive "+totalProducedPas);
		System.out.println();
	}

	private static String symbolString(int symbol) {
		if(symbol==0) {
			return "rd";
		}
		else if(symbol==1) {
			return "gr";
		}
		else if(symbol==2) {
			return "bl";
		}
		else if(symbol==3) {
			return "yl";
		}
		else if(symbol==4) {
			return "pr";
		}
		else if(symbol==5) {
			return "aq";
		}
		else {
			throw new RuntimeException();
		}
	}

}
