package battle;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Action implements Serializable{
	
	public int type;
	public int info;//This can be the symbol or the target
	public int amount;//-1 if it not relevant
	
	public Action() {}
	
	public Action(int type, int info, int amount) {
		this.type=type;
		this.info=info;
		this.amount=amount;
	}
	
}
