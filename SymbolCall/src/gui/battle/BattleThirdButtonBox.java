package gui.battle;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import gui.Box;

@SuppressWarnings("serial")
public class BattleThirdButtonBox extends Box {

	public BattleThirdButtonBox(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		System.out.println("surrender");
	}

	@Override
	public void paint(Graphics graphics) {
		graphics.setColor(Color.orange);
		graphics.fillRect(0, 0, getWidth()-1, getHeight()-1);
		paintChildren(graphics);
	}

}
