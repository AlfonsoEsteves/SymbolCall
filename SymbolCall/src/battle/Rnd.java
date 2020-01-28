package battle;

import java.io.Serializable;
import java.util.Random;

@SuppressWarnings("serial")
public class Rnd implements Serializable{
	
	//This attribute allows the class state to be persisted in the serialization
	private int seed = 36;
	
	public int nextInt(int x) {
		reset();
		return seed % x;
	}

	public int nextInt() {
		reset();
		return seed;
	}
	
	/*
	public Random newRandom() {
		reset();
		return new Random(seed);
	}*/
	
	private void reset() {
		seed = new Random(seed).nextInt(Integer.MAX_VALUE);
	}
}
