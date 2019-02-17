package gui.battle;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import battle.Battle;
import game.Game;
import gui.Box;
import gui.MainFrame;

public class BattleCardsRowBox extends Box {
	
	private int player;
	private int zone;
	private BattleMiddleBox container;
	private BattleCardBox[] battleCardBoxes=new BattleCardBox[Battle.handAndFieldMaxSize];
	private boolean atTop;
	
	public BattleCardsRowBox(int x, int y, int width, int height, BattleMiddleBox container, int player, int zone, boolean atTop){
		super(x, y, width, height, container);
		this.container=container;
		this.player=player;
		this.zone=zone;
		this.atTop=atTop;
		battleCardBoxes=new BattleCardBox[Battle.handAndFieldMaxSize];
		for(int i=0;i<Battle.handAndFieldMaxSize;i++){
			battleCardBoxes[i]=new BattleCardBox(this);
			add(battleCardBoxes[i]);
		}
	}
	
	@Override
	public void refresh() {
		int space=BattleCardBox.cardWidth+2;
		if(Game.battle.zones[player][zone].size()>5) {
			space=(getWidth()-5)/Game.battle.zones[player][zone].size()-1;
		}
		int cardX=3;
		for(int i=0;i<Battle.handAndFieldMaxSize;i++){
			if(i<Game.battle.zones[player][zone].size()){
				battleCardBoxes[i].card=Game.battle.cards[Game.battle.zones[player][zone].get(i)];
				for(int j=0;j<Battle.cardMaxEffects;j++) {
					BattleEffectBox battleEffectBox=battleCardBoxes[i].battleEffectBoxs.get(j);
					battleEffectBox.card=battleCardBoxes[i].card;
				}
				battleCardBoxes[i].setBounds(cardX, 0, BattleCardBox.cardWidth, BattleCardBox.cardHeight);
				battleCardBoxes[i].setVisible(true);
				cardX+=space;
			}
			else{
				battleCardBoxes[i].card=null;
				battleCardBoxes[i].setVisible(false);
			}
		}
	}
	
	@Override
	public void paint(Graphics graphics){
		paintChildren(graphics);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		container.atTop=atTop;
		MainFrame.instance.refresh();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		MainFrame.instance.refresh();
	}
}
