package gui.battle;

import gui.Box;

@SuppressWarnings("serial")
public class BattleRightBox extends Box {

	public BattleRightBox(int x, int y, int width, int height) {
		super(x, y, width, height);
		add(new BattleHealthBox(0, 0, width, BattleBox.mediumSize));
		add(new BattleLogBox(0, BattleBox.mediumSize, width, height-BattleBox.mediumSize*2));
		add(new BattleOptionsBox(0, height-BattleBox.mediumSize, width, BattleBox.mediumSize));	
	}

}
