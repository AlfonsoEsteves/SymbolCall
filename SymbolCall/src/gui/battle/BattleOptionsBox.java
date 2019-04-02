package gui.battle;

import gui.Box;

@SuppressWarnings("serial")
public class BattleOptionsBox extends Box {

	private BattleFirstButtonBox battleFirstButtonBox;
	private BattleThirdButtonBox battleThirdButtonBox;

	public BattleOptionsBox(int x, int y, int width, int height) {
		super(x, y, width, height);
		battleFirstButtonBox=new BattleFirstButtonBox(0, 0, width, BattleBox.smallSize);
		battleThirdButtonBox=new BattleThirdButtonBox(0, BattleBox.smallSize*2, width, BattleBox.smallSize);
	}

}
