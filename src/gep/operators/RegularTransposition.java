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

import java.util.ArrayList;

import gep.model.Chromosome;
import gep.model.Gene;
import gep.model.GeneElement;
import gep.random.RandomEngine;

public class RegularTransposition extends GeneticOperator {

	public RegularTransposition(double applicationRate) {
		super(applicationRate);
	}

	public RegularTransposition(double applicationRate, RandomEngine re) {
		super(applicationRate, re);
	}

	@Override
	protected <T> void apply(Chromosome<T> c) {
		if (random.decideBinaryDecision(super.applicationRate)) {
			// apply operator
			int geneIdx = random.getInt(0, c.genes.length);
			final Gene<T> selectedIsGene = c.genes[geneIdx];

			int funcChosenStartIdx = random.getInt(0, selectedIsGene.getSequenceLength());
			int funcChosenEndIdx = random.getInt(0, selectedIsGene.getSequenceLength());
			if (funcChosenStartIdx > funcChosenEndIdx) {
				int tmp = funcChosenEndIdx;
				funcChosenEndIdx = funcChosenStartIdx;
				funcChosenStartIdx = tmp;
			}

			GeneElement<T>[] isElement = selectedIsGene.getSubsequence(funcChosenStartIdx,
					funcChosenEndIdx - funcChosenStartIdx + 1);

			// select gene in which the IS element should be inserted
			ArrayList<Gene<T>> genesWithMatchinArchitecture = new ArrayList<>();
			for (Gene<T> gene : c.genes) {
				if (gene.architecture.equals(selectedIsGene.architecture)) {
					genesWithMatchinArchitecture.add(gene);
				}
			}
			Gene<T> selectedInsertionTarget = genesWithMatchinArchitecture
					.get(random.getInt(0, genesWithMatchinArchitecture.size()));

			int insertionIdx = random.getInt(1, selectedInsertionTarget.architecture.headLength);

			// insert the IS element
			if (insertionIdx + isElement.length < selectedInsertionTarget.architecture.headLength) {
				for (int i = selectedInsertionTarget.architecture.headLength - 1; i >= isElement.length; i--) {
					selectedInsertionTarget.setSequenceAt(i, selectedInsertionTarget.sequenceAt(i - isElement.length));
				}
				selectedInsertionTarget.setSequenceIntervall(insertionIdx, isElement, 0, isElement.length);
			} else {
				selectedInsertionTarget.setSequenceIntervall(insertionIdx, isElement, 0,
						selectedInsertionTarget.architecture.headLength - insertionIdx);
			}
		}

	}

	@Override
	protected <T> void apply(Gene<T> gene) {
		// can't be called => method stub!
		throw new UnsupportedOperationException("This operator works on chromosome level");
	}

	@Override
	public String getName() {
		return "Transposition of IS Elements";
	}

}
