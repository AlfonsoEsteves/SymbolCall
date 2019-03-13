package gui.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import game.ThreadManager;
import gui.Box;
import gui.MainFrame;

@SuppressWarnings("serial")
public class DeckBuildingButtonBox extends Box {

	public DeckBuildingButtonBox(int x, int y, int width, int height, Box container) {
		super(x, y, width, height, container);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		MainFrame.instance.enterBox(MainFrame.instance.deckBuildingBox);
	}

	@Override
	public void paint(Graphics graphics) {
		graphics.setColor(Color.orange);
		graphics.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
	}

}
