package automatic;

import battle.Battle;
import battle.Player;

public class TestedPlayer {
	
	public Player player;
	
	//each won game counts as 1/deckSize
	public int wonGamesAsFirst;
	public int wonGamesAsSecond;
	
	//each won game counts as 1/deckSize
	public int totalGamesAsFirst;
	public int totalGamesAsSecond;
	
	public int winTurnsCount;
	public int loseTurnsCount;
	
	//If a card has a very high percentage, it shouldn't be nerfed
	//Cards should only be nerfed if they can be added to multiple top tier decks and improve them
	public double[] wonGamesByCards;
	public double[] totalGamesByCards;
	
	public TestedPlayer(Player player){
		this.player=player;
		wonGamesByCards=new double[Battle.deckSize];
		totalGamesByCards=new double[Battle.deckSize];
	}
	
	public int wonGames(){
		return wonGamesAsFirst+wonGamesAsSecond;
	}
	
	public int lostGames(){
		return totalGames()-wonGames();
	}
	
	public int totalGames(){
		return totalGamesAsFirst+totalGamesAsSecond;
	}
	
	public double winRate(){
		return (double)wonGames()/(double)totalGames();
	}
	
	public double averageTurns(){
		return (double)(winTurnsCount+loseTurnsCount)/(double)totalGames();
	}
	
	public double averageWinTurns(){
		return (double)winTurnsCount/(double)wonGames();
	}
	
	public double averageLoseTurns(){
		return (double)loseTurnsCount/(double)lostGames();
	}
}
