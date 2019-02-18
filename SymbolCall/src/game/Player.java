package game;

import battle.BPlayer;

public class Player implements Comparable<Player>{
	
	public BPlayer player;
	
	public int score;
	
	public void addWin() {
		score = (score * 19 + 100) / 20;
	}
	
	public void addDefeat() {
		score = (score * 19) / 20;
	}

	@Override
	public int compareTo(Player o) {
		Player p = (Player)o;
		return p.score - score;
	}
	
}
