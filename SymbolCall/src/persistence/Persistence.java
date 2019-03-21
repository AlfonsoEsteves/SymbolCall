package persistence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import game.Game;
import loader.ImageLoader;

public class Persistence {

	public static String path = "C:\\Users\\Usuario\\Documents\\";

	public static void serialize(String fileName) {
		try {
			FileOutputStream fos = new FileOutputStream(path + fileName + ".ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(Game.ins);

			fos.close();	
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void deserialize(String fileName) {
		try {
			FileInputStream fis = new FileInputStream(path + fileName + ".ser");
			ObjectInputStream ois = new ObjectInputStream(fis);

			Game.ins = (Game) ois.readObject();

			fis.close();
			ois.close();
			
			ImageLoader.loadImages();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}
