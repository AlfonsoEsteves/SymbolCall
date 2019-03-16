package battle;

import java.awt.Image;
import java.util.LinkedList;

public class Card {
		
	//Fixed values
	public String name;
	public int maxHealth;
	public LinkedList<Effect> effects;
	public transient Image image;
	public transient Image background;
	
	//AI values
	public double fieldScore;
	public double handScore;
	public double fieldDurableActive;//Atomic scores
	public double fieldDurablePassive;
	public double fieldOnceActive;
	public double fieldOncePassive;
	public double handDurableActive;
	public double handDurablePassive;
	public double handOnceActive;
	public double handOncePassive;
	public double handTotalScoreActive;//This is to weight how likely is to be used a active effect
	public double fieldTotalScoreActive;
	public double probLeaveHandEachTurn;//To calculate the longevity
	public double probLeaveFieldEachTurn;
	public double fieldLongevity;//Longevity
	public double handLongevity;
	
	public Card() {
		effects=new LinkedList<>();
	}

	@Override
	public String toString() {
		return name;
	}
	
}
