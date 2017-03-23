package gep.operators;

import gep.model.Chromosome;
import gep.model.Gene;
import gep.model.GeneElement;
import gep.model.Individual;
import gep.random.RandomEngine;

public class TwoPointRecombination extends GeneticOperator {

	public TwoPointRecombination(double applicationRate) {
		super(applicationRate);
	}

	public TwoPointRecombination(double applicationRate, RandomEngine re) {
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
					
					int pickedGeneId1 = random.getInt(0, parent1Chromosome.genes.length);
					int pickedGeneId2 = random.getInt(0, parent1Chromosome.genes.length);

					if(pickedGeneId2 < pickedGeneId1) {
						int tmp = pickedGeneId1;
						pickedGeneId1 = pickedGeneId2;
						pickedGeneId2 = tmp;
					}
					
					int recombinationPoint1 = random.getInt(0, parent1Chromosome.genes[pickedGeneId1].getSequenceLength());
					int recombinationPoint2 = random.getInt(0, parent1Chromosome.genes[pickedGeneId2].getSequenceLength());

					if(pickedGeneId1 == pickedGeneId2) {
						
						if(recombinationPoint2 < recombinationPoint1) {
							int tmp = recombinationPoint1;
							recombinationPoint1 = recombinationPoint2;
							recombinationPoint2 = tmp;
						}
						
						int len = recombinationPoint2 - recombinationPoint1;
						GeneElement<T>[] tmpGenePart = parent1Chromosome.genes[pickedGeneId1].getSubsequence(recombinationPoint1, len);
						parent1Chromosome.genes[pickedGeneId1].setSequenceIntervall(recombinationPoint1, parent2Chromosome.genes[pickedGeneId1].getSubsequence(recombinationPoint1, len), 0, len);
						parent2Chromosome.genes[pickedGeneId1].setSequenceIntervall(recombinationPoint1, tmpGenePart, 0, len);
					
					} else {
				
						// gene at first cross over point
						int len = parent1Chromosome.genes[pickedGeneId1].getSequenceLength() - recombinationPoint1;
						GeneElement<T>[] tmpGenePart = parent1Chromosome.genes[pickedGeneId1].getSubsequence(recombinationPoint1, len);
						parent1Chromosome.genes[pickedGeneId1].setSequenceIntervall(recombinationPoint1, parent2Chromosome.genes[pickedGeneId1].getSubsequence(recombinationPoint1, len), 0, len);
						parent2Chromosome.genes[pickedGeneId1].setSequenceIntervall(recombinationPoint1, tmpGenePart, 0, len);
						
						// gene at second cross over point
						len = recombinationPoint2 + 1;
						tmpGenePart = parent1Chromosome.genes[pickedGeneId2].getSubsequence(0, len);
						parent1Chromosome.genes[pickedGeneId2].setSequenceIntervall(0, parent2Chromosome.genes[pickedGeneId2].getSubsequence(0, len), 0, len);
						parent2Chromosome.genes[pickedGeneId2].setSequenceIntervall(0, tmpGenePart, 0, len);
					}
					
					for(int gIdx = pickedGeneId1+1; gIdx < pickedGeneId2; gIdx++) {
						// swap the genes in between
						Gene<T> tmp = new Gene<T>(parent1Chromosome.genes[gIdx]);
						parent1Chromosome.genes[gIdx] = parent2Chromosome.genes[gIdx];
						parent2Chromosome.genes[gIdx] = tmp;
					}
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
		return "One point recombination";
	}


}
