package gui.buycard;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import battle.BCard;
import game.Game;
import gui.Box;
import gui.BoxButton;
import gui.MainFrame;
import gui.battle.BattleCardBox;

@SuppressWarnings("serial")
public class BuyCardBox extends Box {

	public BattleCardBox battleCardBoxBuy;

	public BuyCardBox(int x, int y, int width, int height) {
		super(x, y, width, height);

		BoxButton goBackButton = new BoxButton(100, 600, 100, 50) {
			@Override
			public void mousePressed(MouseEvent arg0) {
				MainFrame.instance.enterBox(MainFrame.instance.mainBox);
			}

			@Override
			public String getText() {
				return "Go Back";
			}
		};
		add(goBackButton);

		BoxButton buyButton = new BoxButton(800, 400, 100, 50) {
			@Override
			public void mousePressed(MouseEvent arg0) {
				Game.instance.buyAvailableToBuy();
				MainFrame.instance.refresh();
			}

			@Override
			public String getText() {
				return "Buy";
			}
		};
		add(buyButton);

		battleCardBoxBuy = new BattleCardBox();
		battleCardBoxBuy.setBounds(830, 30, BattleCardBox.cardWidth, BattleCardBox.cardHeight);
		add(battleCardBoxBuy);
	}

	@Override
	public void refresh() {
		battleCardBoxBuy.card = new BCard(Game.instance.availableToBuy);
		battleCardBoxBuy.refresh();
	}

	@Override
	public void paint(Graphics graphics) {
		graphics.setColor(Color.black);
		graphics.drawString("Gold: " + Game.instance.gold, 600, 500);
		paintChildren(graphics);
	}

}
