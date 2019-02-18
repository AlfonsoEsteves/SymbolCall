package gui.main;

import gui.Box;

@SuppressWarnings("serial")
public class MainBox extends Box {
	
	public static int sidePanelSize=220;
	public static int smallSize=38;
	public static int mediumSize=smallSize*3;
	
	public StartBattleButtonBox startBattleButtonBox;
	
	public MainBox(int x, int y, int width, int height, Box container){
		super(x, y, width, height, container);
		startBattleButtonBox = new StartBattleButtonBox(50, 50, 100, 100, this);
	}
	
	@Override
	public void refresh() {
		startBattleButtonBox.refresh();
	}
	
}
