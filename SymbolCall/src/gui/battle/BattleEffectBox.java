package gui.battle;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import battle.Action;
import battle.BCard;
import battle.Battle;
import battle.Effect;
import game.BattleExecutorAutomatic;
import game.Game;
import gui.Box;
import gui.FontList;
import gui.MainFrame;
import loader.ImageLoader;

@SuppressWarnings("serial")
public class BattleEffectBox extends Box {

	public static int effectWidth = 46;
	public static int effectHeight = 69;
	public static int actionHeight = 14;
	public static int smallSymbolWidth = 11;
	public static int[] actionsStart={30, 20, 13, 8};

	public BCard card;
	public int effectNumber;

	public BattleEffectBox(int x, int y, int width, int height, Box container) {
		super(x, y, width, height, container);
	}

	@Override
	public void paint(Graphics graphics) {
		if(card.model.effects.size()<=effectNumber){
			return;
		}
		boolean grayed=false;
		Effect effect=card.model.effects.get(effectNumber);
		if(effect.sequence.isEmpty()) {
			if(!card.turn || Game.battle.turn!=card.player) {
				grayed=true;
			}
		}
		if(effect.zone!=card.zone) {
			grayed=true;
		}
		if(grayed) {
			graphics.drawImage(ImageLoader.unavailableEffect, 0, 6, null);
		}
		else {
			graphics.drawImage(ImageLoader.availableEffect, 0, 6, null);
		}		
		/*graphics.setColor(Color.RED);
		graphics.drawRect(0, 6, getWidth()-1, getHeight()-7);*/
		int effectPartY = actionsStart[effect.actions.size()-1];
		graphics.setFont(FontList.arial12B);
		graphics.setColor(Color.BLACK);
		for (Action action : effect.actions) {
			String actionStr="";
			int actionStrOffset=0;
			if(action.type==Battle.atkAction) {
				actionStr="("+action.amount+")";
				if(action.amount<10) {
					actionStrOffset=9;
				}
				else {
					actionStrOffset=6;
				}
			}
			else if(action.type==Battle.invAction) {
				actionStr="INVO";
				actionStrOffset=4;
			}
			else if(action.type==Battle.wdrAction) {
				actionStr="WITH";
				actionStrOffset=3;
			}
			else if(action.type==Battle.dscAction) {
				actionStr="DISC";
				actionStrOffset=4;
			}
			graphics.drawString(actionStr, actionStrOffset, effectPartY+12);
			if (action.type == Battle.cllAction) {
				graphics.drawImage(ImageLoader.midSymbols[(action.info/Battle.symbolFamilySubtypesPlusOne)*Battle.symbolFamilySubtypesPlusOne], 15, effectPartY, null);
				graphics.drawImage(ImageLoader.midSymbols[action.info], 15, effectPartY, null);
			} else {
				graphics.drawImage(ImageLoader.targets[action.info], 31, effectPartY+2, null);
			}
			effectPartY += actionHeight+(4-effect.actions.size())*3;
		}
		if (!effect.sequence.isEmpty()) {
			int symbolX = (effectWidth/2) - (effect.sequence.size() * smallSymbolWidth / 2);
			for (Integer symbol : effect.sequence) {
				graphics.drawImage(ImageLoader.smallSymbols[(symbol/Battle.symbolFamilySubtypesPlusOne)*Battle.symbolFamilySubtypesPlusOne], symbolX, 1, null);
				graphics.drawImage(ImageLoader.smallSymbols[symbol], symbolX, 1, null);
				symbolX += smallSymbolWidth;
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if(card.model.effects.size()<=effectNumber){
			return;
		}
		if(Game.battle.turn==0) {
			if(Game.battle.state==Battle.choosingActiveEffectState) {
				if(card.player==0) {
					Effect effect=card.model.effects.get(effectNumber);
					if(card.turn && effect.zone==card.zone && effect.sequence.isEmpty()) {
						Game.battle.executeActiveEffect(card.battleId, effectNumber);
						MainFrame.instance.refresh();
					}
				}
			}
		}
	}
	
}
