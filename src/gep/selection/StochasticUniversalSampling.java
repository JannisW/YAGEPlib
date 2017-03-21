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
