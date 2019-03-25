package automatic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import battle.Battle;
import battle.ComputerAI;
import battle.Rnd;
import battle.BPlayer;
import bruteForceAI.BruteForceAI;
import game.Game;
import game.ThreadManager;
import loader.CardLoader;
import loader.DeckLoader;

@SuppressWarnings("serial")
public class DeckTest {

	public static final int repetitions = 3;
	
	public static LinkedList<TestedPlayer> testPlayers;
	
	public static void main(String[] args){
		Game.ins.rnd = new Rnd();
		
		CardLoader.loadCards();
		DeckLoader.loadDecks();
		
		testPlayers=new LinkedList<>();
		for(BPlayer player : DeckLoader.decks){
			TestedPlayer testPlayer=new TestedPlayer(player.name, player.deck) {
				@Override
				public ComputerAI instantiateComputerAI(Random rnd) {
					return new BruteForceAI();
				}
			};
			testPlayers.add(testPlayer);
		}

		List<BPlayer> players1 = new ArrayList<>();
		List<BPlayer> players2 = new ArrayList<>();
		
		for(int i=0;i<repetitions;i++){
			for(TestedPlayer testPlayer1 : testPlayers){
				for(TestedPlayer testPlayer2 : testPlayers){
					if(testPlayer1!=testPlayer2){
						players1.add(testPlayer1);
						players2.add(testPlayer2);
					}
				}
			}
		}
		
		int[] startingPlayers = IntStream.generate(() -> 0).limit(players1.size()).toArray();
		long ini = System.nanoTime();
		List<Battle> battles = ThreadManager.ins.executeBattles(players1, players2, startingPlayers);
		long end = System.nanoTime();
		System.out.println("Time: " + ((end - ini) / 1000000000));		
		
		for(Battle battle : battles) {
			processBattle(battle);
		}

		for(TestedPlayer tp : testPlayers){
			System.out.println();
			System.out.println(tp.name+"       "+tp.totalGames()+"G       wr: "+round(100*tp.winRate()));
			System.out.println("turns: "+round(tp.averageTurns())+"     win turns: "+round(tp.averageWinTurns())+"     lose turns: "+round(tp.averageLoseTurns()));
						
			System.out.println();
			for(int i=0;i<Battle.deckSize;i++){
				String str=tp.deck.get(i).name;
				while(str.length()<25){
					str+=" ";
				}
				double cardWinRate=tp.wonGamesByCards[i]/tp.totalGamesByCards[i];
				str+=(int)tp.totalGamesByCards[i]+"."+((int)(tp.totalGamesByCards[i]*10)%10)+"G       wr: ";
				str+=(int)(100*cardWinRate)+"%       norm wr: ";
				str+=(int)(100*cardWinRate/tp.winRate()-100)+"%";
				System.out.println(str);
			}
		}
	}
	
	public static void processBattle(Battle battle) {
		TestedPlayer player0 = (TestedPlayer) battle.players[0];
		TestedPlayer player1 = (TestedPlayer) battle.players[1];
		
		if (battle.winner() == 0) {
			player0.wonGamesAsFirst++;
			player0.winTurnsCount += battle.turnCount;
			player1.loseTurnsCount += battle.turnCount;
		} else {
			player1.wonGamesAsSecond++;
			player1.winTurnsCount += battle.turnCount;
			player0.loseTurnsCount += battle.turnCount;
		}
		player0.totalGamesAsFirst++;
		player1.totalGamesAsSecond++;

		int drawnCards0 = 0;
		int drawnCards1 = 0;
		for (int i = 0; i < Battle.deckSize; i++) {
			if (battle.cards[i].drawn) {
				drawnCards0++;
			}
			if (battle.cards[Battle.deckSize + i].drawn) {
				drawnCards1++;
			}
		}

		for (int i = 0; i < Battle.deckSize; i++) {
			if (battle.cards[i].drawn) {
				player0.totalGamesByCards[i] += 1.0 / (double) drawnCards0;
				if (battle.winner() == 0) {
					player0.wonGamesByCards[i] += 1.0 / (double) drawnCards0;
				}
			}
			if (battle.cards[Battle.deckSize + i].drawn) {
				player1.totalGamesByCards[i] += 1.0 / (double) drawnCards1;
				if (battle.winner() == 1) {
					player1.wonGamesByCards[i] += 1.0 / (double) drawnCards1;
				}
			}
		}
	}
	
	static String round(double x){
		return (int)x+"."+(int)(x*10)%10;
	}
}
