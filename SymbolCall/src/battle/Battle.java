package battle;

import java.util.Collections;
import java.util.LinkedList;

@SuppressWarnings("unchecked")
public class Battle {

	public static int deckSize = 20;
	public static int handAndFieldMaxSize = deckSize; 
	public static int cardMaxEffects = 3;
	public static int symbolFamilies=4;
	public static int symbolFamilySubtypesPlusOne=3;
	
	public static int choosingActiveEffectState=0;
	public static int choosingTargetCardState=1;
	public static int executingActionState=2;
	
	public static int deckZone=0;
	public static int handZone=1;
	public static int fieldZone=2;

	public static int atkAction=0;
	public static int invAction=1;
	public static int wdrAction=2;
	public static int dscAction=3;
	public static int cllAction=4;
	
	public static int slTarget=0;
	public static int chTarget=1;
	public static int trTarget=2;
	public static int owTarget=3;
	public static int opTarget=4;
	
	public static int rdSymbol=0;
	public static int grSymbol=1;
	public static int blSymbol=2;
	public static int ylSymbol=3;

	public int[] healths = new int[2];
	public BCard[] cards=new BCard[Battle.deckSize*2];
	public LinkedList<Integer>[][] zones = new LinkedList[2][3];
	public int turn;
	public int decidingPlayer;//Usually it is equals to the turn, unless the opponent has to choose a target
	public LinkedList<ActionExecution> executionStack;
	public LinkedList<Integer> calledSymbols;
	public int state;
	public String[] playerNames=new String[2];
	public LinkedList<String> log;
	public Action choosingTargetStateAction;
	public int targetCard=-1;
	public int turnCount;
	
	public Battle() {}

	public Battle(Player[] players, int startingPlayer) {
		turn = startingPlayer;
		decidingPlayer=turn;
		executionStack = new LinkedList<>();
		calledSymbols = new LinkedList<>();
		log=new LinkedList<>();
		turnCount=0;
		for (int i = 0; i < 2; i++) {
			playerNames[i]=players[i].name;
			healths[i] = 120;
			zones[i][Battle.handZone] = new LinkedList<>();
			zones[i][Battle.fieldZone] = new LinkedList<>();
			zones[i][Battle.deckZone] = shuffle(players[i].deck, i);
			for (int j = 0; j < 4; j++) {
				draw(i);
			}
		}
	}

	private LinkedList<Integer> shuffle(LinkedList<Card> deck, int player) {
		int position=0;
		LinkedList<Integer> ordered=new LinkedList<>();
		for(Card model : deck){
			BCard card=new BCard(model);
			card.battleId=player*Battle.deckSize+position;
			card.player=player;
			card.zone = Battle.deckZone;
			card.drawn=false;
			ordered.addLast(card.battleId);
			cards[card.battleId]=card;
			position++;
		};
		LinkedList<Integer> shuffled = new LinkedList<>();
		while (!ordered.isEmpty()) {
			Integer card = ordered.remove(Rnd.nextInt(ordered.size()));
			shuffled.addLast(card);
		}
		return shuffled;
	}

	//Deep copy of the cards, because they have attributes
	//Shallow copy of the zones, called symbols, and execution stack	
	public Battle copy() {
		Battle battle=new Battle();
		for(int i=0;i<Battle.deckSize*2;i++){
			battle.cards[i]=cards[i].copy();
		}
		for(int i=0;i<2;i++) {
			for(int j=0;j<3;j++) {
				battle.zones[i][j]=(LinkedList<Integer>)zones[i][j].clone();
			}
			battle.healths[i]=healths[i];
		}
		battle.executionStack=(LinkedList<ActionExecution>)executionStack.clone();
		battle.calledSymbols=(LinkedList<Integer>)calledSymbols.clone();
		battle.turn=turn;
		battle.decidingPlayer=decidingPlayer;
		battle.state=state;
		battle.choosingTargetStateAction=choosingTargetStateAction;
		battle.targetCard=targetCard;
		return battle;
	}

	public void passTurn() {
		passTurnWithoutDrawing();
		draw(turn);
		turnCount++;
		//System.out.println("Turn of "+playerNames[turn]);
	}

	//This method is used by the AI
	public void passTurnWithoutDrawing() {
		turn = 1 - turn;
		for (int card : zones[turn][Battle.handZone]) {
			cards[card].turn = true;
		}
		for (int card : zones[turn][Battle.fieldZone]) {
			cards[card].turn = true;
		}
		decidingPlayer=turn;
	}

