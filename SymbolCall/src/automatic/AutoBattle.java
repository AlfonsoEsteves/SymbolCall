package automatic;

import battle.Battle;
import game.BattleExecutor;
import battle.BPlayer;

public class AutoBattle {
	
	public static void executeBattle(TestedPlayer player0, TestedPlayer player1) {
		BPlayer[] players={player0.player, player1.player};
		int winner=BattleExecutor.executeBattle(players);
		if(winner==0){
			player0.wonGamesAsFirst++;
			player0.winTurnsCount+=BattleExecutor.battle.turnCount;
			player1.loseTurnsCount+=BattleExecutor.battle.turnCount;
		}
		else{
			player1.wonGamesAsSecond++;
			player1.winTurnsCount+=BattleExecutor.battle.turnCount;
			player0.loseTurnsCount+=BattleExecutor.battle.turnCount;
		}
		player0.totalGamesAsFirst++;
		player1.totalGamesAsSecond++;
		
		int drawnCards0=0;
		int drawnCards1=0;
		for(int i=0;i<Battle.deckSize;i++){
			if(BattleExecutor.battle.cards[i].drawn){
				drawnCards0++;
			}
			if(BattleExecutor.battle.cards[Battle.deckSize+i].drawn){
				drawnCards1++;
			}
		}
		
		for(int i=0;i<Battle.deckSize;i++){
			if(BattleExecutor.battle.cards[i].drawn){
				player0.totalGamesByCards[i]+=1.0/(double)drawnCards0;
				if(winner==0){
					player0.wonGamesByCards[i]+=1.0/(double)drawnCards0;
				}
			}
			if(BattleExecutor.battle.cards[Battle.deckSize+i].drawn){
				player1.totalGamesByCards[i]+=1.0/(double)drawnCards1;
				if(winner==1){
					player1.wonGamesByCards[i]+=1.0/(double)drawnCards1;
				}
			}
		}
	}
}
