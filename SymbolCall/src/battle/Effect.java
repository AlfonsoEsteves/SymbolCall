package battle;

import java.io.Serializable;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class Effect implements Serializable{
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
