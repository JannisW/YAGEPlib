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
				g.invalidateExpressionTreeCache();
			}
		}

	}

	@Override
	public String getName() {
		return "Mutation";
	}

}
