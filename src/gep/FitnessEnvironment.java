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
	 * Evaluates the fitness for every individual of the given population and
	 * returns the index of the best individual.
	 * 
	 * @param population
	 *            The population to be assessed by this method.
	 * @return The index in the population array with the best fitness value.
	 */
	public int evaluateFitness(Individual<T>[] population) {
		double maxFitness = Double.MIN_VALUE;
		int maxFitnessIdx = 0;
		for (int i = 0; i < population.length; i++) {
			// TODO parallelize (by adding abstract clone method to this class)
			final Individual<T> individual = population[i];
			individual.setFitness(evaluateFitness(individual));
			if (maxFitness < individual.getFitness()) {
				maxFitness = individual.getFitness();
				maxFitnessIdx = i;
			}
		}
		return maxFitnessIdx;
	}

	/**
	 * Evaluates the fitness of the given individual.
	 * 
	 * @param individual
	 *            The individual to be evaluated.
	 * @return The fitness value of the individual
	 */
	abstract protected double evaluateFitness(Individual<T> individual);

}
