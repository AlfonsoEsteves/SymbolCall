package gui.battle;

import java.awt.Component;

import battle.Battle;
import gui.Box;

@SuppressWarnings("serial")
public class BattleMiddleBox extends Box {

	public boolean atTop;// Indicates if the rows should be printed from the top or the bottom

	public BattleMiddleBox(int x, int y, int width, int height) {
		super(x, y, width, height);
		add(new BattleCardsRowBox(0, 3, width, BattleCardBox.cardHeight, 1, Battle.handZone, true));
		add(new BattleCardsRowBox(0, BattleCardBox.cardHeight + 5, width, BattleCardBox.cardHeight, 1, Battle.fieldZone, false));
		add(new BattleCardsRowBox(0, (BattleCardBox.cardHeight + 2) * 2 + 3, width, BattleCardBox.cardHeight, 0, Battle.fieldZone, false));
		add(new BattleCardsRowBox(0, (BattleCardBox.cardHeight + 2) * 3 + 3, width, BattleCardBox.cardHeight, 0, Battle.handZone, false));
	}

	@Override
	public void refresh() {
		int y = -106;
		if (atTop) {
			y = 0;
		}

		for (Component component : getComponents()) {
			BattleCardsRowBox battleCardsRowBox = (BattleCardsRowBox) component;
			battleCardsRowBox.setBounds(0, y, getWidth(), BattleCardBox.cardHeight);
			battleCardsRowBox.refresh();
			y += BattleCardBox.cardHeight + 2;
		}
	}

}
