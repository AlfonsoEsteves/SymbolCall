package battle;

import java.util.LinkedList;

public class Effect {
	//Fixed values
	public int zone;
	public LinkedList<Integer> sequence;
	public LinkedList<Action> actions;
	
	public Effect() {}
	
	public Effect(int zone) {
		this.zone=zone;
		sequence=new LinkedList<>();
		actions=new LinkedList<>();
	}
	
}
