package bruteForceAI;

import java.util.Collections;
import java.util.LinkedList;

import battle.BCard;
import battle.Battle;
import battle.ComputerAI;
import battle.Effect;

public class BruteForceAI extends ComputerAI {

	public static final int maxCheckedNodes = 3500;
	public static final int leeway = 150;
	public static final int nodesArraySize = maxCheckedNodes + leeway;
	public static final int maxOptions = 50;

	public int checkedNodes;

	// The first stack checks the first option, the second checks the second, and so
	// on
	@SuppressWarnings("unchecked")
	private LinkedList<Node>[] nodeStacks = new LinkedList[maxOptions];

	// This stack is used to add the nodes to the finalScoreCalculationQueue in the
	// right order
	private LinkedList<Node> finalScoreCalculationStack;

	// I iterate over this queue to calculate the final score
	private LinkedList<Node> finalScoreCalculationQueue;

	public LinkedList<Node> plan;
	
	private Node[] nodes;
	
	public BruteForceAI() {
		for (int i = 0; i < maxOptions; i++) {
			nodeStacks[i] = new LinkedList<>();
		}
		finalScoreCalculationStack = new LinkedList<>();
		finalScoreCalculationQueue = new LinkedList<>();
		plan = new LinkedList<>();
		
		nodes=new Node[BruteForceAI.nodesArraySize];
		for(int i=0;i<BruteForceAI.nodesArraySize;i++){
			nodes[i]=new Node();
		}
	}

	@Override
	public void play(Battle battle) {
		if (plan.isEmpty()) {
			createPlan(battle);
		}
		Node node = plan.removeFirst();
		if (node.chosenCard == -1) {
			battle.passTurn();
		} else {
			battle.executeActiveEffect(node.chosenCard, node.chosenEffect);
		}
	}

	@Override
	public void chooseTarget(Battle battle) {
		if (plan.isEmpty()) {
			createPlan(battle);
		}
		Node node = plan.removeFirst();
		battle.setChosenTarget(node.chosenCard);
	}

	private void createPlan(Battle battle) {
		checkedNodes = 0;
		Node firstNode = getNewNode(battle, -1, -1, 0, "");
		nodeStacks[0].addFirst(firstNode);
		while (true) {
			Node node = null;
			for (int i = 0; i < maxOptions; i++) {
				if (!nodeStacks[i].isEmpty()) {
					node = nodeStacks[i].removeFirst();
					break;
				}
			}
			if (node == null) {
				break;
			}
			checkNode(node);
			if (checkedNodes >= maxCheckedNodes) {
				// I only make the expanded nodes count break when the current node is a leaf
				// This way, every node is "taken to the end" at least with one branch
				if (node.finished) {
					for (int i = 0; i < maxOptions; i++) {
						nodeStacks[i].clear();
					}
					break;
				}
				if (checkedNodes >= nodesArraySize) {
					throw new RuntimeException("Nodes array overflow");
				}
			}
		}

		finalScoreCalculationStack.addFirst(firstNode);
		while (!finalScoreCalculationStack.isEmpty()) {
			Node node = finalScoreCalculationStack.removeFirst();
			finalScoreCalculationQueue.addFirst(node);// I add the element first to invert the order
			if (!node.continuations.isEmpty()) {
				for (Node continuation : node.continuations) {
					if (continuation.continuations.isEmpty()) {
						if (continuation.finished) {
							continuation.finalScore = continuation.initialScore;
						}
					} else {
						finalScoreCalculationStack.addFirst(continuation);
					}
				}
			}
		}

		while (!finalScoreCalculationQueue.isEmpty()) {
			Node node = finalScoreCalculationQueue.removeFirst();
			node.calculateFinalScore();
		}

		/*
		 * debugPrintFinalScore(firstNode); System.out.println();
		 */

		// The AI projects perfectly its own moves
		// but it doesn't do it perfectly with its opponent's moves
		// So it has to create a new plan each times its opponent moves
		// That is why I select the following nodes only up to my rival's first move
		Node node = firstNode;
		while (true) {
			if (node.scenario.decidingPlayer != battle.decidingPlayer || node.scenario.winner() != -1) {
				break;
			} else {
				node = node.bestContinuation;
				// System.out.println(node);
				plan.addLast(node);
			}
		}
	}

	private void checkNode(Node node) {
		// debugPrintNode(node);

		if (!node.finished) {
			expandNode(node);
			int count = 0;
			for (Node continuation : node.continuations) {
				nodeStacks[count].addFirst(continuation);
				count++;
			}
		}
	}

	private void debugPrintFinalScore(Node node) {
		System.out.println(node.debuggingName + " P:" + node.scenario.decidingPlayer + "   ini:" + node.initialScore
				+ "   fin:" + node.finalScore);
		if (!node.continuations.isEmpty()) {
			for (Node continuation : node.continuations) {
				if (continuation.finalScore != -1) {
					debugPrintFinalScore(continuation);
				}
			}
		}
	}

