package gui.deckbuilding;

import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import battle.BCard;
import battle.Card;
import game.Game;
import gui.Box;
import gui.BoxButton;
import gui.MainFrame;
import gui.battle.BattleCardBox;

@SuppressWarnings("serial")
public class DeckBuildingBox12345 extends Box {

	public JList<Object> deck;
	public JList<Object> inventory;
	public BattleCardBox battleCardBoxInventory;
	public BattleCardBox battleCardBoxDeck;

	public DeckBuildingBox12345(int x, int y, int width, int height) {
		super(x, y, width, height);

		deck = new JList<Object>();
		deck.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		deck.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		deck.setVisibleRowCount(-1);

		ListSelectionListener deckSelectionListener = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent listSelectionEvent) {
				refreshDeckCard();
			}
		};
		deck.addListSelectionListener(deckSelectionListener);

		JScrollPane deckScroller = new JScrollPane(deck);
		deckScroller.setBounds(400, 10, 120, 200);
		add(deckScroller);

		inventory = new JList<Object>();
		inventory.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		inventory.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		inventory.setVisibleRowCount(-1);

		ListSelectionListener inventorySelectionListener = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent listSelectionEvent) {
				refreshInventoryCard();
			}
		};
		inventory.addListSelectionListener(inventorySelectionListener);

		JScrollPane inventoryScroller = new JScrollPane(inventory);
		inventoryScroller.setBounds(250, 10, 120, 200);
		add(inventoryScroller);

		BoxButton switchCardsButton = new BoxButton(300, 300, 100, 50) {
			@Override
			public void mousePressed(MouseEvent arg0) {
				Card deckCard = (Card) deck.getSelectedValue();
				Card inventoryCard = (Card) inventory.getSelectedValue();
				if (deckCard != null && inventoryCard != null) {
					Game.instance.humanPlayer.deck.remove(deckCard);
					Game.instance.humanPlayer.inventory.add(deckCard);
					Game.instance.humanPlayer.inventory.remove(inventoryCard);
					Game.instance.humanPlayer.deck.add(inventoryCard);
					MainFrame.instance.refresh();
				}
			}

			@Override
			public String getText() {
				return "Switch Cards";
			}
		};
		add(switchCardsButton);

		BoxButton goBackButton = new BoxButton(100, 600, 100, 50) {
			@Override
			public void mousePressed(MouseEvent arg0) {
				MainFrame.instance.enterBox(MainFrame.instance.mainBox);
			}

			@Override
			public String getText() {
				return "Go Back";
			}
		};
		add(goBackButton);

		battleCardBoxInventory = new BattleCardBox();
		battleCardBoxInventory.setBounds(30, 30, BattleCardBox.cardWidth, BattleCardBox.cardHeight);
		add(battleCardBoxInventory);

		battleCardBoxDeck = new BattleCardBox();
		battleCardBoxDeck.setBounds(530, 30, BattleCardBox.cardWidth, BattleCardBox.cardHeight);
		add(battleCardBoxDeck);
	}

	@Override
	public void refresh() {
		deck.setListData(Game.instance.humanPlayer.deck.toArray());
		inventory.setListData(Game.instance.humanPlayer.inventory.toArray());

		refreshInventoryCard();
		refreshDeckCard();
	}

	private void refreshInventoryCard() {
		Card inventoryCard = (Card) inventory.getSelectedValue();
		if (inventoryCard != null) {
			battleCardBoxInventory.card = new BCard(inventoryCard);
			battleCardBoxInventory.refresh();
		}
	}

	private void refreshDeckCard() {
		Card deckCard = (Card) deck.getSelectedValue();
		if (deckCard != null) {
			battleCardBoxDeck.card = new BCard(deckCard);
			battleCardBoxDeck.refresh();
		}
	}

}
