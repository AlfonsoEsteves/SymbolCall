package gui.battle;

import gui.Box;

public class BattleRightBox extends Box {
	
	private BattleHealthBox battleHealthBox;
	private BattleLogBox battleLogBox;
	private BattleOptionsBox battleOptionsBox;

	public BattleRightBox(int x, int y, int width, int height) {
		super(x, y, width, height);
		battleHealthBox=new BattleHealthBox(0, 0, width, BattleBox.mediumSize);
		battleLogBox=new BattleLogBox(0, BattleBox.mediumSize, width, height-BattleBox.mediumSize*2);
		battleOptionsBox=new BattleOptionsBox(0, height-BattleBox.mediumSize, width, BattleBox.mediumSize);	
	}

}