	private void debugPrintNode(Node node) {
		String choice = "";
		if (node.chosenCard != -1) {
			BCard card = node.scenario.cards[node.chosenCard];
			choice += debuggingCardString(card);
			if (node.chosenEffect != -1) {
				choice += "{" + node.chosenEffect + "}";
			}
		}
		System.out.println(node.debuggingName + "        St:" + node.scenario.state + "     Tr:" + node.passedTurns
				+ "      Ch: " + choice);
		debuggingPrintZone(node.scenario.zones[1][Battle.handZone], node.scenario);
		debuggingPrintZone(node.scenario.zones[1][Battle.fieldZone], node.scenario);
		debuggingPrintZone(node.scenario.zones[0][Battle.fieldZone], node.scenario);
		debuggingPrintZone(node.scenario.zones[0][Battle.handZone], node.scenario);
		System.out.println();
	}

	private void debuggingPrintZone(LinkedList<Integer> zone, Battle scenario) {
		String str = "";
		for (int c : zone) {
			str += debuggingCardString(scenario.cards[c]) + " ";
		}
		System.out.println("[" + str + "]");
	}

	private String debuggingCardString(BCard card) {
		if (card.turn) {
			return card.model.name + "(Y)";
		} else {
			return card.model.name + "(N)";
		}
	}

	private void expandNode(Node node) {
		node.continuations = new LinkedList<>();
		if (node.scenario.state == Battle.choosingActiveEffectState) {
			continueWithEffectsFrom(node, Battle.handZone);
			continueWithEffectsFrom(node, Battle.fieldZone);
			continuePassingTurn(node);
		} else {// choosing target card
			int zone = Battle.fieldZone;
			if (node.scenario.choosingTargetStateAction.type == Battle.invAction
					|| node.scenario.choosingTargetStateAction.type == Battle.dscAction) {
				zone = Battle.handZone;
			}
			continueWithTargetFrom(node, 0, zone);
			continueWithTargetFrom(node, 1, zone);
			continueWithNoTarget(node);
		}
		if (node.scenario.decidingPlayer == 0) {
			Collections.sort(node.continuations, (a, b) -> (a.initialScore < b.initialScore ? -1 : 1));
		} else {
			Collections.sort(node.continuations, (a, b) -> (a.initialScore > b.initialScore ? -1 : 1));
		}
	}

	private void continueWithEffectsFrom(Node node, int zone) {
		for (int c : node.scenario.zones[node.scenario.turn][zone]) {
			BCard card = node.scenario.cards[c];
			int effectNumber = 0;
			for (Effect effect : card.model.effects) {
				if (card.turn && effect.zone == zone && effect.sequence.isEmpty()) {
					Battle newScenario = node.scenario.copy();
					newScenario.executeActiveEffect(card.battleId, effectNumber);
					while (newScenario.state == Battle.executingActionState) {
						newScenario.executeAction();
						if (newScenario.winner() != -1) {
							break;
						}
					}
					Node newNode = getNewNode(newScenario, card.battleId, effectNumber, node.passedTurns,
							node.debuggingName + node.continuations.size());
					node.continuations.addLast(newNode);
				}
				effectNumber++;
			}
		}
	}

	private void continuePassingTurn(Node node) {
		Battle newScenario = node.scenario.copy();
		newScenario.passTurnWithoutDrawing();
		Node newNode = getNewNode(newScenario, -1, -1, node.passedTurns + 1,
				node.debuggingName + node.continuations.size());
		node.continuations.addLast(newNode);
	}

	private void continueWithTargetFrom(Node node, int player, int zone) {
		for (int c : node.scenario.zones[player][zone]) {
			Battle newScenario = node.scenario.copy();
			newScenario.setChosenTarget(c);
			while (newScenario.state == Battle.executingActionState) {
				newScenario.executeAction();
				if (newScenario.winner() != -1) {
					break;
				}
			}
			Node newNode = getNewNode(newScenario, c, -1, node.passedTurns,
					node.debuggingName + node.continuations.size());
			node.continuations.addLast(newNode);
		}
	}

	private void continueWithNoTarget(Node node) {
		Battle newScenario = node.scenario.copy();
		newScenario.setChosenTarget(-1);
		while (newScenario.state == Battle.executingActionState) {
			newScenario.executeAction();
			if (newScenario.winner() != -1) {
				break;
			}
		}
		Node newNode = getNewNode(newScenario, -1, -1, node.passedTurns,
				node.debuggingName + node.continuations.size());
		node.continuations.addLast(newNode);
	}
	
	public Node getNewNode(Battle scenario, int chosenCard, int chosenEffect, int passedTurns, String debuggingName) {
		Node node=nodes[checkedNodes];
		checkedNodes++;
		node.continuations.clear();
		node.debuggingName=debuggingName;
		node.scenario=scenario;
		node.chosenCard=chosenCard;
		node.chosenEffect=chosenEffect;
		node.passedTurns=passedTurns;
		if(scenario.winner()!=-1 || passedTurns==2) {
			node.finished=true;
		}
		else {
			node.finished=false;
		}
		node.initialScore=BattleScoreCalculator.calculateInitialScore(scenario);
		node.finalScore=-1;
		return node;
	}


}