package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public abstract class AbstractButtonBox extends Box {
	
	private String text;

	public AbstractButtonBox(String text, int x, int y, int width, int height, Box container) {
		super(x, y, width, height, container);
		this.text = text;
	}

	@Override
	public abstract void mousePressed(MouseEvent arg0);
	
	@Override
	public void paint(Graphics graphics) {
		graphics.setColor(Color.orange);
		graphics.fillRect(0, 0, getWidth()-1, getHeight()-1);
		graphics.setColor(Color.black);
		graphics.drawString(text, 0, getHeight() / 2);
	}

}
