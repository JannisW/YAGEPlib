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
package gep.selection;

import java.util.Arrays;

import gep.model.Individual;
import gep.random.DefaultRandomEngine;
import gep.random.RandomEngine;

/**
 * <p>
 * This class implements a roulette wheel selection algorithm. Individuals are
 * selected with a probability proportional to their fitness. No elite
 * preservation is used. If the best individuals of a population should be
 * preserved check {@link RouletteWheelSelectionWithElitePreservation}.
 * Individuals can be selected multiple times.
 * </p>
 * 
 * <p>
 * In some instances, particularly with small population sizes, the randomness
 * of selection may result in excessively high occurrences of particular
 * candidates. If this is a problem, {@link StochasticUniversalSampling}
 * provides an alternative fitness-proportionate strategy for selection.
 * </p>
 * 
 * Based on:
 * https://github.com/dwdyer/watchmaker/blob/master/framework/src/java/main/org/uncommons/watchmaker/framework/selection/RouletteWheelSelection.java
 * 
 * @author Johannes Wortmann, (Daniel Dyer)
 *
 */
public class RouletteWheelSelection implements SelectionMethod {

	/**
	 * The used random engine
	 */
	private RandomEngine random;

	/**
	 * Creates an instance of the roulette wheel selection algorithm using the
	 * default random engine.
	 */
	public RouletteWheelSelection() {
		random = new DefaultRandomEngine();
	}

	/**
	 * Creates an instance of the roulette wheel selection algorithm using the
	 * provided random engine.
	 * 
	 * @param random
	 *            The random engine to be used by this selection method.
	 */
	public RouletteWheelSelection(RandomEngine random) {
		this.random = random;
	}

	/**
	 * <p>
	 * Randomly selects individuals from a the current population to create a
	 * new one based on their fitness.
	 * </p>
	 * 
	 * <p>
	 * Individuals are selected with a probability proportional to their
	 * fitness. No elite preservation is used. If the best individuals of a
	 * population should be preserved check
	 * {@link RouletteWheelSelectionWithElitePreservation}. Individuals can be
	 * selected multiple times.
	 * </p>
	 * 
	 * <p>
	 * The returned index defines from which index in the population array later
	 * reproduction is allowed to occur. If the returned value is for example 2,
	 * it means that all following genetic operators should not change the
	 * elements at index 0 and 1.
	 * </p>
	 * 
	 * @param population
	 *            The population which should be changed.
	 * 
	 * @return The index (inclusive) from which later reproduction is allowed to
	 *         occur.
	 */
	@Override
	public <T> int select(Individual<T>[] population) {
		return select(population, this.random);
	}

	/**
	 * <p>
	 * Randomly selects individuals from a the current population to create a
	 * new one based on their fitness.
	 * </p>
	 * 
	 * <p>
	 * Individuals are selected with a probability proportional to their
	 * fitness. No elite preservation is used. If the best individuals of a
	 * population should be preserved check
	 * {@link RouletteWheelSelectionWithElitePreservation}. Individuals can be
	 * selected multiple times.
	 * </p>
	 * 
	 * <p>
	 * The returned index defines from which index in the population array later
	 * reproduction is allowed to occur. If the returned value is for example 2,
	 * it means that all following genetic operators should not change the
	 * elements at index 0 and 1.
	 * </p>
	 * 
	 * @param population
	 *            The population which should be changed.
	 * @param random
	 *            The RandomEngine to be used
	 * 
	 * @return The index (inclusive) from which later reproduction is allowed to
	 *         occur.
	 */
	@Override
	public <T> int select(Individual<T>[] population, RandomEngine random) {

		// To avoid to copy too many objects keep the original objects before
		// copying
		// This exploits the fact that boolean arrays are initialized to false.
		boolean[] isPartOfNewPopulation = new boolean[population.length];

		Individual<T>[] oldPopulation = Arrays.copyOf(population, population.length);

		double comulativeFitness[] = new double[population.length];
		comulativeFitness[0] = population[0].getFitness();

		for (int i = 1; i < comulativeFitness.length; i++) {
			comulativeFitness[i] = comulativeFitness[i - 1] + population[i].getFitness();
		}

		for (int i = 0; i < population.length; i++) {
			double rndFitness = random.getDouble() * comulativeFitness[comulativeFitness.length - 1];
			int idx = Arrays.binarySearch(comulativeFitness, rndFitness);
			// translate negative results of binary search
			idx = Math.max(idx, -(idx + 1));

			if (isPartOfNewPopulation[idx]) {
				// just copy if could not reuse old one
				population[i] = new Individual<T>(oldPopulation[idx]);
			} else {
				population[i] = oldPopulation[idx];
				isPartOfNewPopulation[idx] = true;
			}
		}

		// no elite preservation
		return 0;
	}

}
