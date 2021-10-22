package loader;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import battle.Battle;
import battle.Card;
import game.Game;

public class ImageLoader {

	public static Image[] targets;
	public static Image[] smallSymbols;
	public static Image[] midSymbols;
	public static Image[] bigSymbols;
	public static Image[] cardFront;
	public static Image availableEffect;
	public static Image unavailableEffect;
	//public static Image cardBack;
	
	public static void loadImages(){
		try {
			/*actions=new BufferedImage[5];
			String actionsPath=Path.path+"images\\actions\\";
			actions[Battle.atkAction]=ImageIO.read(new File(actionsPath+"atk.png"));
			actions[Battle.invAction]=ImageIO.read(new File(actionsPath+"inv.png"));
			actions[Battle.wdrAction]=ImageIO.read(new File(actionsPath+"wdr.png"));
			actions[Battle.dscAction]=ImageIO.read(new File(actionsPath+"dsc.png"));
			actions[Battle.cllAction]=ImageIO.read(new File(actionsPath+"cll.png"));*/
			
			/*numbers=new BufferedImage[10];
			for(int i=0;i<10;i++) {
				numbers[i]=ImageIO.read(new File(actionsPath+i+".png"));
			}*/

			targets=new BufferedImage[5];
			String targetsPath=Path.path+"images/targets/";
			targets[Battle.slTarget]=ImageIO.read(new File(targetsPath+"sl.png"));
			targets[Battle.chTarget]=ImageIO.read(new File(targetsPath+"ch.png"));
			targets[Battle.trTarget]=ImageIO.read(new File(targetsPath+"tr.png"));
			targets[Battle.opTarget]=ImageIO.read(new File(targetsPath+"op.png"));
			targets[Battle.owTarget]=ImageIO.read(new File(targetsPath+"ow.png"));

			bigSymbols=new Image[Battle.symbolFamilies*Battle.symbolFamilySubtypesPlusOne];
			midSymbols=new Image[Battle.symbolFamilies*Battle.symbolFamilySubtypesPlusOne];
			smallSymbols=new Image[Battle.symbolFamilies*Battle.symbolFamilySubtypesPlusOne];
			String symbolsPath=Path.path+"images/symbols/";
			
			int bigSize=28;
			for(int i=0;i<Battle.symbolFamilySubtypesPlusOne;i++) {
				Image r=ImageIO.read(new File(symbolsPath+"R"+i+".png"));
				Image g=ImageIO.read(new File(symbolsPath+"G"+i+".png"));
				Image b=ImageIO.read(new File(symbolsPath+"B"+i+".png"));
				Image y=ImageIO.read(new File(symbolsPath+"Y"+i+".png"));
				if(i>0) {
					bigSize=24;
				}
				bigSymbols[Battle.rdSymbol*Battle.symbolFamilySubtypesPlusOne+i]=r.getScaledInstance(bigSize, bigSize, Image.SCALE_SMOOTH);
				bigSymbols[Battle.grSymbol*Battle.symbolFamilySubtypesPlusOne+i]=g.getScaledInstance(bigSize, bigSize, Image.SCALE_SMOOTH);
				bigSymbols[Battle.blSymbol*Battle.symbolFamilySubtypesPlusOne+i]=b.getScaledInstance(bigSize, bigSize, Image.SCALE_SMOOTH);
				bigSymbols[Battle.ylSymbol*Battle.symbolFamilySubtypesPlusOne+i]=y.getScaledInstance(bigSize, bigSize, Image.SCALE_SMOOTH);
				midSymbols[Battle.rdSymbol*Battle.symbolFamilySubtypesPlusOne+i]=r.getScaledInstance(17, 17, Image.SCALE_SMOOTH);
				midSymbols[Battle.grSymbol*Battle.symbolFamilySubtypesPlusOne+i]=g.getScaledInstance(17, 17, Image.SCALE_SMOOTH);
				midSymbols[Battle.blSymbol*Battle.symbolFamilySubtypesPlusOne+i]=b.getScaledInstance(17, 17, Image.SCALE_SMOOTH);
				midSymbols[Battle.ylSymbol*Battle.symbolFamilySubtypesPlusOne+i]=y.getScaledInstance(17, 17, Image.SCALE_SMOOTH);
				smallSymbols[Battle.rdSymbol*Battle.symbolFamilySubtypesPlusOne+i]=r.getScaledInstance(12, 12, Image.SCALE_SMOOTH);
				smallSymbols[Battle.grSymbol*Battle.symbolFamilySubtypesPlusOne+i]=g.getScaledInstance(12, 12, Image.SCALE_SMOOTH);
				smallSymbols[Battle.blSymbol*Battle.symbolFamilySubtypesPlusOne+i]=b.getScaledInstance(12, 12, Image.SCALE_SMOOTH);
				smallSymbols[Battle.ylSymbol*Battle.symbolFamilySubtypesPlusOne+i]=y.getScaledInstance(12, 12, Image.SCALE_SMOOTH);
			}

			String othersPath=Path.path+"images/card background/";
			cardFront=new Image[6];
			cardFront[0]=ImageIO.read(new File(othersPath+"cardfront_rd.png"));
			cardFront[1]=ImageIO.read(new File(othersPath+"cardfront_yl.png"));
			cardFront[2]=ImageIO.read(new File(othersPath+"cardfront_gr.png"));
			cardFront[3]=ImageIO.read(new File(othersPath+"cardfront_aq.png"));
			cardFront[4]=ImageIO.read(new File(othersPath+"cardfront_bl.png"));
			cardFront[5]=ImageIO.read(new File(othersPath+"cardfront_pr.png"));
			//cardBack=ImageIO.read(new File(othersPath+"cardback.png"));
			availableEffect=ImageIO.read(new File(othersPath+"availableeffect.png"));
			unavailableEffect=ImageIO.read(new File(othersPath+"unavailableeffect.png"));
			
			loadCardImages();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void loadCardImages() {
		try {
			File folder = new File(Path.path+"images/player cards");
			File[] listOfFiles = folder.listFiles();
			for (File file : listOfFiles) {
			    if (file.isFile() && file.getName().contains("png")) {
			    	String cardName=file.getName().split("\\.")[0];
			    	boolean found=false;
			    	for(Card card : Game.instance.playerCards) {
			    		if(card.name.equals(cardName)) {
			    			card.image=ImageIO.read(new File(file.getAbsolutePath()));
					        card.background=cardFront[card.name.toCharArray()[0]%6];
			    			found=true;
			    		}
			    	}
			    	if(!found) {
			    		throw new RuntimeException("Not found "+cardName);
			    	}
			    }
			}
			folder = new File(Path.path+"images/computer cards");
			listOfFiles = folder.listFiles();
			for (File file : listOfFiles) {
			    if (file.isFile()) {
			    	String cardName=file.getName().split("\\.")[0];
			    	boolean found=false;
			    	for(Card card : Game.instance.computerCards) {
			    		if(card.name.equals(cardName)) {
			    			card.image=ImageIO.read(new File(file.getAbsolutePath()));
			    			found=true;
			    		}
			    	}
			    	if(!found) {
			    		throw new RuntimeException();
			    	}
			    }
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
