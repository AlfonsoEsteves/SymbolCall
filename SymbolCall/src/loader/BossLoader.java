package loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

import battle.Card;
import battle.BPlayer;

public class BossLoader {
	
	public static LinkedList<BPlayer> bosses;
	
	public static void loadBosses() {
		bosses=new LinkedList<>();
		try {
			File folder = new File(Path.path+"decks\\boss decks\\");
			File[] listOfFiles = folder.listFiles();
			for (File file : listOfFiles) {
			    if (file.isFile()) {
			    	BPlayer boss=new BPlayer();
			        BufferedReader in = new BufferedReader(new FileReader(file.getAbsolutePath()));
			        boss.name=file.getName().split("\\.")[0];
			        String line;
			        while((line = in.readLine()) != null)
			        {
			        	line=line.trim();
			        	if(!line.equals("")) {
			            	Card copy=CardLoader.getComputerCard(line);
			            	boss.deck.add(copy);
			        	}
			        }
			        in.close();
			        bosses.add(boss);
			    }
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}