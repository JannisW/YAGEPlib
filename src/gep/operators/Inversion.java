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
				GeneElement<T> tmp = gene.sequence[idx1 + i];
				gene.sequence[idx1 + i] = gene.sequence[idx2 - i];
				gene.sequence[idx2 - i] = tmp;
			}
		}

	}

	@Override
	public String getName() {
		return "Inversion";
	}

}
