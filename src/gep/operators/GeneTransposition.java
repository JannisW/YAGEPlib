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
import gep.random.RandomEngine;

public class GeneTransposition extends GeneticOperator {

	public GeneTransposition(double applicationRate) {
		super(applicationRate);
	}

	public GeneTransposition(double applicationRate, RandomEngine re) {
		super(applicationRate, re);
	}

	@Override
	protected <T> void apply(Chromosome<T> c) {
		if (random.decideBinaryDecision(super.applicationRate)) {
			// apply operator
			int transposonGeneIdx = random.getInt(1, c.genes.length);

			Gene<T> transposon = c.genes[transposonGeneIdx];
			for (int i = transposonGeneIdx; i > 0; i--) {
				c.genes[i] = c.genes[i - 1];
			}
			c.genes[0] = transposon;
		}

	}

	@Override
	protected <T> void apply(Gene<T> gene) {
		// can't be called => method stub!
		throw new UnsupportedOperationException("This operator works on chromosome level");
	}

	@Override
	public String getName() {
		return "Gene Transposition";
	}

}
