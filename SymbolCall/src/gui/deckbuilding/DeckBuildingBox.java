package gui.deckbuilding;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import game.Game;
import gui.Box;

@SuppressWarnings("serial")
public class DeckBuildingBox extends Box {
	
	public static JList<Object> deck;
	public static JList<Object> inventory;
	
	public DeckBuildingBox(int x, int y, int width, int height, Box container){
		super(x, y, width, height, container);
		
		deck = new JList<Object>();
		deck.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		deck.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		deck.setVisibleRowCount(-1);
		
		JScrollPane deckScroller = new JScrollPane(deck);
		deckScroller.setBounds(200, 10, 120, 200);
		add(deckScroller);
		
		inventory = new JList<Object>();
		inventory.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		inventory.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		inventory.setVisibleRowCount(-1);
		
		JScrollPane inventoryScroller = new JScrollPane(inventory);
		inventoryScroller.setBounds(50, 10, 120, 200);
		add(inventoryScroller);
		
		SwitchCardsButtonBox switchCardsButtonBox = new SwitchCardsButtonBox(100, 300, 100, 50, null);
		add(switchCardsButtonBox);
	}
	
	@Override
	public void refresh() {
		deck.setListData(Game.humanPlayer.deck.toArray());
		inventory.setListData(Game.humanPlayer.inventory.toArray());
	}
	
}
