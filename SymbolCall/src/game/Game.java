package game;

import battle.Battle;
import battle.Player;
import battle.Rnd;
import bruteForceAI.BruteForceAI;
import loader.BossLoader;
import loader.CardLoader;
import loader.DeckLoader;
import loader.ImageLoader;

public class Game {

	public static Battle battle;
	public static Player[] players;
	
	static{
		CardLoader.loadCards();
		ImageLoader.loadImages();
		BossLoader.loadBosses();
		DeckLoader.loadDecks();
		players=new Player[2];
		players[0]=new Player();
		players[1]=new Player();
		players[0].deck=DeckLoader.decks.get(Rnd.nextInt(DeckLoader.decks.size())).deck;
		players[1].deck=DeckLoader.decks.get(Rnd.nextInt(DeckLoader.decks.size())).deck;
		//players[0].deck=DeckLoader.getPlayer("White dragon");
		//players[1].deck=DeckLoader.getPlayer("Dragon_aq_pr");
		//players[0].computerAI=BruteForceAI.getInstance();
   		players[1].computerAI=BruteForceAI.getInstance();
		battle=new Battle(players, Rnd.nextInt(2));
	}
	
}
