package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

@SuppressWarnings("serial")
public abstract class Box extends JPanel implements MouseInputListener, KeyListener {

	public Box container;

	public Box(int x, int y, int width, int height, Box container) {
		setBounds(x, y, width, height);
		setLayout(null);

		this.container = container;
		if (container != null) {
			container.add(this);
		}

		// Mouse listeners need to be added to themselves to have the correct mouse
		// position values
		addMouseListener(this);

		// Key listeners need to be added to the main frame, because they need to be in
		// a focused component
		MainFrame.instance.addKeyListener(this);
	}

	public void refresh() {
	}

	@Override
	public void paint(Graphics graphics) {
		graphics.setColor(Color.RED);
		graphics.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		paintChildren(graphics);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (container != null) {
			container.mouseEntered(e);
		}
	}

	@Override
	public final void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
