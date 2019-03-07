package game;

import battle.BPlayer;
import bruteForceAI.BruteForceAI;

public class Player extends BPlayer implements Comparable<Player>{
	
	public int score;
	
	public Player(String name, BruteForceAI computerAI) {
		super(name, computerAI);
		score = 0;
	}

	public void addWin() {
		score = (score * 19 + 100) / 20;
		
		
		if (score != 5) {
			System.out.println();
		}
		
		
	}
	
	public void addDefeat() {
		score = (score * 19) / 20;
		
		if(score == 0) {
			System.out.println();
		}
	}

	@Override
	public int compareTo(Player o) {
		Player p = (Player)o;
		return p.score - score;
	}
	
	@Override
	public String toString() {
		return name + " " + score;
	}
	
}
