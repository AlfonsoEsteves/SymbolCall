package battle;

import java.io.Serializable;
import java.util.Random;

@SuppressWarnings("serial")
public class Rnd implements Serializable{
	
	private int seed = 17;
	
	public int nextInt(int x) {
		reset();
		return seed % x;
	}

	public Random newRandom() {
		reset();
		return new Random(seed);
	}
	
	private void reset() {
		seed = new Random(seed).nextInt(Integer.MAX_VALUE);
	}
}
