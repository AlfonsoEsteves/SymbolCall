package gui.deckbuilding;

import java.awt.event.MouseEvent;
import java.util.List;

import battle.BCard;
import battle.Card;
import game.Game;
import gui.Box;
import gui.BoxButton;
import gui.MainFrame;
import gui.battle.BattleCardBox;

@SuppressWarnings("serial")
public class DeckBuildingBox extends Box {
	
	public static final int verticalCardCount = 3;
	public static final int horizontalCardCount = 9;
	
	public static final int cardWidth = 135;
	public static final int cardHeight = 170;
	
	public static DeckBuildingBox instance;
	
	public BattleCardBox selectedForExchange;
	
	public int position = 0;

	public BattleCardBox[][] battleCardBoxesInventory;
	public BattleCardBox[] battleCardBoxesDeck;

	public DeckBuildingBox(int x, int y, int width, int height) {
		super(x, y, width, height);
		
		battleCardBoxesInventory = new BattleCardBox[horizontalCardCount][verticalCardCount];
		for(int i = 0;i<horizontalCardCount;i++) {
			for(int j = 0;j<verticalCardCount;j++) {
				battleCardBoxesInventory[i][j] = new BattleCardBox();
				battleCardBoxesInventory[i][j].setBounds(80 + i * cardWidth, 220 + j * cardHeight, BattleCardBox.cardWidth, BattleCardBox.cardHeight);
				add(battleCardBoxesInventory[i][j]);
			}
		}
		
		battleCardBoxesDeck = new BattleCardBox[horizontalCardCount];
		for(int i = 0;i<horizontalCardCount;i++) {
			battleCardBoxesDeck[i] = new BattleCardBox();
			battleCardBoxesDeck[i].setBounds(80 + i * cardWidth, 0, BattleCardBox.cardWidth, BattleCardBox.cardHeight);
			add(battleCardBoxesDeck[i]);
		}

		BoxButton goBackButton = new BoxButton(10, 700, 60, 50) {
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
		
		BoxButton leftButton = new BoxButton(0, 0, 60, 700) {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if(DeckBuildingBox.instance.position > 0) {
					DeckBuildingBox.instance.position --;
					DeckBuildingBox.instance.refresh();
				}
			}

			@Override
			public String getText() {
				return "Left";
			}
		};
		add(leftButton);
		
		BoxButton rightButton = new BoxButton(1300, 0, 60, 700) {
			@Override
			public void mousePressed(MouseEvent arg0) {
				DeckBuildingBox.instance.position ++;
				DeckBuildingBox.instance.refresh();
			}

			@Override
			public String getText() {
				return "Right";
			}
		};
		add(rightButton);
		
		refresh();
		
		instance = this;
	}

	@Override
	public void refresh() {
		for(int j = 0;j<verticalCardCount;j++) {
			for(int i = 0;i<horizontalCardCount;i++) {
				int index = j * horizontalCardCount + i;
				if(index < Game.instance.humanPlayer.inventory.size()) {
					Card inventoryCard = Game.instance.humanPlayer.inventory.get(index);
					battleCardBoxesInventory[i][j].card = new BCard(inventoryCard);
					battleCardBoxesInventory[i][j].refresh();
					battleCardBoxesInventory[i][j].repaint();
					battleCardBoxesInventory[i][j].setVisible(true);
				}
				else {
					battleCardBoxesInventory[i][j].card = null;
					battleCardBoxesInventory[i][j].setVisible(false);
				}
			}
		}
		for(int j = 0;j<horizontalCardCount;j++) {
			if(j + position < Game.instance.humanPlayer.deck.size()) {
				Card inventoryCard = Game.instance.humanPlayer.deck.get(j + position);
				battleCardBoxesDeck[j].card = new BCard(inventoryCard);
				battleCardBoxesDeck[j].refresh();
				battleCardBoxesDeck[j].repaint();
				battleCardBoxesDeck[j].setVisible(true);
			}
			else {
				battleCardBoxesDeck[j].card = null;
				battleCardBoxesDeck[j].setVisible(false);
			}
		}
	}
	
	public void clickedCard(BattleCardBox battleCardBox) {
		if(selectedForExchange == null) {
			selectedForExchange = battleCardBox;
		}
		else {
			List<Card> list1;
			List<Card> list2;
			if(Game.instance.humanPlayer.deck.contains(selectedForExchange.card.model)
					&& Game.instance.humanPlayer.inventory.contains(battleCardBox.card.model)) {
				list1 = Game.instance.humanPlayer.deck;
				list2 = Game.instance.humanPlayer.inventory;
			}
			else if(Game.instance.humanPlayer.inventory.contains(selectedForExchange.card.model)
					&& Game.instance.humanPlayer.deck.contains(battleCardBox.card.model)) {
				list1 = Game.instance.humanPlayer.inventory;
				list2 = Game.instance.humanPlayer.deck;
			}
			else {
				selectedForExchange = battleCardBox;
				return;
			}
			list1.remove(selectedForExchange.card.model);
			list2.add(selectedForExchange.card.model);
			list2.remove(battleCardBox.card.model);
			list1.add(battleCardBox.card.model);
			selectedForExchange = null;
			refresh();
		}
	}
}
