package gui.main;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import game.Game;
import gui.Box;

@SuppressWarnings("serial")
public class MainBox extends Box {
	
	public static int sidePanelSize=220;
	public static int smallSize=38;
	public static int mediumSize=smallSize*3;
	
	public StartBattleButtonBox startBattleButtonBox;
	
	private JList<Object> list;
	
	public MainBox(int x, int y, int width, int height, Box container){
		super(x, y, width, height, container);
		startBattleButtonBox = new StartBattleButtonBox(50, 50, 100, 100, this);
		
		list = new JList<Object>();
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1);
		
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setBounds(200, 10, 120, 200);
		add(listScroller);
	}
	
	@Override
	public void enter() {
		list.setListData(Game.players.toArray());
	}
	
	@Override
	public void refresh() {
		startBattleButtonBox.refresh();
	}
	
}
