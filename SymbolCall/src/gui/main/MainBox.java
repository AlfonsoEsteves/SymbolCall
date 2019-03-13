package gui.main;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import game.Game;
import gui.Box;

@SuppressWarnings("serial")
public class MainBox extends Box {
	
	public StartBattleButtonBox startBattleButtonBox;
	public DeckBuildingButtonBox deckBuildingButtonBox;
	
	private JList<Object> list;
	
	public MainBox(int x, int y, int width, int height, Box container){
		super(x, y, width, height, container);
		startBattleButtonBox = new StartBattleButtonBox(50, 50, 100, 100, this);
		deckBuildingButtonBox = new DeckBuildingButtonBox(120, 250, 100, 100, this);
		
		list = new JList<Object>();
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1);
		
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setBounds(300, 10, 120, 200);
		add(listScroller);
	}
	
	@Override
	public void refresh() {
		list.setListData(Game.players.toArray());
		//startBattleButtonBox.refresh();
	}
	
}
