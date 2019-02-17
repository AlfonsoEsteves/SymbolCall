package statistics;

import javax.swing.JFrame;

import loader.CardLoader;
import loader.DeckLoader;
import loader.ImageLoader;

@SuppressWarnings("serial")
public class StatisticsFrame extends JFrame {
	
	private static int width=1250;
	private static int height=768;
	
	public static StatisticsFrame instance;
	
	public StatisticsBox mainBox;

	public StatisticsFrame() {
		instance=this;
		setUndecorated(true);
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainBox=new StatisticsBox(width, height);
        add(mainBox);
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
    	instance.setVisible(true);
    }

    public static void main(String[] args) {
		CardLoader.loadCards();
		ImageLoader.loadImages();
		DeckLoader.loadDecks();
    	new StatisticsFrame();
    }
}