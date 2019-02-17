package gui.battle;

import battle.Battle;
import gui.Box;

@SuppressWarnings("serial")
public class BattleMiddleBox extends Box {
	
	private BattleCardsRowBox battleCardsRowBox1;
	private BattleCardsRowBox battleCardsRowBox2;
	private BattleCardsRowBox battleCardsRowBox3;
	private BattleCardsRowBox battleCardsRowBox4;
	
	public boolean atTop;//Indicates if the rows should be printed from the top or the bottom

	public BattleMiddleBox(int x, int y, int width, int height, Box container) {
		super(x, y, width, height, container);
		battleCardsRowBox1=new BattleCardsRowBox(0, 3, width, BattleCardBox.cardHeight, this, 1, Battle.handZone, true);
		battleCardsRowBox2=new BattleCardsRowBox(0, BattleCardBox.cardHeight+5, width, BattleCardBox.cardHeight, this, 1, Battle.fieldZone, false);
		battleCardsRowBox3=new BattleCardsRowBox(0, (BattleCardBox.cardHeight+2)*2+3, width, BattleCardBox.cardHeight, this, 0, Battle.fieldZone, false);
		battleCardsRowBox4=new BattleCardsRowBox(0, (BattleCardBox.cardHeight+2)*3+3, width, BattleCardBox.cardHeight, this, 0, Battle.handZone, false);
	}
	
	@Override
	public void refresh() {
		int startY=-106;
		if(atTop) {
			startY=0;
		}
		battleCardsRowBox1.setBounds(0, startY, getWidth(), BattleCardBox.cardHeight);
		battleCardsRowBox2.setBounds(0, startY+BattleCardBox.cardHeight+2, getWidth(), BattleCardBox.cardHeight);
		battleCardsRowBox3.setBounds(0, startY+(BattleCardBox.cardHeight+2)*2, getWidth(), BattleCardBox.cardHeight);
		battleCardsRowBox4.setBounds(0, startY+(BattleCardBox.cardHeight+2)*3, getWidth(), BattleCardBox.cardHeight);
		battleCardsRowBox1.refresh();
		battleCardsRowBox2.refresh();
		battleCardsRowBox3.refresh();
		battleCardsRowBox4.refresh();
	}

}
