package gui.battle;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import battle.Battle;
import battle.ComputerAI;
import game.BattleExecutor;
import game.Game;
import gui.Box;
import gui.MainFrame;

@SuppressWarnings("serial")
public class BattleFirstButtonBox extends Box {

	public BattleFirstButtonBox(int x, int y, int width, int height, Box container) {
		super(x, y, width, height, container);
		container.addKeyListener(this);
	}
	
	@Override	
	public void mousePressed(MouseEvent arg0) {
		if(Game.battle.state==Battle.choosingActiveEffectState) {
			ComputerAI computerAI=Game.battle.players[Game.battle.turn].computerAI;
			if(computerAI==null) {
				Game.battle.passTurn();
			}
			MainFrame.instance.refresh();
		}
		else {
			next();
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e){
		if(e.getKeyCode()==KeyEvent.VK_SPACE) {
			next();
		}
	}
	
	public void next() {
		if(Game.battle.state==Battle.choosingActiveEffectState) {
			ComputerAI computerAI=Game.battle.players[Game.battle.turn].computerAI;
			if(computerAI!=null) {
				computerAI.play(Game.battle);
			}
			MainFrame.instance.refresh();
		}
		else if(Game.battle.state==Battle.choosingTargetCardState) {
			ComputerAI computerAI=Game.battle.players[Game.battle.decidingPlayer].computerAI;
			if(computerAI==null) {
				Game.battle.setChosenTarget(-1);
				MainFrame.instance.refresh();
			}
			else {
				computerAI.chooseTarget(Game.battle);
				MainFrame.instance.refresh();
			}
		}
		else if(Game.battle.state==Battle.executingActionState) {
			Game.battle.executeAction();
			if(Game.battle.winner() == -1) {
				MainFrame.instance.refresh();
			}
			else {
				Game.executeRound();
				MainFrame.instance.enterBox(MainFrame.instance.mainBox);
			}
		}
	}

	@Override
	public void paint(Graphics graphics) {
		graphics.drawRect(0, 0, getWidth()-1, getHeight()-1);
		if(Game.battle.state==Battle.choosingActiveEffectState) {
			ComputerAI computerAI=Game.battle.players[Game.battle.turn].computerAI;
			if(computerAI==null) {
				graphics.drawString("Pass turn", 14, 19);
			}
			else {
				graphics.drawString("Next opponent's move", 14, 19);
			}
		}
		else if(Game.battle.state==Battle.choosingTargetCardState) {
			ComputerAI computerAI=Game.battle.players[Game.battle.decidingPlayer].computerAI;
			if(computerAI==null) {
				if(Game.battle.choosingTargetStateAction.type==Battle.atkAction && Game.battle.zones[1-Game.battle.decidingPlayer][Battle.fieldZone].isEmpty()) {
					graphics.drawString("Attack directly", 14, 19);
				}
				else {
					graphics.drawString("Don't choose anything", 14, 19);
				}
			}
			else {
				graphics.drawString("Next (op choosing)", 14, 19);
			}
		}
		else if(Game.battle.state==Battle.executingActionState) {
			graphics.drawString("Next action", 14, 19);
		}
	}

}
