package gui.battle;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import battle.Battle;
import battle.ComputerAI;
import game.BattleExecutor;
import gui.Box;
import gui.MainFrame;

@SuppressWarnings("serial")
public class BattleFirstButtonBox extends Box {

	public BattleFirstButtonBox(int x, int y, int width, int height, Box container) {
		super(x, y, width, height, container);
		MainFrame.instance.addKeyListener(this);
	}
	
	@Override	
	public void mousePressed(MouseEvent arg0) {
		if(BattleExecutor.battle.state==Battle.choosingActiveEffectState) {
			ComputerAI computerAI=BattleExecutor.battle.players[BattleExecutor.battle.turn].computerAI;
			if(computerAI==null) {
				BattleExecutor.battle.passTurn();
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
		if(BattleExecutor.battle.state==Battle.choosingActiveEffectState) {
			ComputerAI computerAI=BattleExecutor.battle.players[BattleExecutor.battle.turn].computerAI;
			if(computerAI!=null) {
				computerAI.play(BattleExecutor.battle);
			}
			MainFrame.instance.refresh();
		}
		else if(BattleExecutor.battle.state==Battle.choosingTargetCardState) {
			ComputerAI computerAI=BattleExecutor.battle.players[BattleExecutor.battle.decidingPlayer].computerAI;
			if(computerAI==null) {
				BattleExecutor.battle.setChosenTarget(-1);
				MainFrame.instance.refresh();
			}
			else {
				computerAI.chooseTarget(BattleExecutor.battle);
				MainFrame.instance.refresh();
			}
		}
		else if(BattleExecutor.battle.state==Battle.executingActionState) {
			BattleExecutor.battle.executeAction();
			MainFrame.instance.refresh();
		}
	}

	@Override
	public void paint(Graphics graphics) {
		graphics.drawRect(0, 0, getWidth()-1, getHeight()-1);
		if(BattleExecutor.battle.state==Battle.choosingActiveEffectState) {
			ComputerAI computerAI=BattleExecutor.battle.players[BattleExecutor.battle.turn].computerAI;
			if(computerAI==null) {
				graphics.drawString("Pass turn", 14, 19);
			}
			else {
				graphics.drawString("Next opponent's move", 14, 19);
			}
		}
		else if(BattleExecutor.battle.state==Battle.choosingTargetCardState) {
			ComputerAI computerAI=BattleExecutor.battle.players[BattleExecutor.battle.decidingPlayer].computerAI;
			if(computerAI==null) {
				if(BattleExecutor.battle.choosingTargetStateAction.type==Battle.atkAction && BattleExecutor.battle.zones[1-BattleExecutor.battle.decidingPlayer][Battle.fieldZone].isEmpty()) {
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
		else if(BattleExecutor.battle.state==Battle.executingActionState) {
			graphics.drawString("Next action", 14, 19);
		}
	}

}
