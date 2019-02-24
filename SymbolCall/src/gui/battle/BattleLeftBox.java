package gui.battle;

import gui.Box;

@SuppressWarnings("serial")
public class BattleLeftBox extends Box {
	
	private BattleDescriptionBox battleDescriptionBox;
	private BattleSymbolsBox battleSymbolsBox;
	private BattleStackBox battleStackBox;

	public BattleLeftBox(int x, int y, int width, int height, Box container) {
		super(x, y, width, height, container);
		battleDescriptionBox=new BattleDescriptionBox(0, 0, width, BattleBox.mediumSize, this);
		battleSymbolsBox=new BattleSymbolsBox(0, BattleBox.mediumSize, width, BattleBox.smallSize, this);
		battleStackBox=new BattleStackBox(0, BattleBox.mediumSize+BattleBox.smallSize, width, height-(BattleBox.mediumSize+BattleBox.smallSize), this);
	}

}
