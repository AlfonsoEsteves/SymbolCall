package bruteForceAI;

import java.util.LinkedList;

import battle.Battle;

public class Node{
	
	public String debuggingName;
	public Battle scenario;
	public int passedTurns;
	public boolean finished;//true if 2 turns passed, or if the game ended
	
	//0 would be player zero's victory, 1 would be player one's victory
	public double initialScore;//The score of the current scenario
	public double finalScore;//The score of the final projection
	
	//This is not the move to do from the current scenario. 
	//It is the previous move that got us here
	//A null choice means either to pass the turn, or to not target anything
	public int chosenCard;
	public int chosenEffect;
	
	public LinkedList<Node> continuations;
	public Node bestContinuation;
	
	public Node() {
		continuations=new LinkedList<>();
	}

	public void calculateFinalScore() { 
		if(scenario.decidingPlayer==0) {
			finalScore=999;
			for(Node continuation : continuations) {
				if(continuation.finalScore!=-1) {
					if(continuation.finalScore<finalScore) {
						finalScore=continuation.finalScore;
						bestContinuation=continuation;
					}
				}
			}
		}
		else {
			finalScore=-999;
			for(Node continuation : continuations) {
				if(continuation.finalScore!=-1) {
					if(continuation.finalScore>finalScore) {
						finalScore=continuation.finalScore;
						bestContinuation=continuation;
					}
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return "'"+debuggingName+"' ("+chosenCard+","+chosenEffect+") "+initialScore+"/"+finalScore+" scenario-turn: "+scenario.turn;
	}
	
}
