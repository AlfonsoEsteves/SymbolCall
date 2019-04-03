package gui.battle;

import gui.Box;

@SuppressWarnings("serial")
public class BattleLeftBox extends Box {

	public BattleLeftBox(int x, int y, int width, int height) {
		super(x, y, width, height);
		add(new BattleDescriptionBox(0, 0, width, BattleBox.mediumSize));
		add(new BattleSymbolsBox(0, BattleBox.mediumSize, width, BattleBox.smallSize));
		add(new BattleStackBox(0, BattleBox.mediumSize+BattleBox.smallSize, width, height-(BattleBox.mediumSize+BattleBox.smallSize)));
	}

}
