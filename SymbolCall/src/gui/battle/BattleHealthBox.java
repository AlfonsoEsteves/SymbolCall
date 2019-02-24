package gui.battle;

import java.awt.Graphics;

import game.BattleExecutor;
import game.Game;
import gui.Box;

@SuppressWarnings("serial")
public class BattleHealthBox extends Box {

	public BattleHealthBox(int x, int y, int width, int height, Box container) {
		super(x, y, width, height, container);
	}

	@Override
	public void paint(Graphics graphics) {
		graphics.drawRect(getX(), getY(), getWidth() - 1, getHeight() - 1);
		graphics.drawString(Game.battle.players[1].name + " health: " + Game.battle.healths[1], 14, 35);
		graphics.drawString(Game.battle.players[0].name + " health: " + Game.battle.healths[0], 14, 65);
	}

}
