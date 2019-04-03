package gui.battle;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

import battle.BCard;
import battle.Battle;
import battle.Effect;
import game.Game;
import gui.Box;
import gui.FontList;
import gui.ImageDrawer;
import gui.MainFrame;
import zAI.CardScoreCalculator;

@SuppressWarnings("serial")
public class BattleCardBox extends Box {

	public static int cardHeight = 217;
	public static int cardWidth = 150;

	public BCard card;

	public BattleCardBox() {
		super(0, 0, 0, 0);
		for (int i = 0; i < Battle.cardMaxEffects; i++) {
			BattleEffectBox battleEffectBox = new BattleEffectBox(0, 0, BattleEffectBox.effectWidth,
					BattleEffectBox.effectHeight);
			add(battleEffectBox);
			battleEffectBox.effectNumber = i;
		}
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D graphics = (Graphics2D) g;
		if (card != null) {
			if (card.player == 1 && card.zone == Battle.handZone && !card.visible) {
				graphics.fillRect(1, 1, cardWidth, cardHeight);
			} else {
				graphics.setFont(FontList.arial12);
				graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				graphics.setColor(Color.BLACK);
				graphics.drawImage(card.model.background, 0, 0, null);
				graphics.drawString(card.model.name + " " + card.health, 4, 13);
				int handCount = 0;
				int fieldCount = 0;
				for (Effect effect : card.model.effects) {
					if (effect.zone == Battle.handZone) {
						handCount++;
					} else {
						fieldCount++;
					}
				}
				int imageX = 6;
				int imageY = 17;
				int imageWidth = cardWidth - 12;
				int imageHeight = 121;
				ImageDrawer.drawImage(card.model.image, imageX, imageY, imageWidth, imageHeight, graphics);
				int totalCount = handCount + fieldCount;
				int effectSeparation = BattleEffectBox.effectWidth + 2;
				int effectX = (cardWidth - effectSeparation * totalCount) / 2 + 1;
				int headerY = imageY + imageHeight + 2;
				graphics.setFont(FontList.arial9);
				if (handCount > 0) {
					graphics.drawString("HAND", effectX + ((effectSeparation * handCount) / 2) - 14, headerY + 8);
				}
				if (fieldCount > 0) {
					graphics.drawString("FIELD",
							effectX + effectSeparation * handCount + ((effectSeparation * fieldCount) / 2) - 15,
							headerY + 8);
				}
				double fs = (int) (CardScoreCalculator.calculateCurrentFieldScore(card) * 10.0) / 10.0;
				double hs = (int) (card.model.handScore * 10.0) / 10.0;

				graphics.drawString("F " + fs, 5, 27 + 1);
				graphics.drawString("H " + hs, 5, 39 + 1);
				graphics.drawString("F " + fs, 5, 27 - 1);
				graphics.drawString("H " + hs, 5, 39 - 1);
				graphics.drawString("F " + fs, 5 + 1, 27);
				graphics.drawString("H " + hs, 5 + 1, 39);
				graphics.drawString("F " + fs, 5 - 1, 27);
				graphics.drawString("H " + hs, 5 - 1, 39);
				graphics.setColor(Color.WHITE);
				graphics.drawString("F " + fs, 5, 27);
				graphics.drawString("H " + hs, 5, 39);

				for (int i = 0; i < Battle.cardMaxEffects; i++) {
					if (i < card.model.effects.size()) {
						BattleEffectBox battleEffectBox = (BattleEffectBox)getComponent(i);
						battleEffectBox.setBounds(effectX + i * effectSeparation, headerY + 4,
								BattleEffectBox.effectWidth, BattleEffectBox.effectHeight);
					}
				}

				paintChildren(graphics);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if (Game.instance.battle.turn == 0 && Game.instance.battle.state == Battle.choosingTargetCardState) {
			int zone;
			if (Game.instance.battle.choosingTargetStateAction.type == Battle.atkAction) {
				zone = Battle.fieldZone;
			} else if (Game.instance.battle.choosingTargetStateAction.type == Battle.wdrAction) {
				zone = Battle.fieldZone;
			} else if (Game.instance.battle.choosingTargetStateAction.type == Battle.invAction) {
				zone = Battle.handZone;
			} else if (Game.instance.battle.choosingTargetStateAction.type == Battle.dscAction) {
				zone = Battle.handZone;
			} else {
				throw new RuntimeException();
			}
			if (card.zone == zone) {
				Game.instance.battle.setChosenTarget(card.battleId, Battle.noneAISimulating);
				MainFrame.instance.refresh();
			}
		}
	}

	@Override
	public void refresh() {
		for (Component component : getComponents()) {
			BattleEffectBox battleEffectBox = (BattleEffectBox)component;
			battleEffectBox.card = card;
		}
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		Box parent = (Box) getParent();
		parent.setComponentZOrder(this, 0);
		parent.mouseEntered(e);
		repaint();
	}

}
