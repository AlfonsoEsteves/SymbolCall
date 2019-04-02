package gui.battle;

import java.awt.Graphics;

import game.Game;
import gui.Box;

@SuppressWarnings("serial")
public class BattleHealthBox extends Box {

	public BattleHealthBox(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public void paint(Graphics graphics) {
		graphics.drawRect(getX(), getY(), getWidth() - 1, getHeight() - 1);
		graphics.drawString(Game.instance.battle.players[1].name + " health: " + Game.instance.battle.healths[1], 14, 35);
		graphics.drawString(Game.instance.battle.players[0].name + " health: " + Game.instance.battle.healths[0], 14, 65);
	}

}