	private void draw(int player) {
		//if(!zones[player][Battle.deckZone].isEmpty()) {
			int c = zones[player][Battle.deckZone].removeFirst();
			BCard card = cards[c];
			card.zone = Battle.handZone;
			card.health = card.model.maxHealth;
			card.turn = true;
			card.drawn=true;
			zones[player][Battle.handZone].addLast(c);
		/*}
		else {
			System.out.println("Warning: EMPTY DECK");
		}*/
	}

	public void executeActiveEffect(int c, int effectNumber) {
		if(log!=null){
			log.clear();
			logMessage(playerNames[turn]+" activated effect "+effectNumber+" of "+cards[c].model.name);
		}
		cards[c].turn = false;
		addEffectToExecutionStack(c, effectNumber, -1);
		state=Battle.executingActionState;
	}

	public boolean executeAction() {
		ActionExecution actionExecution = executionStack.getFirst();
		boolean remove=true;
		if(actionExecution.action.type==Battle.cllAction) {
			callSymbol(actionExecution.action.info, actionExecution.executingCard);
		}
		else {
			if (actionExecution.action.info==Battle.owTarget) {
				if (damagePlayer(cards[actionExecution.executingCard].player, actionExecution.action.amount)) {
					return true;
				}
			}
			else if (actionExecution.action.info==Battle.opTarget) {
				if (damagePlayer(1-cards[actionExecution.executingCard].player, actionExecution.action.amount)) {
					return true;
				}
			}
			else if (actionExecution.action.info==Battle.slTarget) {
				executeActionOverCard(actionExecution.action, actionExecution.executingCard);
			}
			else if (actionExecution.action.info==Battle.trTarget) {
				executeActionOverCard(actionExecution.action, actionExecution.triggeringCard);
			}
			else if (actionExecution.action.info==Battle.chTarget) {
				if(state!=Battle.choosingTargetCardState) {
					state=Battle.choosingTargetCardState;
					choosingTargetStateAction=actionExecution.action;
					decidingPlayer=cards[actionExecution.executingCard].player;
					remove=false;
				}
				else {
					if(targetCard!=-1) {
						executeActionOverCard(actionExecution.action, targetCard);
					}
					else {
						if(actionExecution.action.type==Battle.atkAction && zones[1-decidingPlayer][Battle.fieldZone].isEmpty()) {
							if (damagePlayer(1 - cards[actionExecution.executingCard].player, actionExecution.action.amount)) {
								return true;
							}
						}
					}
					state=Battle.executingActionState;
				}
			}
			else {
				throw new RuntimeException();
			}
		}
		if(remove) {
			executionStack.remove(actionExecution);
			if(executionStack.isEmpty()) {
				state=Battle.choosingActiveEffectState;
				decidingPlayer=turn;
			}
		}
		return false;
	}

	//If choosenTargetCard is -1, then it means the player didn't select a card
	//If the action was an attack and the opponent's field is empty, a -1 means to attack directly at the opponent
	public void setChosenTarget(int chosenTargetCard) {
		if(chosenTargetCard==-1){
			if(choosingTargetStateAction.type==Battle.atkAction && zones[1-decidingPlayer][Battle.fieldZone].isEmpty()) {
				logMessage(playerNames[decidingPlayer]+" chose to attack directly to the opponent");
			}
			else {
				logMessage(playerNames[decidingPlayer]+" chose not to "+actionString(choosingTargetStateAction.type));
			}
		}
		else{
			logMessage(playerNames[decidingPlayer]+" chose to "+actionString(choosingTargetStateAction.type)+" "+cards[chosenTargetCard]);
		}
		targetCard=chosenTargetCard;
		executeAction();
	}
	
	//This should be a verb
	private String actionString(int action) {
		if(action==Battle.atkAction) {
			return "attack";
		}
		if(action==Battle.invAction) {
			return "invoke";
		}
		if(action==Battle.invAction) {
			return "withdraw";
		}
		if(action==Battle.invAction) {
			return "discard";
		}
		return null;
	}
	
	public int checkWinner() {
		if(healths[0]<=0) {
			return 1;
		}
		if(healths[1]<=0) {
			return 0;
		}
		return -1;
	}

	private boolean damagePlayer(int player, int amount) {
		logMessage(playerNames[player]+" receives "+amount+" damage");
		healths[player] -= amount;
		if (healths[player] <= 0) {
			logMessage(playerNames[1-player]+" defeated "+playerNames[player]);
			return true;
		}
		return false;
	}

