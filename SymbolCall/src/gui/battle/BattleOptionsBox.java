package gui.battle;

import gui.Box;

@SuppressWarnings("serial")
public class BattleOptionsBox extends Box {

	public BattleOptionsBox(int x, int y, int width, int height) {
		super(x, y, width, height);
		
		
		add(new BattleFirstButtonBox(0, 0, width, BattleBox.smallSize));
		add(new BattleThirdButtonBox(0, BattleBox.smallSize*2, width, BattleBox.smallSize));
	}

}
