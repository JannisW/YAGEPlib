/*
 * Copyright 2017 Johannes Wortmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package examples.behavior.fitness;

import java.util.ArrayList;

import examples.behavior.world.Field;
import examples.behavior.world.Orientation;
import examples.behavior.world.WorldMap;
import gep.FitnessEnvironment;
import gep.model.ExpressionTreeNode;
import gep.model.Individual;

public class EvaluationEnvironment extends FitnessEnvironment<Boolean> {

	final static int MAX_NUMBER_OF_SIMULATION_TICKS = 400;
	// final static double START_FITNESS = 100.0;

	public static boolean PRINT_STEPS = false;

	private int posAgentX;
	private int posAgentY;
	private Orientation agentOrientation;

	// TODO maybe change 2d array to 1d array
	private Field[][] grid;

	private int foodConsumed = 0;
	private int movedDistance = 0;

	/**
	 * controls the current fitness score for the current fitness case (map)
	 */
	protected BehaviorFitnessFunction fitnessFunction;

	/**
	 * Keeps the accumulated fitness score over all maps.
	 */
	private double totalFitnessScore;

	// the different maps (fitness cases) for generalization
	private final WorldMap[] maps;

	private WorldMap currentMap; // TODO resolve redundancy with grid

	public EvaluationEnvironment(ArrayList<WorldMap> maps, BehaviorFitnessFunction fitnessFunctionPerMap) {
		this.maps = maps.toArray(new WorldMap[maps.size()]);
		this.fitnessFunction = fitnessFunctionPerMap;
		fitnessFunction.resetFitnessScorePerMap();
		resetTotalFitnessScore();
	}

	/**
	 * Moves the agent to the given position if possible.
	 * 
	 * As the field is surrounded by walls and step size is always one it is
	 * sufficient to check if the target field is not a wall. If these
	 * preconditions are not guaranteed by the caller. This function may not
	 * work as intended.
	 * 
	 * @param x
	 *            The new x position of the agent
	 * @param y
	 *            The new y position of the agent
	 * @return true, if the move was successful, false otherwise.
	 */
	public boolean moveTo(int x, int y) {
		// System.out.println("CALLED");
		if (grid[x][y].isWall()) {
			// give penalty for actually try to move on a field with a wall.
			fitnessFunction.applyWalkIntoWallPenalty();
			return false;
		}
		// every valid step gives a plus point (makes the agent move)
		fitnessFunction.applyValidMovementBonus();

		posAgentX = x;
		posAgentY = y;
		if (grid[x][y].isFood()) {
			grid[x][y].removeFood();
			foodConsumed++;
			fitnessFunction.applyFoodConsumedBonus();
		}

		movedDistance++;

		if (PRINT_STEPS) {
			currentMap.printCurrentMapState(posAgentX, posAgentY, agentOrientation);
		}
		return true;
	}

	/**
	 * Sets the marker flag of the field where the agent stays to the provided
	 * boolean value.
	 * 
	 * @param marked
	 *            true if marker should be set, false if it should be removed.
	 */
	public void setMarkerOnCurrentPosition(boolean marked) {
		currentMap.setMarkerOnCurrentPosition(posAgentX, posAgentY, marked);
	}

	/**
	 * Returns the x position of the agent
	 * 
	 * @return the x position of the agent
	 */
	public int getPosAgentX() {
		return posAgentX;
	}

	/**
	 * Returns the y position of the agent
	 * 
	 * @return the y position of the agent
	 */
	public int getPosAgentY() {
		return posAgentY;
	}

	public Orientation getAgentOrientation() {
		return agentOrientation;
	}

	public void setAgentOrientation(Orientation agentOrientation) {
		if (this.agentOrientation != agentOrientation && PRINT_STEPS) {
			currentMap.printCurrentMapState(posAgentX, posAgentY, agentOrientation);
		}
		this.agentOrientation = agentOrientation;
	}

	/**
	 * Returns the field in front of the agent. Assumes that the field is
	 * surrounded by walls and that the agent is never located there.
	 * 
	 * @return the field in front of the agent
	 */
	public Field getFieldInFront() {
		switch (getAgentOrientation()) {
		case NORTH:
			return grid[posAgentX][posAgentY + 1];
		case EAST:
			return grid[posAgentX + 1][posAgentY];
		case SOUTH:
			return grid[posAgentX][posAgentY - 1];
		case WEST:
			return grid[posAgentX - 1][posAgentY];
		}

		return null; // never be reached
	}

	@Override
	protected double evaluateFitness(Individual<Boolean> individual) {

		resetTotalFitnessScore();

		for (WorldMap map : maps) {

			fitnessFunction.resetFitnessScorePerMap();
			foodConsumed = 0;
			movedDistance = 0;

			currentMap = map;

			grid = map.initMap();
			posAgentX = map.getStartPositionX();
			posAgentY = map.getStartPositionY();
			setAgentOrientation(map.getStartOrientation());

			// single chromosome individuals (only one program)
			ExpressionTreeNode<Boolean> currentProgram = individual.getExpressionTrees().get(0);

			int numberOfTicks = 0;
			while (numberOfTicks < MAX_NUMBER_OF_SIMULATION_TICKS) { // TODO and
																		// food
																		// is
																		// left

				boolean executionResult = currentProgram.execute();

				if (!executionResult) {
					// return currentFitnessScore-1; // TODO makes sense?
				}

				numberOfTicks++; // TODO maybe also couple to number of steps
			}

			if (movedDistance == 0) {
				// if no movement during the whole simulation high penalty to
				// eliminate this behavior
				fitnessFunction.applyNoMovementPenalty();
			}

			totalFitnessScore += fitnessFunction.getCurrentScore();
		}

		// System.out.println("SCORE: " + totalFitnessScore + " (food consumed:
		// " + foodConsumed + ")");

		return this.totalFitnessScore;

		// TODO check if that makes sense... especially wrt. parallelization
		// etc.
	}

	protected void resetTotalFitnessScore() {
		this.totalFitnessScore = 0;
		this.foodConsumed = 0;
		this.movedDistance = 0;
	}

}
