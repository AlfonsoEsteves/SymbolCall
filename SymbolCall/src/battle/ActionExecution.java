package battle;

public class ActionExecution {
	
	public Action action;
	public int executingCard;
	public int executingEffect;
	public int triggeringCard;

	public ActionExecution(Action action, int executingCard, int executingEffect, int triggeringCard) {
		this.action=action;
		this.executingCard=executingCard;
		this.executingEffect=executingEffect;
		this.triggeringCard=triggeringCard;
	}
	
}
