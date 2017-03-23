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

public abstract class BehaviorFitnessFunction {
	
	protected double currentFitnessScore;
	
	public BehaviorFitnessFunction() {
		resetFitnessScorePerMap();
	}
	
	abstract public void applyNoMovementPenalty();
	
	abstract public void applyWalkIntoWallPenalty();
	
	abstract public void applyFoodConsumedBonus();
	
	abstract public void applyValidMovementBonus();
	
	abstract public void resetFitnessScorePerMap();
	
	public double getCurrentScore() {
		return this.currentFitnessScore;
	}

}
