package zAI;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import battle.BCard;
import battle.Battle;
import battle.ComputerAI;
import battle.Effect;
import bruteForceAI.BattleScoreCalculator;

public class ZAI extends ComputerAI {

	public static final int maxTakenOptions = 8;
	public static final int maxConsideredOptions = 100;

	public static class ZAIFactory extends ComputerAIFactory {
		public ComputerAI create(int player, int rndSeed) {
			return new ZAI(player, rndSeed);
		}
	}

	private LinkedList<ZNode> expandedOptions;

	public LinkedList<ZNode> plan;

	public ZAI(int player, int rndSeed) {
		super(player);
		expandedOptions = new LinkedList<>();
		plan = new LinkedList<>();
	}

	@Override
	public void play(Battle battle) {
		if (plan.isEmpty()) {
			createPlan(battle);
		}
		ZNode node = plan.removeFirst();
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
		ZNode ZNode = plan.removeFirst();
		//plan.clear();

		// Here is where the AI actually makes its move, so it is not a simulation
		battle.setChosenTarget(ZNode.chosenCard, Battle.noneAISimulating);
	}

	@Override
	public void notifyUnexpectedEffectTriggered() {
		plan.clear();
	}

	private void createPlan(Battle battle) {
		ZNode firstZNode = getNewZNode(battle, -1, -1, 0, null, "");
		List<ZNode> bestSelfTurnFinishingOptions = findBestTurnFinishingOptions(firstZNode);

		List<ZNode> finalOptions = new LinkedList<>();
		for (ZNode bestSelfTurnFinishingOption : bestSelfTurnFinishingOptions) {
			finalOptions.addAll(findBestTurnFinishingOptions(bestSelfTurnFinishingOption));
		}

		order(finalOptions, AIplayer);

		ZNode bestOption = finalOptions.get(0);

		while (bestOption.previous != null) {
			if (bestOption.previous.scenario.turn == AIplayer) {
				plan.addFirst(bestOption);
			}
			bestOption = bestOption.previous;
		}
	}

	private List<ZNode> findBestTurnFinishingOptions(ZNode firstZNode) {
		int player = firstZNode.scenario.turn;

		LinkedList<ZNode> bestOptions = new LinkedList<>();
		LinkedList<ZNode> consideredOptions = new LinkedList<>();
		bestOptions.add(firstZNode);

		while (!bestOptions.isEmpty()) {
			while (!bestOptions.isEmpty()) {
				ZNode ZNode = bestOptions.removeFirst();
				if (ZNode.scenario.turn == player && !ZNode.finished) {
					expandedOptions.clear();
					expandZNode(ZNode);
					consideredOptions.addAll(expandedOptions);
				} else {
					consideredOptions.add(ZNode);
				}
			}

			order(consideredOptions, player);

			int turnFinishingOptions = 0;
			for (int i = 0; i < maxTakenOptions && !consideredOptions.isEmpty(); i++) {
				ZNode consideredOption = consideredOptions.removeFirst();
				bestOptions.add(consideredOption);
				if (consideredOption.scenario.turn != player || consideredOption.finished) {
					turnFinishingOptions++;
				}
			}
			
			consideredOptions.clear();
			
			if (turnFinishingOptions == bestOptions.size()) {
				break;
			}
		}

		return bestOptions;
	}

	private void order(List<ZNode> nodes, int player) {
		Collections.sort(nodes, (a, b) -> {
			if (b.initialScore == a.initialScore) {
				return 0;
			} else {
				return ((b.initialScore - a.initialScore) * (player * 2 - 1) < 0) ? -1 : 1;
			}
		});
	}

