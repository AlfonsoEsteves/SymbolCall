package statistics;

import java.util.List;

import battle.BCard;
import battle.Card;
import gui.Box;
import loader.DeckLoader;

@SuppressWarnings("serial")
public class StatisticsBox extends Box{

	public StatisticsBox(int width, int height){
		super(0, 0, width, height);
		int count=0;
		List<Card> from=DeckLoader.decks.get("White_dragon");
		//LinkedList<Card> from=Game.instance.playerCards;
		List<Card> cards=Filter.filter(from);
		for(Card card : cards) {
			BCard bcard=new BCard(card);
			StatisticsCardBox statisticsCardBox=new StatisticsCardBox((count%10)*135-5, (count/10)*(StatisticsCardBox.cardHeight), StatisticsCardBox.cardWidth, StatisticsCardBox.cardHeight, bcard);
			add(statisticsCardBox);
			count++;
		}
	}
	
	

}
