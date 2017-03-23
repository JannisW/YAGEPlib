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
package gep.operators;

import gep.model.Chromosome;
import gep.model.Gene;
import gep.model.GeneElement;
import gep.model.Individual;
import gep.random.RandomEngine;

public class OnePointRecombination extends GeneticOperator {

	public OnePointRecombination(double applicationRate) {
		super(applicationRate);
	}

	public OnePointRecombination(double applicationRate, RandomEngine re) {
		super(applicationRate, re);
	}

	@Override
	public <T> void apply(Individual<T>[] individuals, final int fromIdx) {
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
					int recombinationPoint = random.getInt(0, parent1Chromosome.genes[pickedGeneId].getSequenceLength());

					int len = parent1Chromosome.genes[pickedGeneId].getSequenceLength() - recombinationPoint;
					GeneElement<T>[] tmpGenePart = parent1Chromosome.genes[pickedGeneId].getSubsequence(recombinationPoint, len);
					parent1Chromosome.genes[pickedGeneId].setSequenceIntervall(recombinationPoint, parent2Chromosome.genes[pickedGeneId].getSubsequence(recombinationPoint, len), 0, len);
					parent2Chromosome.genes[pickedGeneId].setSequenceIntervall(recombinationPoint, tmpGenePart, 0, len);
					
					
					for(int gIdx = pickedGeneId+1; gIdx < parent1Chromosome.genes.length; gIdx++) {
						// swap the gene
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
