package gui;

import javax.swing.JFrame;

import game.Game;
import gui.battle.BattleBox;
import gui.main.MainBox;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private static int width = 1320;
	private static int height = 768;

	public static MainFrame instance;

	public Box currentBox;
	public MainBox mainBox;
	public BattleBox battleBox;

	public MainFrame() {
		instance = this;
		setUndecorated(true);
		setSize(width, height);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		mainBox = new MainBox(0, 0, width, height, null);
		battleBox = new BattleBox(0, 0, width, height, null);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		enterBox(mainBox);
		instance.setVisible(true);
	}

	public static void main(String[] args) {
		Game.initialize();
		new MainFrame();
	}

	public void enterBox(Box box) {
		exitCurrentBox();
		currentBox = box;
		add(currentBox);
		refresh();
	}

	public void exitCurrentBox() {
		if(currentBox != null) {
			remove(currentBox);
			currentBox = null;
		}
	}

	public void refresh() {
		currentBox.refresh();
		repaint();
	}
}