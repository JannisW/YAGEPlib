package gep.model;

import java.util.ArrayList;

import gep.random.RandomEngine;

public class IndividualArchitecture {

	private ArrayList<ChromosomalArchitecture> chromosomeArchitecture;

	public IndividualArchitecture() {
		chromosomeArchitecture = new ArrayList<ChromosomalArchitecture>();
	}

	public IndividualArchitecture(ArrayList<ChromosomalArchitecture> chromosomeArchitecture) {
		this.chromosomeArchitecture = chromosomeArchitecture;
	}

	public void addChromosome(ChromosomalArchitecture c) {
		this.chromosomeArchitecture.add(c);
	}

	public Individual[] createRandomPopulation(int numIndividuals, RandomEngine r) {
		if (chromosomeArchitecture.isEmpty()) {
			throw new IllegalStateException("Cannot create individual without a chromosome");
		}

		Individual[] result = new Individual[numIndividuals];
		for (int i = 0; i < result.length; i++) {
			result[i] = createRandomIndividual(r);
		}

		return result;
	}

	private Individual createRandomIndividual(RandomEngine r) {

		Individual individual = new Individual(chromosomeArchitecture.size());

		int idx = 0;
		for (ChromosomalArchitecture chromoArchitecture : chromosomeArchitecture) {
			individual.chromosomes[idx] = chromoArchitecture.createRandomChromosome(r);
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
	public static IndividualArchitecture createSingleChromosomalArchitecture(
			ChromosomalArchitecture chromosomeArchitecture) {
		ArrayList<ChromosomalArchitecture> cas = new ArrayList<ChromosomalArchitecture>(1);
		cas.add(chromosomeArchitecture);
		return new IndividualArchitecture(cas);
	}

}
