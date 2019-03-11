package game;

import battle.Battle;
import battle.Rnd;
import gui.MainFrame;

public class BattleExecutorManual {
	
	public static BattleExecutorManual instance = new BattleExecutorManual();
	
	public synchronized Battle executeBattleOfHumanPlayer() {
		
		// Find the player rival
		int index = Game.players.indexOf(Game.humanPlayer);
		if(index % 2 == 0) {
			index++;
		}
		else {
			index--;
		}
		Player rival = Game.players.get(index);
		
		// Initialize battle
		Game.battle = new Battle(Game.humanPlayer, rival, Rnd.nextInt(2));
		
		// The GUI is notified that it can go on with the battle
		synchronized(MainFrame.instance) {
			MainFrame.instance.notify();
		}
		
		// The GUI should notify when the battle is over
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return Game.battle;
	}
	
}
