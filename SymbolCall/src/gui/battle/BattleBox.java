package gui.battle;

import gui.Box;

@SuppressWarnings("serial")
public class BattleBox extends Box {
	
	public static int sidePanelSize=220;
	public static int smallSize=38;
	public static int mediumSize=smallSize*3;
	
	public BattleLeftBox battleLeftBox;
	public BattleMiddleBox battleMiddleBox;
	public BattleRightBox battleRightBox;
	
	public BattleBox(int x, int y, int width, int height, Box container){
		super(x, y, width, height, container);
		battleLeftBox=new BattleLeftBox(0, 0, sidePanelSize, height, this);
		battleMiddleBox=new BattleMiddleBox(sidePanelSize, 0, width-sidePanelSize*2, height, this);
		battleRightBox=new BattleRightBox(width-sidePanelSize, 0, sidePanelSize, height, this);
	}
	
	@Override
	public void refresh() {
		battleMiddleBox.refresh();
	}
	
}
