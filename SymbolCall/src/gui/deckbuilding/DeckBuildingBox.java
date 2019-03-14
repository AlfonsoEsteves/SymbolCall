package gui.deckbuilding;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import battle.Card;
import game.Game;
import gui.AbstractButtonBox;
import gui.Box;
import gui.MainFrame;
import gui.main.MainBox;

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
		
		//SwitchCardsButtonBox switchCardsButtonBox = new SwitchCardsButtonBox(100, 300, 100, 50, null);
		//add(switchCardsButtonBox);
		
		AbstractButtonBox switchCardsButton = new AbstractButtonBox("Switch Cards", 100, 300, 100, 50, null) {
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				Card deckCard = (Card)deck.getSelectedValue();
				Card inventoryCard = (Card)inventory.getSelectedValue();
				Game.humanPlayer.deck.remove(deckCard);
				Game.humanPlayer.inventory.add(deckCard);
				Game.humanPlayer.inventory.remove(inventoryCard);
				Game.humanPlayer.deck.add(inventoryCard);
				MainFrame.instance.refresh();
			}
		};
		add(switchCardsButton);
		
		AbstractButtonBox goBackButton = new AbstractButtonBox("Go Back", 600, 200, 100, 50, null) {
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				MainFrame.instance.enterBox(MainFrame.instance.mainBox);
			}
		};
		add(goBackButton);
	}
	
	@Override
	public void refresh() {
		deck.setListData(Game.humanPlayer.deck.toArray());
		inventory.setListData(Game.humanPlayer.inventory.toArray());
	}

	@Override
	public void paint(Graphics graphics) {
		graphics.setColor(Color.black);
		graphics.drawString("Gold: " + Game.humanPlayer.gold, 400, 50);
		paintChildren(graphics);
	}
	
}
