package persistence;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import game.Game;
import loader.ImageLoader;

public class Persistence {
	
	public static String path = "C:\\Users\\Usuario\\Documents\\game.xml";

	public static void serialize() {
		XMLEncoder encoder = null;
		try {
			encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(path)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		encoder.writeObject(Game.ins);
		encoder.close();
	}
	
	public static void deserialize() {
		try {
			FileInputStream fis = new FileInputStream(path);
			XMLDecoder decoder = new XMLDecoder(fis);
			Game.ins = (Game)decoder.readObject();
			decoder.close();
			fis.close();
			ImageLoader.loadImages();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
