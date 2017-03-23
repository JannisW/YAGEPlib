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
package gep.selection;

import gep.model.Individual;
import gep.random.DefaultRandomEngine;
import gep.random.RandomEngine;

public class StochasticUniversalSampling implements SelectionMethod {
	
	private RandomEngine random;

	public StochasticUniversalSampling() {
		random = new DefaultRandomEngine();
	}

	public StochasticUniversalSampling(RandomEngine random) {
		this.random = random;
	}

	@Override
	public <T> int select(Individual<T>[] population) {
		return select(population, this.random);
	}

	@Override
	public <T> int select(Individual<T>[] population, RandomEngine random) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Method not implemented yet");
		
		// no elite preservation
		// return 0;
	}

}
