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
