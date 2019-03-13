package gui.deckbuilding;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import battle.Card;
import game.Game;
import gui.Box;
import gui.MainFrame;

@SuppressWarnings("serial")
public class SwitchCardsButtonBox extends Box {

	public SwitchCardsButtonBox(int x, int y, int width, int height, Box container) {
		super(x, y, width, height, container);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		Card deckCard = (Card)DeckBuildingBox.deck.getSelectedValue();
		Card inventoryCard = (Card)DeckBuildingBox.inventory.getSelectedValue();
		Game.humanPlayer.deck.remove(deckCard);
		Game.humanPlayer.inventory.add(deckCard);
		Game.humanPlayer.inventory.remove(inventoryCard);
		Game.humanPlayer.deck.add(inventoryCard);
		MainFrame.instance.refresh();
	}

	@Override
	public void paint(Graphics graphics) {
		graphics.setColor(Color.orange);
		graphics.fillRect(0, 0, getWidth()-1, getHeight()-1);
		paintChildren(graphics);
	}

}
