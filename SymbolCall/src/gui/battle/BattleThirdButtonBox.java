package gui.battle;

import java.awt.event.MouseEvent;

import gui.BoxButton;

@SuppressWarnings("serial")
public class BattleThirdButtonBox extends BoxButton {

	public BattleThirdButtonBox(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		System.out.println("surrender");
	}

	@Override
	public String getText() {
		return "Surrender";
	}

}
