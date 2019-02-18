package loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

import battle.Card;
import battle.BPlayer;

public class DeckLoader {
	
	public static LinkedList<BPlayer> decks;
	
	public static void loadDecks() {
		decks=new LinkedList<>();
		try {
			File folder = new File(Path.path+"decks\\player decks\\");
			File[] listOfFiles = folder.listFiles();
			for (File file : listOfFiles) {
			    if (file.isFile()) {
			    	BPlayer player=new BPlayer();
			        BufferedReader in = new BufferedReader(new FileReader(file.getAbsolutePath()));
			        player.name=file.getName().split("\\.")[0];
			        String line;
			        while((line = in.readLine()) != null)
			        {
			        	line=line.trim();
			        	if(!line.equals("")){
				        	if(line.equals("description:")) {
				        		break;
				        	}
			            	Card copy=CardLoader.getPlayerCard(line);
			            	player.deck.add(copy);
			        	}
			        }
			        in.close();
			        decks.add(player);
			    }
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static LinkedList<Card> getPlayer(String name) {
		for(BPlayer player : decks) {
			if(player.name.equals(name)) {
				return player.deck;
			}
		}
		return null;
	}
}
