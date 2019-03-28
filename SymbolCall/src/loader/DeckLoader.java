package loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import battle.Card;
import bruteForceAI.BruteForceAI;
import battle.Player;

public class DeckLoader {
	
	public static Map<String, List<Card>> decks;
	
	public static void loadDecks() {
		decks=new HashMap<>();
		try {
			File folder = new File(Path.path+"decks\\player decks\\");
			File[] listOfFiles = folder.listFiles();
			for (File file : listOfFiles) {
			    if (file.isFile()) {
			        BufferedReader in = new BufferedReader(new FileReader(file.getAbsolutePath()));
			        String name=file.getName().split("\\.")[0];
			        List<Card> deck = new ArrayList<>();
			        String line;
			        while((line = in.readLine()) != null)
			        {
			        	line=line.trim();
			        	if(!line.equals("")){
				        	if(line.equals("description:")) {
				        		break;
				        	}
			            	Card copy=CardLoader.getPlayerCard(line);
			            	deck.add(copy);
			        	}
			        }
			        in.close();
			        decks.put(name, deck);
			    }
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
