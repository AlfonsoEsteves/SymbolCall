package statistics;

import java.util.List;

import battle.BCard;
import battle.Card;
import game.Game;
import gui.Box;
import gui.battle.BattleCardBox;
import loader.DeckLoader;

@SuppressWarnings("serial")
public class StatisticsBox extends Box{

	public StatisticsBox(int width, int height){
		super(0, 0, width, height);
		int count=0;
		List<Card> from=Filter.filter(Game.instance.playerCards);
		List<Card> cards=Filter.filter(from);
		//List<Card> cards=DeckLoader.decks.get("White_dragon");
		for(Card card : cards) {
			BCard bcard=new BCard(card);
			BattleCardBox battleCardBox = new BattleCardBox();
			battleCardBox.card = bcard;
			battleCardBox.refresh();
			add(battleCardBox);
			battleCardBox.setBounds((count%10)*135-5, (count/10)*(StatisticsCardBox.cardHeight) - 7, StatisticsCardBox.cardWidth, StatisticsCardBox.cardHeight);
			//StatisticsCardBox statisticsCardBox=new StatisticsCardBox((count%10)*135-5, (count/10)*(StatisticsCardBox.cardHeight), StatisticsCardBox.cardWidth, StatisticsCardBox.cardHeight, bcard);
			//add(statisticsCardBox);
			count++;
		}
	}
	
}