	private void executeActionOverCard(Action action, int c) {
		BCard card=cards[c];
		if (action.type==Battle.invAction) {//You can invoke from both the hand and deck
			if (card.zone!=Battle.fieldZone) {
				logMessage(card.model.name+" was invoked on the field");
				zones[card.player][card.zone].remove((Object)c);
				zones[card.player][Battle.fieldZone].addLast(c);
				card.zone = Battle.fieldZone;
				card.health=card.model.maxHealth;
			}
			else{
				logMessage(card.model.name+" wasn't invoked because it already was on the field");
			}
		}
		else if (action.type==Battle.wdrAction) {//You can withdraw from both the field and deck
			if (card.zone!=Battle.handZone) {
				logMessage(card.model.name+" was withdrawn to its player's hand");
				card.health = card.model.maxHealth;
				zones[card.player][card.zone].remove((Object)c);
				zones[card.player][Battle.handZone].addLast(c);
				card.zone = Battle.handZone;
			}
			else{
				logMessage(card.model.name+" wasn't withdrawn because it already was in its player's hand");
			}
		}
		else if (action.type==Battle.atkAction) {//You can attack only to the field
			if (card.zone==Battle.fieldZone) {
				if (card.health > action.amount) {
					logMessage(card.model.name+" receives "+action.amount+" damage");
					card.health -= action.amount;
				}
				else {
					logMessage(card.model.name+" was destroyed");
					card.zone = Battle.deckZone;
					zones[card.player][Battle.fieldZone].remove((Object)c);
					zones[card.player][Battle.deckZone].addLast(c);
				}
			}
			else{
				logMessage(card.model.name+" didn't receive damage because it is not on the field");
			}
		}
		else if (action.type==Battle.dscAction) {//You can discard only from the hand
			if (card.zone==Battle.handZone) {
				logMessage(card.model.name+" was discarded");
				card.zone = Battle.deckZone;
				zones[card.player][Battle.handZone].remove((Object)c);
				zones[card.player][Battle.deckZone].addLast(c);
			}
			else{
				logMessage(card.model.name+" wasn't because it is not in its player's hand");
			}
		}
	}

	//The order of execution is:
	//waiting field
	//turn field
	//waiting hand
	//turn hand
	private void callSymbol(int symbol, int triggeringCard) {
		logMessage(cards[triggeringCard].model.name+" called a "+symbolString(symbol)+" symbol");
		calledSymbols.addFirst(new Integer(symbol));
		notifyNewSymbolToZone(zones[1-turn][Battle.fieldZone], triggeringCard);
		notifyNewSymbolToZone(zones[turn][Battle.fieldZone], triggeringCard);
		notifyNewSymbolToZone(zones[1-turn][Battle.handZone], triggeringCard);
		notifyNewSymbolToZone(zones[turn][Battle.handZone], triggeringCard);
	}
	
	private String symbolString(int symbol){
		return "asd";
	}

	private void notifyNewSymbolToZone(LinkedList<Integer> zone, int triggeringCard) {
		for (int c : zone) {
			BCard card=cards[c];
			if (card.zone!=Battle.deckZone) {
				int effectNumber=0;
				for (Effect effect : card.model.effects) {
					if (effect.zone==card.zone) {
						if (!effect.sequence.isEmpty()) {
							if (calledSymbols.size() >= effect.sequence.size()) {
								boolean triggered = true;
								int position = effect.sequence.size();
								for (Integer neededSymbol : effect.sequence) {
									position--;
									Integer currentSymbol = calledSymbols.get(position);
									if(neededSymbol%Battle.symbolFamilySubtypesPlusOne==0) {
										if (neededSymbol/Battle.symbolFamilySubtypesPlusOne!=currentSymbol/Battle.symbolFamilySubtypesPlusOne) {
											triggered = false;
										}
									}
									else {
										if (!neededSymbol.equals(currentSymbol)) {
											triggered = false;
										}
									}
								}
								if (triggered) {
									addEffectToExecutionStack(card.battleId, effectNumber, triggeringCard);
								}
							}
						}
					}
					effectNumber++;
				}
			}
		}
	}
	
	private void addEffectToExecutionStack(int executingCard, int executingEffect, int triggeringCard) {
		Effect effect=cards[executingCard].model.effects.get(executingEffect);
		LinkedList<Action> reversedActions = (LinkedList<Action>) effect.actions.clone();
		Collections.reverse(reversedActions);
		for (Action action : reversedActions) {
			ActionExecution actionExecution = new ActionExecution(action, executingCard, executingEffect, triggeringCard);
			executionStack.addFirst(actionExecution);
		}
	}
	
	private void logMessage(String message){
		if(log!=null){
			//System.out.println(message);
			log.addLast(message);
		}
	}
	
	@Override
	public String toString(){
		return "0H:"+zones[0][Battle.handZone].size()+" 0F:"+zones[0][Battle.fieldZone].size()+" 1F:"+zones[1][Battle.fieldZone].size()+" 1H:"+zones[1][Battle.handZone].size();
	}
}
