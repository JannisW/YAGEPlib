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

	final protected static int MAX_NUMBER_OF_SIMULATION_TICKS = 400;
	// final static double START_FITNESS = 100.0;

	public static boolean PRINT_STEPS = false;

	private int posAgentX;
	private int posAgentY;
	private Orientation agentOrientation;
	
	// a one bit memory for the agent
	private boolean agentMemory;

	// TODO maybe change 2d array to 1d array
	//private Field[][] grid;

	private int foodConsumed = 0;
	private int movedDistance = 0;

	/**
	 * controls the current fitness score for the current fitness case (map)
	 */
	transient protected BehaviorFitnessFunction fitnessFunction;

	/**
	 * Keeps the accumulated fitness score over all maps.
	 */
	private double totalFitnessScore;

	// the different maps (fitness cases) for generalization
	protected final WorldMap[] maps;

	protected WorldMap currentMap;

	public EvaluationEnvironment(WorldMap[] maps, BehaviorFitnessFunction fitnessFunctionPerMap) {
		this.maps = maps;
		this.fitnessFunction = fitnessFunctionPerMap;
		resetTotalFitnessScore();
	}

	public EvaluationEnvironment(ArrayList<WorldMap> maps, BehaviorFitnessFunction fitnessFunctionPerMap) {
		this(maps.toArray(new WorldMap[maps.size()]), fitnessFunctionPerMap);
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
		final Field goalField = currentMap.getField(x, y);
		if (goalField.isWall()) {
			// give penalty for actually try to move on a field with a wall.
			fitnessFunction.applyWalkIntoWallPenalty();
			return false;
		}
		// every valid step gives a plus point (makes the agent move)
		fitnessFunction.applyValidMovementBonus();

		posAgentX = x;
		posAgentY = y;
		if (goalField.isFood()) {
			goalField.removeFood();
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

	protected void setAgentPos(int x, int y) {
		this.posAgentX = x;
		this.posAgentY = y;
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
			return currentMap.getField(posAgentX, posAgentY + 1);
		case EAST:
			return currentMap.getField(posAgentX + 1, posAgentY);
		case SOUTH:
			return currentMap.getField(posAgentX, posAgentY - 1);
		case WEST:
			return currentMap.getField(posAgentX - 1, posAgentY);
		}

		return null; // never be reached
	}

	@Override
	protected double evaluateFitness(Individual<Boolean> individual) {

		resetTotalFitnessScore();

		for (WorldMap map : maps) {

			// TODO move to init function
			fitnessFunction.resetFitnessScorePerMap();
			foodConsumed = 0;
			movedDistance = 0;

			currentMap = map;
			currentMap.initMap();
			
			posAgentX = map.getStartPositionX();
			posAgentY = map.getStartPositionY();
			setAgentOrientation(map.getStartOrientation());
			agentMemory = false;

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
		this.agentMemory = false;
	}

	protected int getFoodConsumed() {
		return foodConsumed;
	}

	public boolean getAgentMemory() {
		return agentMemory;
	}

	public void setAgentMemory(boolean agentMemory) {
		this.agentMemory = agentMemory;
	}

}
