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
