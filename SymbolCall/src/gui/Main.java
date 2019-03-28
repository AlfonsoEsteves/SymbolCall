package gui;

import game.Game;
import game.ThreadManager;

public class Main {

	public static void main(String[] args) {
		Game.instantiate();
		Game.instance.initializeCircumstances();
		MainFrame.instantiate();
		MainFrame.instance.enterBox(MainFrame.instance.mainBox);
		
		//This will run forever
		ThreadManager.instance.runGame();
	}

}
