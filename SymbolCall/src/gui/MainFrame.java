package gui;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	private static int width=1320;
	private static int height=768;
	
	public static MainFrame instance;
	
	public MainBox mainBox;

	public MainFrame() {
		instance=this;
		setUndecorated(true);
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainBox=new MainBox(width, height);
        add(mainBox);
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
    	instance.refresh();
    	instance.setVisible(true);
    }

    public static void main(String[] args) {
    	new MainFrame();
    }

	public void refresh() {
		mainBox.battleBox.refresh();
		repaint();
	}
}