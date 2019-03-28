package gui.battle;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import battle.Battle;
import game.Game;
import game.ThreadManager;
import gui.Box;
import gui.MainFrame;

@SuppressWarnings("serial")
public class BattleFirstButtonBox extends Box {

	public BattleFirstButtonBox(int x, int y, int width, int height, Box container) {
		super(x, y, width, height, container);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if (Game.instance.battle.players[Game.instance.battle.turn].isHuman() && Game.instance.battle.state == Battle.choosingActiveEffectState) {
			Game.instance.battle.passTurn();
			MainFrame.instance.refresh();
		} else {
			next();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			next();
		}
	}

	public void next() {
		if(MainFrame.instance.currentBox == MainFrame.instance.battleBox) {
			if (Game.instance.battle.state == Battle.choosingActiveEffectState) {
				if (!Game.instance.battle.players[Game.instance.battle.turn].isHuman()) {
					Game.instance.battle.computerAIs[Game.instance.battle.turn].play(Game.instance.battle);
				}
			} else if (Game.instance.battle.state == Battle.choosingTargetCardState) {
				if (Game.instance.battle.players[Game.instance.battle.decidingPlayer].isHuman()) {
					Game.instance.battle.setChosenTarget(-1,  Battle.noneAISimulating);
				} else {
					Game.instance.battle.computerAIs[Game.instance.battle.turn].chooseTarget(Game.instance.battle);
				}
			} else if (Game.instance.battle.state == Battle.executingActionState) {
				Game.instance.battle.executeAction(Battle.noneAISimulating);
			}
			if (Game.instance.battle.winner() == -1) {
				MainFrame.instance.refresh();
			} else {
				ThreadManager.instance.humanBattleHasFinished.release();
				try {
					ThreadManager.instance.roundHasFinished.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				MainFrame.instance.enterBox(MainFrame.instance.mainBox);
			}
		}
	}

	@Override
	public void paint(Graphics graphics) {
		graphics.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		if (Game.instance.battle.state == Battle.choosingActiveEffectState) {
			if (Game.instance.battle.players[Game.instance.battle.turn].isHuman()) {
				graphics.drawString("Pass turn", 14, 19);
			} else {
				graphics.drawString("Next opponent's move", 14, 19);
			}
		} else if (Game.instance.battle.state == Battle.choosingTargetCardState) {
			if (Game.instance.battle.players[Game.instance.battle.decidingPlayer].isHuman()) {
				if (Game.instance.battle.choosingTargetStateAction.type == Battle.atkAction
						&& Game.instance.battle.zones[1 - Game.instance.battle.decidingPlayer][Battle.fieldZone].isEmpty()) {
					graphics.drawString("Attack directly", 14, 19);
				} else {
					graphics.drawString("Don't choose anything", 14, 19);
				}
			} else {
				graphics.drawString("Next (op choosing)", 14, 19);
			}
		} else if (Game.instance.battle.state == Battle.executingActionState) {
			graphics.drawString("Next action", 14, 19);
		}
	}

}
