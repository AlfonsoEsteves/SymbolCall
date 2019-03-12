package gui.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import game.ThreadManager;
import gui.Box;
import gui.MainFrame;

@SuppressWarnings("serial")
public class StartBattleButtonBox extends Box {

	public StartBattleButtonBox(int x, int y, int width, int height, Box container) {
		super(x, y, width, height, container);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		synchronized(ThreadManager.instance) {
			ThreadManager.instance.notify();
		}
		try {
			synchronized(MainFrame.instance) {
				// This is meant to be notified by the BattleExecutorManual
				// when the battle has been initialized
				MainFrame.instance.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		MainFrame.instance.enterBox(MainFrame.instance.battleBox);
	}

	@Override
	public void paint(Graphics graphics) {
		graphics.setColor(Color.orange);
		graphics.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
	}

}
