package game;

import battle.ComputerAI.ComputerAIFactory;
import battle.Player;

@SuppressWarnings("serial")
public class LeaguePlayer extends Player implements Comparable<LeaguePlayer>{
	
	public int score;
	
	public LeaguePlayer() {}

	public LeaguePlayer(ComputerAIFactory computerAIFactory, int seed) {
		super(computerAIFactory, null, seed);
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
