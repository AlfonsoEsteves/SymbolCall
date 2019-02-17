package gui.battle;

import gui.Box;

public class BattleOptionsBox extends Box {

	private BattleFirstButtonBox battleFirstButtonBox;
	private BattleThirdButtonBox battleThirdButtonBox;

	public BattleOptionsBox(int x, int y, int width, int height, Box container) {
		super(x, y, width, height, container);
		battleFirstButtonBox=new BattleFirstButtonBox(0, 0, width, BattleBox.smallSize, this);
		battleThirdButtonBox=new BattleThirdButtonBox(0, BattleBox.smallSize*2, width, BattleBox.smallSize, this);
	}

}
