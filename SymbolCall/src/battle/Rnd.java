package battle;

import java.util.Random;

public class Rnd {
	
	private static Random random=new Random(785);
	
	public static int nextInt(int x) {
		return random.nextInt(x);
	}
	
	/*public static int seed=7179;
	
	public static int nextInt2(int x){
		seed = ((seed * 7621) + 1) % 32768;
		return seed%x;
	}*/
	
}
