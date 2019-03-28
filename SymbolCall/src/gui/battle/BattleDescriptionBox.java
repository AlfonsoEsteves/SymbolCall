package gui.battle;

import java.awt.Graphics;

import battle.Battle;
import game.Game;
import gui.Box;

@SuppressWarnings("serial")
public class BattleDescriptionBox extends Box {

	public BattleDescriptionBox(int x, int y, int width, int height, Box container) {
		super(x, y, width, height, container);
	}

	@Override
	public void paint(Graphics graphics) {
		graphics.drawRect(0, 0, getWidth()-1, getHeight()-1);
		String turnString;
		if(Game.instance.battle.turn==0) {
			turnString="Bottom player's turn";
		}
		else {
			turnString="Top player's turn";
		}
		graphics.drawString(turnString, 14, 35);
		String stateString="";
		if(Game.instance.battle.state==Battle.executingActionState) {
			stateString="Executing chain of actions";
		}
		if(Game.instance.battle.state==Battle.choosingTargetCardState) {
			stateString="Choosing target";
		}
		graphics.drawString(stateString, 14, 65);	
	}

}
