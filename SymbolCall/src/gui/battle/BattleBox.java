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
	
	public BattleBox(int x, int y, int width, int height){
		super(x, y, width, height);
		battleLeftBox=new BattleLeftBox(0, 0, sidePanelSize, height);
		battleMiddleBox=new BattleMiddleBox(sidePanelSize, 0, width-sidePanelSize*2, height);
		battleRightBox=new BattleRightBox(width-sidePanelSize, 0, sidePanelSize, height);
	}
	
	@Override
	public void refresh() {
		battleMiddleBox.refresh();
	}
	
}
