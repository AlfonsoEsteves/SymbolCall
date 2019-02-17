package automatic;

import battle.Battle;
import battle.Player;

public class AutoBattle {
	
	public static void executeBattle(TestedPlayer player0, TestedPlayer player1) {
		Player[] players={player0.player, player1.player};
		Battle battle=new Battle(players, 0);
		int winner=-1;
		while(winner==-1) {
			if(battle.state==Battle.choosingActiveEffectState) {
				players[battle.turn].computerAI.play(battle);
			}
			else if(battle.state==Battle.choosingTargetCardState) {
				players[battle.decidingPlayer].computerAI.chooseTarget(battle);
			}
			else if(battle.state==Battle.executingActionState) {
				battle.executeAction();
			}
			winner=battle.checkWinner();
		}
		
		if(winner==0){
			player0.wonGamesAsFirst++;
			player0.winTurnsCount+=battle.turnCount;
			player1.loseTurnsCount+=battle.turnCount;
		}
		else{
			player1.wonGamesAsSecond++;
			player1.winTurnsCount+=battle.turnCount;
			player0.loseTurnsCount+=battle.turnCount;
		}
		player0.totalGamesAsFirst++;
		player1.totalGamesAsSecond++;
		
		int drawnCards0=0;
		int drawnCards1=0;
		for(int i=0;i<Battle.deckSize;i++){
			if(battle.cards[i].drawn){
				drawnCards0++;
			}
			if(battle.cards[Battle.deckSize+i].drawn){
				drawnCards1++;
			}
		}
		
		for(int i=0;i<Battle.deckSize;i++){
			if(battle.cards[i].drawn){
				player0.totalGamesByCards[i]+=1.0/(double)drawnCards0;
				if(winner==0){
					player0.wonGamesByCards[i]+=1.0/(double)drawnCards0;
				}
			}
			if(battle.cards[Battle.deckSize+i].drawn){
				player1.totalGamesByCards[i]+=1.0/(double)drawnCards1;
				if(winner==1){
					player1.wonGamesByCards[i]+=1.0/(double)drawnCards1;
				}
			}
		}
	}
}
