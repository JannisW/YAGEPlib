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

/**
 * Abstract class defining which methods have to be implemented to be considered
 * a valid fitness function for the evolution of behavior example.
 * 
 * @author Johannes Wortmann
 *
 */
public abstract class BehaviorFitnessFunction {

	/**
	 * The current value of the fitness associated with the evaluated
	 * individual.
	 */
	protected double currentFitnessScore;

	/**
	 * Instantiates and resets a new fitness function.
	 */
	protected BehaviorFitnessFunction() {
		resetFitnessScorePerMap();
	}

	/**
	 * Adjusts the current fitness value of the evaluated individual by applying
	 * a penalty in case of no movement of the agent.
	 */
	abstract public void applyNoMovementPenalty();

	/**
	 * Adjusts the current fitness value of the evaluated individual by applying
	 * a penalty in case of an attempt of the agent to walk into a wall.
	 */
	abstract public void applyWalkIntoWallPenalty();

	/**
	 * Adjusts the current fitness value of the evaluated individual by applying
	 * a bonus in case of consumed food.
	 */
	abstract public void applyFoodConsumedBonus();

	/**
	 * Adjusts the current fitness value of the evaluated individual by applying
	 * a bonus in case of a valid movement of the agent.
	 */
	abstract public void applyValidMovementBonus();

	/**
	 * This resets the current value of the fitness score (remember to call)
	 */
	abstract public void resetFitnessScorePerMap();

	/**
	 * Returns the current value of the fitness function (of the current
	 * individual). Note: Remember to reset the current score by calling
	 * {@link BehaviorFitnessFunction#resetFitnessScorePerMap}
	 * 
	 * @return The current value of the fitness function (of the current
	 *         individual)
	 */
	public double getCurrentScore() {
		return this.currentFitnessScore;
	}

}
