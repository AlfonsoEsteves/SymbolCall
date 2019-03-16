package loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

import battle.Action;
import battle.Battle;
import battle.Card;
import battle.Effect;
import battle.Rnd;
import bruteForceAI.CardScoreCalculator;
import game.Game;

public class CardLoader {
	
	public static void loadCards() {
		Game.ins.playerCards=loadCardsAux(Path.path+"sets\\player cards");
		Game.ins.computerCards=loadCardsAux(Path.path+"sets\\computer cards");
	}
	
	private static LinkedList<Card> loadCardsAux(String location) {
		LinkedList<Card> cards=new LinkedList<>();
		try {
			File folder = new File(location);
			File[] listOfFiles = folder.listFiles();
			for (File file : listOfFiles) {
			    if (file.isFile()) {
			        Card card=new Card();
			        BufferedReader in = new BufferedReader(new FileReader(file.getAbsolutePath()));
			        card.name=file.getName().split("\\.")[0];
			        card.maxHealth=Integer.parseInt(in.readLine().trim());
			        String line;
			        while((line = in.readLine()) != null)
			        {
			        	line=line.trim();
			        	if(line.equals("description:")) {
			        		break;
			        	}
			        	if(!line.equals("")) {
				            if(line.equals("hand effect:")) {
				            	Effect effect=new Effect(Battle.handZone);
				            	card.effects.addLast(effect);
				            }
				            else if(line.equals("field effect:")) {
				            	Effect effect=new Effect(Battle.fieldZone);
				            	card.effects.addLast(effect);
				            }
				            else if(line.toCharArray()[0]=='[') {
				            	if(line.length()>2) {
				            		addSequence(card.effects.getLast(), line);
				            	}
				            }
				            else {
				            	String[] parts=line.split(" ");
				            	int type;
				            	if(parts[0].equals("atta")) {
			            			type=Battle.atkAction;
			            		}
			            		else if(parts[0].equals("dama")) {
			            			type=Battle.atkAction;
			            		}
			            		else if(parts[0].equals("invo")) {
			            			type=Battle.invAction;
			            		}
			            		else if(parts[0].equals("with")) {
			            			type=Battle.wdrAction;
			            		}
			            		else if(parts[0].equals("disc")) {
			            			type=Battle.dscAction;
			            		}
			            		else if(parts[0].equals("call")) {
			            			type=Battle.cllAction;
			            		}
			            		else {
			    			        in.close();
			            			throw new RuntimeException();
			            		}
				            	int info;
				            	if(parts[1].equals("sl")) {
				            		info=Battle.slTarget;
			            		}
			            		else if(parts[1].equals("ch")) {
				            		info=Battle.chTarget;
			            		}
			            		else if(parts[1].equals("tr")) {
				            		info=Battle.trTarget;
			            		}
			            		else if(parts[1].equals("op")) {
				            		info=Battle.opTarget;
			            		}
			            		else if(parts[1].equals("ow")) {
				            		info=Battle.owTarget;
			            		}
			            		else {
			            			char color=parts[1].charAt(0);
			            			if(color=='r') {
			            				info=Battle.rdSymbol*Battle.symbolFamilySubtypesPlusOne;
			            			}
			            			else if(color=='g') {
			            				info=Battle.grSymbol*Battle.symbolFamilySubtypesPlusOne;
			            			}
			            			else if(color=='b') {
			            				info=Battle.blSymbol*Battle.symbolFamilySubtypesPlusOne;
			            			}
			            			else if(color=='y') {
			            				info=Battle.ylSymbol*Battle.symbolFamilySubtypesPlusOne;
			            			}
			            			else {
				    			        in.close();
				            			throw new RuntimeException();
			            			}
			            			int subtype=parts[1].charAt(1)-'0';
			            			if(subtype>Battle.symbolFamilySubtypesPlusOne) {
				    			        in.close();
				            			throw new RuntimeException();
			            			}
			            			info+=subtype;
			            		}
				            	int amount=-1;
				            	if(parts.length==3) {
				            		amount=Integer.parseInt(parts[2]);
				            	}
				            	Action action=new Action(type, info, amount);
			            		card.effects.getLast().actions.add(action);
				            }
			        	}
			        }
			        in.close();
			        CardScoreCalculator.calculateScores(card);
			        cards.add(card);
			    }
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return cards;
	}
	
	private static void addSequence(Effect effect, String line) {
		String[] symbols=line.substring(1, line.length()-1).split(" ");
    	for(String symbol : symbols) {
    		char color=symbol.charAt(0);
    		int subType=symbol.charAt(1)-'0';
    		if(color=='r') {
    			effect.sequence.addLast(new Integer(Battle.rdSymbol*Battle.symbolFamilySubtypesPlusOne+subType));
    		}
    		else if(color=='g') {
    			effect.sequence.addLast(new Integer(Battle.grSymbol*Battle.symbolFamilySubtypesPlusOne+subType));
    		}
    		else if(color=='b') {
    			effect.sequence.addLast(new Integer(Battle.blSymbol*Battle.symbolFamilySubtypesPlusOne+subType));
    		}
    		else if(color=='y') {
    			effect.sequence.addLast(new Integer(Battle.ylSymbol*Battle.symbolFamilySubtypesPlusOne+subType));
    		}
    		else {
    			throw new RuntimeException();
    		}
    	}
	}

	public static LinkedList<Card> randomDeck(){
		LinkedList<Card> deck=new LinkedList<>();
		for(int i=0;i<Battle.deckSize;i++) {
			Card card=Game.ins.playerCards.get(Rnd.nextInt(Game.ins.playerCards.size()));
			deck.add(card);
		}
		return deck;
	}

	public static Card getComputerCard(String line) {
		for(Card card : Game.ins.computerCards){
			if(card.name.equals(line)){
				return card;
			}
		}
		throw new RuntimeException("There is no computer card named "+line);
	}

	public static Card getPlayerCard(String line) {
		for(Card card : Game.ins.playerCards){
			if(card.name.equals(line)){
				return card;
			}
		}
		throw new RuntimeException("There is no player card named "+line);
	}
}
