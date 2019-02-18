package automatic;

import java.util.LinkedList;

import battle.BPlayer;
import bruteForceAI.BruteForceAI;
import loader.CardLoader;
import loader.DeckLoader;
import randomAI.RandomAI;

public class AITest {
	
	public static LinkedList<TestedPlayer> brutePlayers;
	public static LinkedList<TestedPlayer> randomPlayers;
	
	public static void main(String[] args){
		CardLoader.loadCards();
		
		DeckLoader.loadDecks();
		brutePlayers=new LinkedList<>();
		for(BPlayer player : DeckLoader.decks){
			TestedPlayer brutePlayer=new TestedPlayer(player);
			brutePlayer.player.computerAI=BruteForceAI.getInstance();
			brutePlayers.add(brutePlayer);
		}

		DeckLoader.loadDecks();
		randomPlayers=new LinkedList<>();
		for(BPlayer player : DeckLoader.decks){
			TestedPlayer randomPlayer=new TestedPlayer(player);
			randomPlayer.player.computerAI=new RandomAI();
			randomPlayers.add(randomPlayer);
		}
		
		long ini=System.nanoTime();
		for(int i=0;i<30;i++){
			for(TestedPlayer testPlayer1 : brutePlayers){
				for(TestedPlayer testPlayer2 : randomPlayers){
					System.out.println(i+") "+testPlayer1.player.name+" vs "+testPlayer2.player.name);
					AutoBattle.executeBattle(testPlayer1, testPlayer2);
				}
			}
		}
		long end=System.nanoTime();
		System.out.println("Time: "+((end-ini)/1000000000));
		
		int wonByBrute=0;
		for(TestedPlayer testPlayer1 : brutePlayers){
			wonByBrute+=testPlayer1.wonGames();
		}

		int wonByRandom=0;
		for(TestedPlayer testPlayer1 : randomPlayers){
			wonByRandom+=testPlayer1.wonGames();
		}

		System.out.println("Won by brute: "+wonByBrute+"   "+100*wonByBrute/(wonByBrute+wonByRandom)+"%");
		System.out.println("Won by random: "+wonByRandom+"   "+100*wonByRandom/(wonByBrute+wonByRandom)+"%");
	}
	
	static String round(double x){
		return (int)x+"."+(int)(x*10)%10;
	}
}
