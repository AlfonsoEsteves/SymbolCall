package gui;

import java.awt.Dimension;

import javax.swing.JFrame;

import gui.battle.BattleBox;
import gui.deckbuilding.DeckBuildingBox;
import gui.main.MainBox;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private static int width = 1320;
	private static int height = 768;

	public static MainFrame instance;

	public Box currentBox;
	public MainBox mainBox;
	public BattleBox battleBox;
	public DeckBuildingBox deckBuildingBox;

	public static void instantiate() {
		instance = new MainFrame();
		instance.initialize();
	}

	private void initialize() {
		setUndecorated(true);
		setSize(width, height);
		setLayout(null);
		setLocationRelativeTo(null);
		mainBox = new MainBox(0, 0, width, height);
		battleBox = new BattleBox(0, 0, width, height);
		deckBuildingBox = new DeckBuildingBox(0, 0, width, height);
		setMinimumSize(new Dimension(width, height));
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
	}

	public void enterBox(Box box) {
		exitCurrentBox();
		currentBox = box;
		refresh();
	}

	public void exitCurrentBox() {
		if(currentBox != null) {
			remove(currentBox);
			currentBox = null;
		}
	}
	
	public void refresh() {
		remove(currentBox);
		currentBox.refresh();
		add(currentBox);
		validate();
		repaint();
	}
}