package gui.battle;

import java.awt.Color;
import java.awt.Graphics;

import battle.Action;
import battle.ActionExecution;
import battle.Battle;
import battle.Card;
import battle.Effect;
import game.Game;
import gui.Box;
import gui.FontList;
import gui.ImageDrawer;
import loader.ImageLoader;

@SuppressWarnings("serial")
public class BattleStackBox extends Box {
	
	public static int entryHeight=75;

	public BattleStackBox(int x, int y, int width, int height, Box container) {
		super(x, y, width, height, container);
	}
	
	@Override
	public void paint(Graphics graphics) {
		graphics.setFont(FontList.arial12B);
		graphics.setColor(Color.BLACK);
		int lastCard=-1;
		Effect lastEffect=null;
		int cardX=15;
		int cardY=15-entryHeight;
		int actionY=0;
		for(ActionExecution actionExecution : Game.instance.battle.executionStack) {
			//Action action=actionExecution.action;
			Effect effect=Game.instance.battle.cards[actionExecution.executingCard].model.effects.get(actionExecution.executingEffect);
			if(effect!=lastEffect || actionExecution.executingCard!=lastCard) {
				lastEffect=effect;
				lastCard=actionExecution.executingCard;
				cardY+=entryHeight+20;
				if(actionExecution.triggeringCard!=-1) {
					Card triggeringCard=Game.instance.battle.cards[actionExecution.triggeringCard].model;
					ImageDrawer.drawImage(triggeringCard.image, cardX, cardY, entryHeight, entryHeight, graphics);
				}
				Card executingCard=Game.instance.battle.cards[actionExecution.executingCard].model;
				ImageDrawer.drawImage(executingCard.image, cardX+12, cardY+12, entryHeight, entryHeight, graphics);
				actionY=cardY;
			}
			Action action=actionExecution.action;
			String actionStr="";
			if(action.type==Battle.atkAction) {
				actionStr="Damage "+action.amount;
			}
			else if(action.type==Battle.invAction) {
				actionStr="Invoke";
			}
			else if(action.type==Battle.wdrAction) {
				actionStr="Withdraw";
			}
			else if(action.type==Battle.dscAction) {
				actionStr="Discard";
			}
			else if(action.type==Battle.cllAction) {
				actionStr="Call";
			}
			graphics.drawString(actionStr, 109, actionY+20);
			if (action.type == Battle.cllAction) {
				graphics.drawImage(ImageLoader.midSymbols[(action.info/Battle.symbolFamilySubtypesPlusOne)*Battle.symbolFamilySubtypesPlusOne], 170, actionY + 7, null);
				graphics.drawImage(ImageLoader.midSymbols[action.info], 170, actionY + 7, null);
			} else {
				graphics.drawImage(ImageLoader.targets[action.info], 172, actionY + 10, null);
			}
			actionY+=21;
		}
	}

}
