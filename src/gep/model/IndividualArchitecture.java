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
package gep.model;

import java.util.ArrayList;

import gep.random.DefaultRandomEngine;
import gep.random.RandomEngine;

/**
 * This class is a factory class to create random individuals and populations.
 * 
 * @author Johannes Wortmann
 *
 * @param <T>
 *            The return type of the elements in the expression tree encoded by
 *            an individual
 */
public class IndividualArchitecture<T> {

	/**
	 * The ordered set of chromosomes of an individual produced by this factory.
	 */
	private ArrayList<ChromosomalArchitecture<T>> chromosomeArchitecture;

	/**
	 * Creates a new factory for Individuals. Initially the encoded Individuals
	 * don't have any chromosomes.
	 */
	public IndividualArchitecture() {
		chromosomeArchitecture = new ArrayList<ChromosomalArchitecture<T>>();
	}

	/**
	 * Creates a new factory for Individuals. Initially the encoded Individuals
	 * have the given chromosomes. The given list will not be copied and
	 * therefore the order of the chromosomes will be preserved.
	 * 
	 * @param chromosomeArchitecture
	 *            the architecture of the chromosomes of the encoded Individual.
	 */
	public IndividualArchitecture(ArrayList<ChromosomalArchitecture<T>> chromosomeArchitecture) {
		this.chromosomeArchitecture = chromosomeArchitecture;
	}

	/**
	 * Adds a chromosome to the encoded Individual, that uses the given
	 * chromosomal architecture.
	 * 
	 * @param c
	 *            The architecture to be added.
	 */
	public void addChromosome(ChromosomalArchitecture<T> c) {
		this.chromosomeArchitecture.add(c);
	}

	/**
	 * Creates a random population of the given size and with the current
	 * configuration of chromosomes of this factory.
	 * 
	 * This convenient method uses the default random generator
	 * 
	 * @param numIndividuals
	 *            The size of the population.
	 * 
	 * @return An array of individuals representing a population.
	 */
	public Individual<T>[] createRandomPopulation(int numIndividuals) {
		return createRandomPopulation(numIndividuals, new DefaultRandomEngine());
	}

	/**
	 * Creates a random population of the given size and with the current
	 * configuration of chromosomes of this factory.
	 * 
	 * @param numIndividuals
	 *            The size of the population.
	 * @param r
	 *            The RandomEngine to be used
	 * @return An array of individuals representing a population.
	 */
	public Individual<T>[] createRandomPopulation(int numIndividuals, RandomEngine r) {
		if (chromosomeArchitecture.isEmpty()) {
			throw new IllegalStateException("Cannot create individual without a chromosome");
		}

		@SuppressWarnings("unchecked")
		Individual<T>[] result = (Individual<T>[]) new Individual<?>[numIndividuals];
		for (int i = 0; i < result.length; i++) {
			result[i] = createRandomIndividual(r);
		}

		return result;
	}

	/**
	 * Internal method to create a single random Individual based on the current
	 * chromosome configuration of this factory.
	 * 
	 * @param r
	 *            The used random engine
	 * @return the randomly created Individual
	 */
	private Individual<T> createRandomIndividual(RandomEngine r) {

		Individual<T> individual = new Individual<T>(chromosomeArchitecture.size());

		int idx = 0;
		for (ChromosomalArchitecture<T> chromoArchitecture : chromosomeArchitecture) {
			individual.chromosomes[idx] = chromoArchitecture.create(r);
			idx++;
		}

		return individual;
	}

	/**
	 * Creates an IndividualArchitecture, which represents an individual with
	 * the given single chromosome.
	 * 
	 * @param chromosomeArchitecture
	 *            The chromosome of the individual
	 * @return An IndividualArchitecture, which represents an individual with a
	 *         single chromosome.
	 */
	public static <T> IndividualArchitecture<T> createSingleChromosomalArchitecture(
			ChromosomalArchitecture<T> chromosomeArchitecture) {
		ArrayList<ChromosomalArchitecture<T>> cas = new ArrayList<ChromosomalArchitecture<T>>(1);
		cas.add(chromosomeArchitecture);
		return new IndividualArchitecture<T>(cas);
	}

}
