package battle;

import java.util.Random;

public class Rnd {
	
	private static Random random=new Random(792);
	
	public static int nextInt(int x) {
		return random.nextInt(x);
	}

	public static Random newRandom() {
		return new Random(random.nextLong());
	}
	
}
