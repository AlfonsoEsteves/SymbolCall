package gui.battle;

import gui.Box;

@SuppressWarnings("serial")
public class BattleBox extends Box {
	
	public static int sidePanelSize=220;
	public static int smallSize=38;
	public static int mediumSize=smallSize*3;
	
	public BattleBox(int x, int y, int width, int height){
		super(x, y, width, height);
		add(new BattleLeftBox(0, 0, sidePanelSize, height));
		add(new BattleMiddleBox(sidePanelSize, 0, width-sidePanelSize*2, height));
		add(new BattleRightBox(width-sidePanelSize, 0, sidePanelSize, height));
	}
	
}
