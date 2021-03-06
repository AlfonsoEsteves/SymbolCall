package battle;

public class BCard {

	public Card model;
	public int battleId;
	public int health;
	public int player;
	public int zone;
	public boolean turn;
	
	//This attribute is relevant when the card is in the hand
	public boolean visible;
	
	//This attribute is to collect statistics
	public boolean drawn;
	
	public BCard(Card model){
		this.model=model;
		health = model.maxHealth;
		visible = false;
	}

	public BCard copy(){
		BCard card=new BCard(model);
		card.battleId=battleId;
		card.health=health;
		card.player=player;
		card.zone=zone;
		card.turn=turn;		
		card.visible = visible;
		return card;
	}
	
}
