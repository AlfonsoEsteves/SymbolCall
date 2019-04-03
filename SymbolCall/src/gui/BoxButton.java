package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public abstract class BoxButton extends Box {

	public BoxButton(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public abstract void mousePressed(MouseEvent arg0);

	public abstract String getText();
	
	@Override
	public void paint(Graphics graphics) {
		graphics.setColor(Color.orange);
		graphics.fillRect(0, 0, getWidth()-1, getHeight()-1);
		graphics.setColor(Color.black);
		graphics.drawString(getText(), 0, getHeight() / 2);
	}

}
