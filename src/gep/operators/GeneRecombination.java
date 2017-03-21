package gep.operators;

import gep.model.Chromosome;
import gep.model.Gene;
import gep.model.Individual;
import gep.random.RandomEngine;

public class GeneRecombination extends GeneticOperator {

	public GeneRecombination(double applicationRate) {
		super(applicationRate);
	}

	public GeneRecombination(double applicationRate, RandomEngine re) {
		super(applicationRate, re);
	}

	@Override
	public <T> void apply(Individual<T>[] individuals) {
		if (individuals.length < 2) {
			// this operator requires at least
			throw new IllegalArgumentException(
					"The GeneRecombination operator requires at least two individuals in the population.");
		}

		for (int i = 0; i < individuals.length; i++) {
			Individual<T> individual = individuals[i];
			for (int cIdx = 0; cIdx < individual.chromosomes.length; cIdx++) {
				if (random.decideBinaryDecision(super.applicationRate)) {
					// chromosome was selected => find another individual to
					// recombine with
					int otherIndiIdx = super.random.getInt(0, individuals.length);
					if (otherIndiIdx == i) {
						otherIndiIdx = (otherIndiIdx > 0) ? otherIndiIdx - 1 : otherIndiIdx + 1;
					}
					
					Chromosome<T> parent1Chromosome = individual.chromosomes[cIdx];
					int pickedGeneId = random.getInt(0, parent1Chromosome.genes.length);
					Chromosome<T> parent2Chromosome = individuals[otherIndiIdx].chromosomes[cIdx];

					// swap the gene
					Gene<T> tmp = new Gene<T>(parent1Chromosome.genes[pickedGeneId]);
					parent1Chromosome.genes[pickedGeneId] = parent2Chromosome.genes[pickedGeneId];
					parent2Chromosome.genes[pickedGeneId] = tmp;
				}

			}
		}
	}

	@Override
	protected <T> void apply(Gene<T> gene) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("This operator works on chromosome level");
	}

	@Override
	public String getName() {
		return "Gene recombination";
	}

}