	private void expandZNode(ZNode ZNode) {
		if (ZNode.scenario.state == Battle.choosingActiveEffectState) {
			continueWithEffectsFrom(ZNode, Battle.handZone);
			continueWithEffectsFrom(ZNode, Battle.fieldZone);
			continuePassingTurn(ZNode);
		} else {// choosing target card
			switch (ZNode.scenario.choosingTargetStateAction.type) {
			case Battle.invAction:
				continueWithTargetFrom(ZNode, AIplayer, Battle.handZone);
				break;
			case Battle.atkAction:
				continueWithTargetFrom(ZNode, 1 - AIplayer, Battle.fieldZone);
				break;
			case Battle.dscAction:
				continueWithTargetFrom(ZNode, 1 - AIplayer, Battle.handZone);
				break;
			case Battle.wdrAction:
				continueWithTargetFrom(ZNode, AIplayer, Battle.fieldZone);
				continueWithTargetFrom(ZNode, 1 - AIplayer, Battle.fieldZone);
				break;
			}
			continueWithNoTarget(ZNode);
		}
		if (expandedOptions.size() > maxConsideredOptions) {
			throw new RuntimeException("The amount of considered options is greater than the array size. Increase it.");
		}
	}

	private void continueWithEffectsFrom(ZNode node, int zone) {
		for (int c : node.scenario.zones[node.scenario.turn][zone]) {
			BCard card = node.scenario.cards[c];
			int effectNumber = 0;
			for (Effect effect : card.model.effects) {
				if (card.turn && effect.zone == zone && effect.sequence.isEmpty()) {
					Battle newScenario = node.scenario.copy();
					newScenario.executeActiveEffect(card.battleId, effectNumber);
					while (newScenario.state == Battle.executingActionState) {
						newScenario.executeAction(AIplayer);
						if (newScenario.winner() != -1) {
							break;
						}
					}
					ZNode newNode = getNewZNode(newScenario, card.battleId, effectNumber, node.passedTurns, node,
							node.debuggingName + expandedOptions.size());
					expandedOptions.addLast(newNode);
				}
				effectNumber++;
			}
		}
	}

	private void continuePassingTurn(ZNode node) {
		Battle newScenario = node.scenario.copy();
		newScenario.passTurn();
		ZNode newNode = getNewZNode(newScenario, -1, -1, node.passedTurns + 1, node,
				node.debuggingName + expandedOptions.size());
		expandedOptions.addLast(newNode);
	}

	private void continueWithTargetFrom(ZNode node, int targetPlayer, int targetZone) {
		for (int c : node.scenario.zones[targetPlayer][targetZone]) {
			Battle newScenario = node.scenario.copy();
			newScenario.setChosenTarget(c, AIplayer);
			while (newScenario.state == Battle.executingActionState) {
				newScenario.executeAction(AIplayer);
				if (newScenario.winner() != -1) {
					break;
				}
			}
			ZNode newZNode = getNewZNode(newScenario, c, -1, node.passedTurns, node,
					node.debuggingName + expandedOptions.size());
			expandedOptions.addLast(newZNode);
		}
	}

	private void continueWithNoTarget(ZNode node) {
		Battle newScenario = node.scenario.copy();
		newScenario.setChosenTarget(-1, AIplayer);
		while (newScenario.state == Battle.executingActionState) {
			newScenario.executeAction(AIplayer);
			if (newScenario.winner() != -1) {
				break;
			}
		}
		ZNode newNode = getNewZNode(newScenario, -1, -1, node.passedTurns, node,
				node.debuggingName + expandedOptions.size());
		expandedOptions.addLast(newNode);
	}

	public ZNode getNewZNode(Battle scenario, int chosenCard, int chosenEffect, int passedTurns, ZNode previous,
			String debuggingName) {
		ZNode node = new ZNode();
		node.previous = previous;
		node.debuggingName = debuggingName;
		node.scenario = scenario;
		node.chosenCard = chosenCard;
		node.chosenEffect = chosenEffect;
		node.passedTurns = passedTurns;
		if (scenario.winner() != -1 || passedTurns == 2) {
			node.finished = true;
		} else {
			node.finished = false;
		}
		node.initialScore = BattleScoreCalculator.calculateInitialScore(scenario, AIplayer);
		return node;
	}

}