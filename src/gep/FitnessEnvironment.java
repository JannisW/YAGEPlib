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
package gep;

import gep.model.Individual;

public abstract class FitnessEnvironment<T> {

	/**
	 * Evaluates the fitness for every individual of the given population.
	 * 
	 * @param population
	 *            The population to be assessed by this method.
	 * @return The fitness value of the best individual
	 */
	public double evaluateFitness(Individual<T>[] population) {
		double maxFitness = Double.MIN_VALUE;
		for (Individual<T> individual : population) { // TODO parallelize
			individual.setFitness(evaluateFitness(individual));
			maxFitness = Math.max(maxFitness, individual.getFitness());
		}
		return maxFitness;
	}

	abstract protected double evaluateFitness(Individual<T> individual);
	
	// maybe not abstract individual fitness evaluation (but add an init)

}
