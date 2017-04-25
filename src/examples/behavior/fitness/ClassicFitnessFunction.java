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
 * This class represents the classic fitness function in which the fitness is
 * just determined by the amount of food an individual is able to consume (more
 * is better)
 * 
 * @author Johannes Wortmann
 *
 */
public class ClassicFitnessFunction extends BehaviorFitnessFunction {

	@Override
	public void applyNoMovementPenalty() {
		// do nothing
	}

	@Override
	public void resetFitnessScorePerMap() {
		this.currentFitnessScore = 0.0;
	}

	@Override
	public void applyWalkIntoWallPenalty() {
		// do nothing
	}

	@Override
	public void applyFoodConsumedBonus() {
		this.currentFitnessScore += 1.0;

	}

	@Override
	public void applyValidMovementBonus() {
		// do nothing
	}

}
