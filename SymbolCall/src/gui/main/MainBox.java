package gui.main;

import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import game.Game;
import game.ThreadManager;
import gui.Box;
import gui.BoxButton;
import gui.MainFrame;
import persistence.Persistence;

@SuppressWarnings("serial")
public class MainBox extends Box {
	
	private JList<Object> list;
	
	public MainBox(int x, int y, int width, int height){
		super(x, y, width, height);
		
		list = new JList<Object>();
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1);
		
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setBounds(300, 10, 120, 200);
		add(listScroller);
		
		BoxButton startBattleButtonBox = new BoxButton(50, 50, 100, 100) {
			@Override
			public void mousePressed(MouseEvent arg0) {
				ThreadManager.instance.roundCanBeStarted.release();
				// The ThreadExecutor will call BattleExecutorManual
				// And BattleExecutorManual will make the frame enter the battle box
			}

			@Override
			public String getText() {
				return "Start Battle";
			}
		};
		add(startBattleButtonBox);
		
		BoxButton deckBuildingButtonBox = new BoxButton(120, 250, 100, 100) {			
			@Override
			public void mousePressed(MouseEvent arg0) {
				MainFrame.instance.enterBox(MainFrame.instance.deckBuildingBox);
			}

			@Override
			public String getText() {
				return "Deck Building";
			}
		};
		add(deckBuildingButtonBox);
		
		BoxButton saveButtonBox = new BoxButton(920, 350, 100, 100) {			
			@Override
			public void mousePressed(MouseEvent arg0) {
				Persistence.serialize("save");
			}

			@Override
			public String getText() {
				return "Save";
			}
		};
		add(saveButtonBox);
		
		BoxButton loadButtonBox = new BoxButton(920, 450, 100, 100) {			
			@Override
			public void mousePressed(MouseEvent arg0) {
				Persistence.deserialize("save");
			}

			@Override
			public String getText() {
				return "Load";
			}
		};
		add(loadButtonBox);
	}
	
	@Override
	public void refresh() {
		list.setListData(Game.instance.players.toArray());
	}
	
}
