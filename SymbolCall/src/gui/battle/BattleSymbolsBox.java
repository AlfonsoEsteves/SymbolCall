package gui.battle;

import java.awt.Graphics;

import battle.Battle;
import game.Game;
import gui.Box;
import loader.ImageLoader;

public class BattleSymbolsBox extends Box {

	public BattleSymbolsBox(int x, int y, int width, int height, Box container) {
		super(x, y, width, height, container);
	}
	
	@Override
	public void paint(Graphics graphics) {
		graphics.drawRect(0, 0, getWidth()-1, getHeight()-1);
		int i=0;
		for(Integer symbol : Game.battle.calledSymbols) {
			graphics.drawImage(ImageLoader.bigSymbols[(symbol/Battle.symbolFamilySubtypesPlusOne)*Battle.symbolFamilySubtypesPlusOne], getWidth()-(54+i*30), 7, null);
			if(symbol%Battle.symbolFamilySubtypesPlusOne!=0) {
				graphics.drawImage(ImageLoader.bigSymbols[symbol], getWidth()-(52+i*30), 9, null);
			}
			i++;
			if(i==7) {
				break;
			}
		}
	}

}
