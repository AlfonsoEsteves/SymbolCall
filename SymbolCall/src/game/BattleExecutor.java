package game;

import battle.Battle;
import battle.Player;

public abstract class BattleExecutor {

	public abstract Battle executeBattle(Player player1, Player player2, int startingPlayer, int rndSeed);

}
