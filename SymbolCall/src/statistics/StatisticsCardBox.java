package statistics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import battle.BCard;
import battle.Battle;
import battle.Effect;
import gui.Box;
import gui.ImageDrawer;
import gui.battle.BattleEffectBox;

public class StatisticsCardBox extends Box {

	public static int cardHeight = 174;
	public static int cardWidth = 135;
	public static int effectWidth = 30;
	
	
	public LinkedList<BattleEffectBox> battleEffectBoxs;

	public BCard card;
	public Box container;

	public StatisticsCardBox(int x, int y, int width, int height, Box container, BCard card) {
		super(x, y, width, height, container);
		this.card = card;
		this.container=container;
		battleEffectBoxs=new LinkedList<>();
		int maxActions = 0;
		for (Effect effect : card.model.effects) {
			if (effect.actions.size() > maxActions) {
				maxActions = effect.actions.size();
			}
		}
		int effectSeparation = (cardWidth * 2 - 4) / (card.model.effects.size() + 4);
		int effectX =  (cardWidth - (effectSeparation * card.model.effects.size())) / 2 + 1;
		int effectY = 92 + (5 - maxActions) * 11 / 2;
		int effectNumber=0;
		for(Effect effect : card.model.effects) {
			int effectHeight=effect.actions.size()*BattleEffectBox.actionHeight+6;
			BattleEffectBox battleEffectBox = new BattleEffectBox(effectX, effectY, effectWidth, effectHeight, this);
			battleEffectBox.card = card;
			battleEffectBox.effectNumber = effectNumber;
			battleEffectBoxs.addLast(battleEffectBox);
			effectX += effectWidth;
			effectNumber++;
		}
	}

	@Override
	public void paint(Graphics graphics) {
		if(card!=null) {
			graphics.setColor(Color.WHITE);
			graphics.fillRect(1, 1, cardWidth - 3, cardHeight - 3);
			graphics.setColor(Color.BLACK);
			graphics.drawRect(0, 0, cardWidth - 1, cardHeight - 1);
			graphics.drawString(card.model.name + " " + card.model.maxHealth, 2, 12);
			int handCount = 0;
			int fieldCount = 0;
			int maxActions = 0;
			for (Effect effect : card.model.effects) {
				if (effect.zone == Battle.handZone) {
					handCount++;
				} else {
					fieldCount++;
				}
				if (effect.actions.size() > maxActions) {
					maxActions = effect.actions.size();
				}
			}
			int imageX = 4;
			int imageY = 16;
			int imageWidth = cardWidth - 8;
			int imageHeight = 63 + (5 - maxActions) * 11 / 2;
			graphics.drawRect(imageX - 1, imageY - 1, imageWidth + 1, imageHeight + 1);
			ImageDrawer.drawImage(card.model.image, imageX, imageY, imageWidth, imageHeight, graphics);
			int totalCount = handCount + fieldCount;
			int effectWidth = (cardWidth * 2 - 10) / (totalCount + 4);
			int effectSeparation=effectWidth+2;
			int effectX =  (cardWidth - (effectWidth * totalCount)) / 2 + 1;
			int headerY = 82 + (5 - maxActions) * 11 / 2;
			if (handCount > 0) {
				graphics.drawString("HAND", effectX + ((effectSeparation * handCount) / 2) - 14, headerY+8);
			}
			if (fieldCount > 0) {
				graphics.drawString("FIELD", effectX + effectSeparation * handCount + ((effectSeparation * fieldCount) / 2) - 15, headerY+8);
			}
			
			double fs=(int)(card.model.fieldScore*10.0)/10.0;
			double hs=(int)(card.model.handScore*10.0)/10.0;
			
			graphics.drawString("F "+fs, 5, 27+1);
			graphics.drawString("H "+hs, 5, 39+1);
			graphics.drawString("F "+fs, 5, 27-1);
			graphics.drawString("H "+hs, 5, 39-1);
			graphics.drawString("F "+fs, 5+1, 27);
			graphics.drawString("H "+hs, 5+1, 39);
			graphics.drawString("F "+fs, 5-1, 27);
			graphics.drawString("H "+hs, 5-1, 39);
			graphics.setColor(Color.WHITE);
			graphics.drawString("F "+fs, 5, 27);
			graphics.drawString("H "+hs, 5, 39);
			paintChildren(graphics);
		}
	}

}
