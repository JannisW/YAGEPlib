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
import java.util.Comparator;
import java.util.PriorityQueue;

import gep.model.Individual;
import gep.random.DefaultRandomEngine;
import gep.random.RandomEngine;

/**
 * <p>
 * This class implements a roulette wheel selection algorithm. Individuals are
 * selected with a probability proportional to their fitness. The best
 * individuals of a population are preserved. If this is not intended check
 * {@link RouletteWheelSelection}. Individuals can be selected multiple times.
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
public class RouletteWheelSelectionWithElitePreservation implements SelectionMethod {

	/**
	 * The used random engine
	 */
	private RandomEngine random;

	/**
	 * The fraction [0.0, 1.0] of individuals that should be preserved as elite.
	 */
	private final double preservationPercentage;

	/**
	 * Creates an instance of the roulette wheel selection algorithm with elite
	 * preservation using the default random engine and a preservation
	 * percentage of 10% (The best 10% of the population are guaranteed to be
	 * selected).
	 */
	public RouletteWheelSelectionWithElitePreservation() {
		this(new DefaultRandomEngine(), 0.1);
	}

	/**
	 * Creates an instance of the roulette wheel selection algorithm with elite
	 * preservation using the default random engine and the given preservation
	 * percentage in the interval [0.0, 1.0]. A preservation percentage of 0.1
	 * means a that the best 10% of the population are guaranteed to be
	 * selected.
	 * 
	 * @param preservationPercentage
	 *            The preservation percentage in the interval [0.0, 1.0]
	 */
	public RouletteWheelSelectionWithElitePreservation(double preservationPercentage) {
		this(new DefaultRandomEngine(), preservationPercentage);
	}

	/**
	 * Creates an instance of the roulette wheel selection algorithm with elite
	 * preservation using the given random engine and the given preservation
	 * percentage in the interval [0.0, 1.0]. A preservation percentage of 0.1
	 * means a that the best 10% of the population are guaranteed to be
	 * selected.
	 * 
	 * @param random
	 *            The random engine to be used by this selection method
	 * 
	 * @param preservationPercentage
	 *            The preservation percentage in the interval [0.0, 1.0]
	 */
	public RouletteWheelSelectionWithElitePreservation(RandomEngine random, double preservationPercentage) {
		this.random = random;
		this.preservationPercentage = preservationPercentage;
	}

	/**
	 * <p>
	 * Randomly selects individuals from a the current population to create a
	 * new one based on their fitness.
	 * </p>
	 * 
	 * <p>
	 * Individuals are selected with a probability proportional to their
	 * fitness. The best individuals of a population are preserved (determined
	 * by the preservation percentage provided at construction). Individuals can
	 * be selected multiple times.
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
	 * fitness. The best individuals of a population are preserved (determined
	 * by the preservation percentage provided at construction). Individuals can
	 * be selected multiple times.
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

		final Individual<T>[] oldPopulation = Arrays.copyOf(population, population.length);

		final int numElitesPreserved = Math.max(1, (int) (population.length * preservationPercentage));
		PriorityQueue<Integer> eliteIdx = new PriorityQueue<>(numElitesPreserved,
				new IndexedIndividualComparator<>(oldPopulation));

		double comulativeFitness[] = new double[population.length];
		comulativeFitness[0] = population[0].getFitness();
		eliteIdx.add(0);
		// TODO might be improved by filling the complete pq here (so one if in
		// the loop can be deleted)

		for (int i = 1; i < comulativeFitness.length; i++) {
			comulativeFitness[i] = comulativeFitness[i - 1] + population[i].getFitness();
			if (eliteIdx.size() == numElitesPreserved) {
				// priority queue is full
				if (oldPopulation[eliteIdx.peek()].compareTo(oldPopulation[i]) < 0) {
					// the worst individual of the elite is worse than the
					// current individual
					// => replace it by the current one
					eliteIdx.poll();
					eliteIdx.add(i);
				}
			} else {
				eliteIdx.add(i);
			}
		}

		// preserve elite
		for (int i = numElitesPreserved - 1; i >= 0; i--) {
			final int idx = eliteIdx.poll();
			population[i] = oldPopulation[idx];
			isPartOfNewPopulation[idx] = true;
		}

		// fill remaining population
		for (int i = numElitesPreserved; i < population.length; i++) {
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

		return numElitesPreserved;
	}

	/**
	 * Used to compare individuals based on their indicies in the oldPopulation
	 * array and their compareTo methods.
	 */
	private class IndexedIndividualComparator<T> implements Comparator<Integer> {

		private final Individual<T>[] oldPopulation;

		public IndexedIndividualComparator(Individual<T>[] oldPopulation) {
			this.oldPopulation = oldPopulation;
		}

		@Override
		public int compare(Integer idx1, Integer idx2) {
			return oldPopulation[idx1].compareTo(oldPopulation[idx2]);
		}

	}

}
