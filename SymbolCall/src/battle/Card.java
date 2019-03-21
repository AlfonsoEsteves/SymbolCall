package battle;

import java.awt.Image;
import java.io.Serializable;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class Card implements Serializable {
		
	//Fixed values
	public String name;
	public int maxHealth;
	public LinkedList<Effect> effects;
	public transient Image image;
	public transient Image background;
	
	//AI values
	public transient double fieldScore;
	public transient double handScore;
	public transient double fieldDurableActive;//Atomic scores
	public transient double fieldDurablePassive;
	public transient double fieldOnceActive;
	public transient double fieldOncePassive;
	public transient double handDurableActive;
	public transient double handDurablePassive;
	public transient double handOnceActive;
	public transient double handOncePassive;
	public transient double handTotalScoreActive;//This is to weight how likely is to be used a active effect
	public transient double fieldTotalScoreActive;
	public transient double probLeaveHandEachTurn;//To calculate the longevity
	public transient double probLeaveFieldEachTurn;
	public transient double fieldLongevity;//Longevity
	public transient double handLongevity;
	
	public Card() {
		effects=new LinkedList<>();
	}

	@Override
	public String toString() {
		return name;
	}
	
}
