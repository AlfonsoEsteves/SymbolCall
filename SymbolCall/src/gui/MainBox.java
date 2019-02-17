package gui;

import gui.battle.BattleBox;

@SuppressWarnings("serial")
public class MainBox extends Box{
	
	public BattleBox battleBox;

	public MainBox(int width, int height){
		super(0, 0, width, height, null);
		battleBox=new BattleBox(0, 0, width, height, this);
	}
	
	@Override
	public void refresh() {
		battleBox.refresh();
	}

}
