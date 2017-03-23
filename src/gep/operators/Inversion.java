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

import gep.model.Gene;
import gep.model.GeneElement;
import gep.random.RandomEngine;

public class Inversion extends GeneticOperator {

	public Inversion(double applicationRate) {
		super(applicationRate);
	}

	public Inversion(double applicationRate, RandomEngine re) {
		super(applicationRate, re);
	}

	@Override
	protected <T> void apply(Gene<T> gene) {
		// decide if operator should actually be applied
		if (random.decideBinaryDecision(super.applicationRate)) {
			int idx1 = random.getInt(0, gene.architecture.headLength);
			int idx2 = random.getInt(0, gene.architecture.headLength);

			if (idx1 > idx2) {
				int tmp = idx1;
				idx1 = idx2;
				idx2 = tmp;
			}

			for (int i = 0; i < (idx2 - idx1 + 1) / 2; i++) {
				GeneElement<T> tmp = gene.sequenceAt(idx1 + i);
				gene.setSequenceAt(idx1 + i, gene.sequenceAt(idx2 - i));
				gene.setSequenceAt(idx2 - i, tmp);
			}
		}

	}

	@Override
	public String getName() {
		return "Inversion";
	}

}
