package battle;

public abstract class ComputerAI{
	
	// Automated testing can make a single player have multiple battles at the same time.
	// So it will need a new instance of a ComputerAI for each battle
	// Note that some AIs need a random number generator
	public abstract static class ComputerAIFactory{
		public abstract ComputerAI create(int player, int rndSeed);
	}
	
	public static class NullComputerAIFactory extends ComputerAIFactory{
		public ComputerAI create(int player, int rndSeed) {
			return null;
		}
	}
	
	public int AIplayer;
	
	public ComputerAI(int AIplayer) {
		this.AIplayer = AIplayer;
	}

	public abstract void play(Battle battle);

	public abstract void chooseTarget(Battle battle);

	public void notifyUnexpectedEffectTriggered() {}

}
