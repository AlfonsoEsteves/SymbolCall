package gui;

import java.awt.Dimension;

import javax.swing.JFrame;

import game.Game;
import game.ThreadManager;
import gui.battle.BattleBox;
import gui.deckbuilding.DeckBuildingBox;
import gui.main.MainBox;
import persistence.Persistence;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private static int width = 1320;
	private static int height = 768;

	public static MainFrame instance;

	public Box currentBox;
	public MainBox mainBox;
	public BattleBox battleBox;
	public DeckBuildingBox deckBuildingBox;

	public void initialize() {
		setUndecorated(true);
		setSize(width, height);
		setLayout(null);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		mainBox = new MainBox(0, 0, width, height, null);
		battleBox = new BattleBox(0, 0, width, height, null);
		deckBuildingBox = new DeckBuildingBox(0, 0, width, height, null);
		setMinimumSize(new Dimension(width, height));
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		enterBox(mainBox);
		setVisible(true);
	}

	public static void main(String[] args) {
		Game.ins.initialize();
		instance = new MainFrame();
		instance.initialize();
		
		//This will run forever
		ThreadManager.ins.runGame();
	}

	public void enterBox(Box box) {
		exitCurrentBox();
		currentBox = box;
		add(currentBox);
        setFocusable(true);
        requestFocusInWindow();
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