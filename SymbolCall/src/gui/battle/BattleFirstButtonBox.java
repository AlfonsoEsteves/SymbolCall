package gui.battle;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import battle.Battle;
import game.Game;
import game.ThreadManager;
import gui.BoxButton;
import gui.MainFrame;

@SuppressWarnings("serial")
public class BattleFirstButtonBox extends BoxButton {

	public BattleFirstButtonBox(int x, int y, int width, int height) {
		super(x, y, width, height);
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
				if (Game.instance.battle.players[Game.instance.battle.turn].isHuman()) {
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
	public String getText() {
		if (Game.instance.battle.state == Battle.choosingActiveEffectState) {
			if (Game.instance.battle.players[Game.instance.battle.turn].isHuman()) {
				return "Pass turn";
			} else {
				return "Next opponent's move";
			}
		} else if (Game.instance.battle.state == Battle.choosingTargetCardState) {
			if (Game.instance.battle.players[Game.instance.battle.turn].isHuman()) {
				if (Game.instance.battle.choosingTargetStateAction.type == Battle.atkAction
						&& Game.instance.battle.zones[1 - Game.instance.battle.turn][Battle.fieldZone].isEmpty()) {
					return "Attack directly";
				} else {
					return "Don't choose anything";
				}
			} else {
				return "Next (op choosing)";
			}
		} else if (Game.instance.battle.state == Battle.executingActionState) {
			return "Next action";
		}
		return null;
	}

}
