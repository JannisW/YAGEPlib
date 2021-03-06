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
import gep.random.RandomEngine;

public class Mutation extends GeneticOperator {

	public Mutation(double mutationRate) {
		super(mutationRate);
	}

	public Mutation(double mutationRate, RandomEngine re) {
		super(mutationRate, re);
	}

	@Override
	public <T> void apply(Gene<T> g) {
		for (int i = 0; i < g.getSequenceLength(); i++) {
			if (random.decideBinaryDecision(super.applicationRate)) {
				if (g.isPartOfHead(i) && random.decideTakeFunction()) {
					g.setSequenceAt(i, random.pickElement(g.architecture.potentialFunctions));
				} else {
					g.setSequenceAt(i, random.pickElement(g.architecture.potentialTerminals));
				}
			}
		}

	}

	@Override
	public String getName() {
		return "Mutation";
	}

}
