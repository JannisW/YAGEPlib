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
	public <T> void apply(Individual<T>[] individuals, int fromIdx) {
		if (individuals.length - fromIdx < 2) {
			// this operator requires at least two modifiable individuals
			throw new IllegalArgumentException(
					"The GeneRecombination operator requires at least two modifiable individuals in the population.");
		}

		for (int i = fromIdx; i < individuals.length; i++) {
			Individual<T> individual = individuals[i];
			for (int cIdx = 0; cIdx < individual.chromosomes.length; cIdx++) {
				if (random.decideBinaryDecision(super.applicationRate)) {
					// chromosome was selected => find another individual to
					// recombine with
					int otherIndiIdx = super.random.getInt(fromIdx, individuals.length);
					if (otherIndiIdx == i) {
						otherIndiIdx = (otherIndiIdx > fromIdx) ? otherIndiIdx - 1 : otherIndiIdx + 1;
					}

					Chromosome<T> parent1Chromosome = individual.chromosomes[cIdx];
					Chromosome<T> parent2Chromosome = individuals[otherIndiIdx].chromosomes[cIdx];

					int pickedGeneId = random.getInt(0, parent1Chromosome.genes.length);

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
		// can't be called => method stub!
		throw new UnsupportedOperationException("This operator works on population level");
	}

	@Override
	public String getName() {
		return "Gene recombination";
	}

}
