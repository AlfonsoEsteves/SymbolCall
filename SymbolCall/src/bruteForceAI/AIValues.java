package bruteForceAI;

public class AIValues {
	
	//Was changed from 2.0 because 'Costoso' wasn't worth invoking
	public static double healthFactor=1.5;

	public static int iterations=20;
	public static double initialScore=5.0;//If there are a lot of iterations, this does not matter
	
	public static double minTurnsToGetKilled=1.5;
	public static double turnsToGetKilledPerHealthPoint=0.1;
	
	public static double chooseDsc=10;
	public static double chooseInv=12;
	public static double chooseWdr=5;
	
	public static double minActiveScore=0.5;
	
	public static double fieldDamagefactor=2.0;
	
	public static double minSequenceUnlikelihood=1.0;
	public static double sequenceUnlikelihoodPerSymbol=1.0;
	
	public static double infiniteLongetivity=10;

}
