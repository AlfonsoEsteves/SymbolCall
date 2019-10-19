package gui.deckbuilding;

import java.awt.event.MouseEvent;

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
		
		refresh();
	}

	@Override
	public void refresh() {
		for(int j = 0;j<verticalCardCount;j++) {
			for(int i = 0;i<horizontalCardCount;i++) {
				int index = j*horizontalCardCount + i;
				if(index < Game.instance.humanPlayer.inventory.size()) {
					Card inventoryCard = Game.instance.humanPlayer.inventory.get(index);
					battleCardBoxesInventory[i][j].card = new BCard(inventoryCard);
					battleCardBoxesInventory[i][j].refresh();
					battleCardBoxesInventory[i][j].setVisible(true);
				}
				else {
					battleCardBoxesInventory[i][j].card = null;
					battleCardBoxesInventory[i][j].setVisible(false);
				}
			}
		}
		for(int j = 0;j<horizontalCardCount;j++) {
			if(j < Game.instance.humanPlayer.deck.size()) {
				Card inventoryCard = Game.instance.humanPlayer.deck.get(j);
				battleCardBoxesDeck[j].card = new BCard(inventoryCard);
				battleCardBoxesDeck[j].refresh();
				battleCardBoxesDeck[j].setVisible(true);
			}
			else {
				battleCardBoxesDeck[j].card = null;
				battleCardBoxesDeck[j].setVisible(false);
			}
		}
	}
}
