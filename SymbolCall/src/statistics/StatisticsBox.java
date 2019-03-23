package statistics;

import java.util.LinkedList;

import battle.BCard;
import battle.Card;
import game.Game;
import gui.Box;
import loader.CardLoader;

@SuppressWarnings("serial")
public class StatisticsBox extends Box{

	public StatisticsBox(int width, int height){
		super(0, 0, width, height, null);
		int count=0;
		//LinkedList<Card> from=DeckLoader.getPlayer("White dragon");
		LinkedList<Card> from=Game.ins.playerCards;
		LinkedList<Card> cards=Filter.filter(from);
		for(Card card : cards) {
			BCard bcard=new BCard(card);
			StatisticsCardBox statisticsCardBox=new StatisticsCardBox((count%11)*125-5, (count/11)*(StatisticsCardBox.cardHeight), StatisticsCardBox.cardWidth, StatisticsCardBox.cardHeight, null, bcard);
			add(statisticsCardBox);
			count++;
		}
	}
	
	

}
