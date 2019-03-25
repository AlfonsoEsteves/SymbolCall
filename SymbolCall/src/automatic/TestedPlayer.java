package automatic;

import battle.Battle;
import battle.Card;
import battle.ComputerAI;

import java.util.List;

import battle.BPlayer;

@SuppressWarnings("serial")
public abstract class TestedPlayer extends BPlayer{
	
	public int wonGamesAsFirst;
	public int wonGamesAsSecond;
	
	public int totalGamesAsFirst;
	public int totalGamesAsSecond;
	
	public int winTurnsCount;
	public int loseTurnsCount;
	
	//If a card has a very high percentage, it not necessarily should be nerfed
	//Cards should only be nerfed if they can be added to multiple top tier decks and improve them
	public double[] wonGamesByCards;
	public double[] totalGamesByCards;
	
	public TestedPlayer(String name, List<Card> deck){
		super(name);
		this.deck = deck;
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
	
	@Override
	public abstract ComputerAI instantiateComputerAI();
}
