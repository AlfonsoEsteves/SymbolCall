package game;

import battle.Player;
import battle.Battle;

public abstract class BattleExecutor {

	public abstract Battle executeBattle(Player player1, Player player2, int startingPlayer, int rndSeed);

}
