package automatic;

import java.util.LinkedList;

import battle.Battle;
import battle.BPlayer;
import bruteForceAI.BruteForceAI;
import loader.CardLoader;
import loader.DeckLoader;

public class Test {
	
	public static LinkedList<TestedPlayer> testPlayers;
	
	public static void main(String[] args){
		CardLoader.loadCards();
		DeckLoader.loadDecks();
		testPlayers=new LinkedList<>();
		for(BPlayer player : DeckLoader.decks){
			TestedPlayer testPlayer=new TestedPlayer(player);
			testPlayer.player.computerAI=BruteForceAI.getInstance();
			testPlayers.add(testPlayer);
		}
		
		long ini=System.nanoTime();
		for(int i=0;i<10;i++){
			for(TestedPlayer testPlayer1 : testPlayers){
				for(TestedPlayer testPlayer2 : testPlayers){
					if(testPlayer1!=testPlayer2){
						System.out.println(i+") "+testPlayer1.player.name+" vs "+testPlayer2.player.name);
						AutoBattle.executeBattle(testPlayer1, testPlayer2);
					}
				}
			}
		}
		long end=System.nanoTime();
		System.out.println("Time: "+((end-ini)/1000000000));
				

		for(TestedPlayer tp : testPlayers){
			System.out.println();
			System.out.println(tp.player.name+"       "+tp.totalGames()+"G       wr: "+round(100*tp.winRate()));
			System.out.println("turns: "+round(tp.averageTurns())+"     win turns: "+round(tp.averageWinTurns())+"     lose turns: "+round(tp.averageLoseTurns()));
						
			System.out.println();
			for(int i=0;i<Battle.deckSize;i++){
				String str=tp.player.deck.get(i).name;
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
	
	static String round(double x){
		return (int)x+"."+(int)(x*10)%10;
	}
}
