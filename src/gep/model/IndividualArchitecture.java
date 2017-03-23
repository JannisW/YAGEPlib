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

import gep.random.RandomEngine;

public class IndividualArchitecture<T> {

	private ArrayList<ChromosomalArchitecture<T>> chromosomeArchitecture;

	public IndividualArchitecture() {
		chromosomeArchitecture = new ArrayList<ChromosomalArchitecture<T>>();
	}

	public IndividualArchitecture(ArrayList<ChromosomalArchitecture<T>> chromosomeArchitecture) {
		this.chromosomeArchitecture = chromosomeArchitecture;
	}

	public void addChromosome(ChromosomalArchitecture<T> c) {
		this.chromosomeArchitecture.add(c);
	}

	public Individual<T>[] createRandomPopulation(int numIndividuals, RandomEngine r) {
		if (chromosomeArchitecture.isEmpty()) {
			throw new IllegalStateException("Cannot create individual without a chromosome");
		}

		Individual<T>[] result = new Individual[numIndividuals];
		for (int i = 0; i < result.length; i++) {
			result[i] = createRandomIndividual(r);
		}

		return result;
	}

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
