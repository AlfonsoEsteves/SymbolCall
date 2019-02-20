package gui.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import game.Game;
import gui.Box;
import gui.MainFrame;

@SuppressWarnings("serial")
public class StartBattleButtonBox extends Box {

	public StartBattleButtonBox(int x, int y, int width, int height, Box container) {
		super(x, y, width, height, container);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		Game.initializeHumanPlayerBattle();
		MainFrame.instance.changeToBattleBox();
	}

	@Override
	public void paint(Graphics graphics) {
		graphics.setColor(Color.orange);
		graphics.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
	}

}
