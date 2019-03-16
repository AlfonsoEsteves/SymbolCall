package gui.main;

import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import game.Game;
import game.ThreadManager;
import gui.AbstractButtonBox;
import gui.Box;
import gui.MainFrame;
import persistence.Persistence;

@SuppressWarnings("serial")
public class MainBox extends Box {
	
	private JList<Object> list;
	
	public MainBox(int x, int y, int width, int height, Box container){
		super(x, y, width, height, container);
		
		list = new JList<Object>();
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1);
		
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setBounds(300, 10, 120, 200);
		add(listScroller);
		
		AbstractButtonBox startBattleButtonBox = new AbstractButtonBox("Start Battle", 50, 50, 100, 100, null) {
			@Override
			public void mousePressed(MouseEvent arg0) {
				synchronized(ThreadManager.instance) {
					ThreadManager.instance.notify();
				}
				try {
					synchronized(MainFrame.instance) {
						// This is meant to be notified by the BattleExecutorManual
						// when the battle has been initialized
						MainFrame.instance.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				MainFrame.instance.enterBox(MainFrame.instance.battleBox);
			}
		};
		add(startBattleButtonBox);
		
		AbstractButtonBox deckBuildingButtonBox = new AbstractButtonBox("Deck Building", 120, 250, 100, 100, null) {			
			@Override
			public void mousePressed(MouseEvent arg0) {
				MainFrame.instance.enterBox(MainFrame.instance.deckBuildingBox);
			}
		};
		add(deckBuildingButtonBox);
		
		AbstractButtonBox saveButtonBox = new AbstractButtonBox("Save", 920, 350, 100, 100, null) {			
			@Override
			public void mousePressed(MouseEvent arg0) {
				Persistence.serialize();
			}
		};
		add(saveButtonBox);
		
		AbstractButtonBox loadButtonBox = new AbstractButtonBox("Load", 920, 450, 100, 100, null) {			
			@Override
			public void mousePressed(MouseEvent arg0) {
				Persistence.deserialize();
			}
		};
		add(loadButtonBox);
	}
	
	@Override
	public void refresh() {
		list.setListData(Game.ins.players.toArray());
		//startBattleButtonBox.refresh();
	}
	
}
