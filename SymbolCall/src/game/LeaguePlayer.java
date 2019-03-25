package game;

import battle.BPlayer;

@SuppressWarnings("serial")
public class LeaguePlayer extends BPlayer implements Comparable<LeaguePlayer>{
	
	public int score;
	
	public LeaguePlayer() {}
	
	public LeaguePlayer(String name) {
		super(name);
		score = 0;
	}

	public void addWin() {
		score = (score * 19 + 100) / 20;
	}
	
	public void addDefeat() {
		score = (score * 19) / 20;
	}

	@Override
	public int compareTo(LeaguePlayer o) {
		LeaguePlayer p = (LeaguePlayer)o;
		return p.score - score;
	}
	
	@Override
	public String toString() {
		return name + " " + score;
	}
	
}
