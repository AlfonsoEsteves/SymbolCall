package gui.battle;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import battle.Battle;
import game.BattleExecutorManual;
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
		if (Game.ins.battle.players[Game.ins.battle.turn].isHuman() && Game.ins.battle.state == Battle.choosingActiveEffectState) {
			Game.ins.battle.passTurn();
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
			if (Game.ins.battle.state == Battle.choosingActiveEffectState) {
				if (!Game.ins.battle.players[Game.ins.battle.turn].isHuman()) {
					BattleExecutorManual.computerAI.play(Game.ins.battle);
				}
			} else if (Game.ins.battle.state == Battle.choosingTargetCardState) {
				if (Game.ins.battle.players[Game.ins.battle.decidingPlayer].isHuman()) {
					Game.ins.battle.setChosenTarget(-1);
				} else {
					BattleExecutorManual.computerAI.chooseTarget(Game.ins.battle);
				}
			} else if (Game.ins.battle.state == Battle.executingActionState) {
				Game.ins.battle.executeAction();
			}
			if (Game.ins.battle.winner() == -1) {
				MainFrame.instance.refresh();
			} else {
				ThreadManager.ins.humanBattleHasFinished.release();
				try {
					ThreadManager.ins.roundHasFinished.acquire();
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
		if (Game.ins.battle.state == Battle.choosingActiveEffectState) {
			if (Game.ins.battle.players[Game.ins.battle.turn].isHuman()) {
				graphics.drawString("Pass turn", 14, 19);
			} else {
				graphics.drawString("Next opponent's move", 14, 19);
			}
		} else if (Game.ins.battle.state == Battle.choosingTargetCardState) {
			if (Game.ins.battle.players[Game.ins.battle.decidingPlayer].isHuman()) {
				if (Game.ins.battle.choosingTargetStateAction.type == Battle.atkAction
						&& Game.ins.battle.zones[1 - Game.ins.battle.decidingPlayer][Battle.fieldZone].isEmpty()) {
					graphics.drawString("Attack directly", 14, 19);
				} else {
					graphics.drawString("Don't choose anything", 14, 19);
				}
			} else {
				graphics.drawString("Next (op choosing)", 14, 19);
			}
		} else if (Game.ins.battle.state == Battle.executingActionState) {
			graphics.drawString("Next action", 14, 19);
		}
	}

}
