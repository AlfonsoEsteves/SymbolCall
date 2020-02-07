package tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import battle.Battle;
import battle.Player;
import game.BattleExecutor;
import game.BattleExecutorAutomatic;
import game.Game;
import game.ThreadManager;
import loader.CardLoader;
import loader.DeckLoader;
import zAI.ZAI.ZAIFactory;;

public class DeckCreationTest {
	
	public static final int repetitions = 8;

	public static final int decks = 20;
	
	public static LinkedList<TestedPlayer> testPlayers;
	
	public static Random rnd = new Random();
	
	public static void main(String[] args){
		Game.instantiate();
		
		CardLoader.loadCards();
		
		testPlayers=new LinkedList<>();
		for(int i = 0;i < decks;i++){
			TestedPlayer testPlayer=new TestedPlayer(new ZAIFactory(), null, rnd.nextInt());
			testPlayers.add(testPlayer);
		}
		

		TestedPlayer bestPlayer = null;
		
		for(int r = 0;r < repetitions;r++) {
			List<Player> players1 = new ArrayList<>();
			List<Player> players2 = new ArrayList<>();
			for(TestedPlayer testPlayer1 : testPlayers){
				for(TestedPlayer testPlayer2 : testPlayers){
				if(testPlayer1!=testPlayer2){
						players1.add(testPlayer1);
						players2.add(testPlayer2);
					}
				}
			}
			
			int size = players1.size();
			int[] startingPlayers = IntStream.generate(() -> 0).limit(size).toArray();
			int[] rndSeeds = IntStream.generate(() -> Game.instance.rnd.nextInt()).limit(size).toArray();
			BattleExecutor[] battleExecutors = Collections.nCopies(size, BattleExecutorAutomatic.instance).toArray(new BattleExecutor[size]);		
			
			List<Battle> battles = ThreadManager.instance.executeBattles(players1, players2, startingPlayers, rndSeeds, battleExecutors);	
			
			for(Battle battle : battles) {
				processBattle(battle);
			}

			bestPlayer = null;
			double bestPlayerWinRate = 0;
			TestedPlayer worstPlayer = null;
			double worstPlayerWinRate = 1;
			for(TestedPlayer tp : testPlayers){
				if (tp.winRate() < worstPlayerWinRate) {
					worstPlayer = tp;
					worstPlayerWinRate = tp.winRate();
				}
				if (tp.winRate() > bestPlayerWinRate) {
					bestPlayer = tp;
					bestPlayerWinRate = tp.winRate();
				}
			}
			testPlayers.remove(worstPlayer);
			System.out.println(worstPlayer.winRate());
			
			for(TestedPlayer tp : testPlayers){
				int worstCard = -1;
				double worstCardWinRate = 1;
				for(int i = 0;i<Battle.deckSize;i++){
					String str=tp.deck.get(i).name;
					while(str.length()<25){
						str+=" ";
					}
					double cardWinRate=tp.wonGamesByCards[i]/tp.totalGamesByCards[i];
					if(cardWinRate < worstCardWinRate) {
						worstCard = i;
						worstCardWinRate = cardWinRate;
					}
					
					if(tp == bestPlayer) {
						str+=(int)tp.totalGamesByCards[i]+"."+((int)(tp.totalGamesByCards[i]*10)%10)+"G       wr: ";
						str+=(int)(100*cardWinRate)+"%       norm wr: ";
						str+=(int)(100*cardWinRate/tp.winRate()-100)+"%";
						System.out.println(str);
					}
				}
				tp.deck.remove(worstCard);
				tp.deck.add(Game.instance.playerCards.get(rnd.nextInt(Game.instance.playerCards.size())));
			}
			
			testPlayers.add(new TestedPlayer(new ZAIFactory(), null, rnd.nextInt()));
		}

		System.out.println();
		for(int i = 0;i<Battle.deckSize;i++){
			String str=bestPlayer.deck.get(i).name;
			System.out.println(str);
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
