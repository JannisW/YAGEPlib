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

public class AlternativeFitnessFunction extends BehaviorFitnessFunction {
	
	protected final static double START_FITNESS = 100.0;
	
	@Override
	public void applyNoMovementPenalty() {
		currentFitnessScore = 0.0;
	}
	
	@Override
	public void resetFitnessScorePerMap() {
		this.currentFitnessScore = START_FITNESS;
	}

	@Override
	public void applyWalkIntoWallPenalty() {
		currentFitnessScore = Math.max(0, currentFitnessScore - 2);
	}

	@Override
	public void applyFoodConsumedBonus() {
		currentFitnessScore += 22.0;
	}

	@Override
	public void applyValidMovementBonus() {
		currentFitnessScore += 1.0;
	}

}
