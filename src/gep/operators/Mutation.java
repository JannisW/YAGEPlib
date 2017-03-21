package gep.operators;

import gep.model.Chromosome;
import gep.model.Gene;
import gep.model.Individual;
import gep.random.DefaultRandomEngine;
import gep.random.RandomEngine;

public class Mutation implements GeneticOperator {

	private double mutationRate;

	private final RandomEngine random;

	public Mutation(double mutationRate) {
		this.mutationRate = mutationRate;
		this.random = new DefaultRandomEngine();
	}

	public Mutation(double mutationRate, RandomEngine re) {
		this.mutationRate = mutationRate;
		this.random = re;
	}

	@Override
	public <T> void apply(Individual<T>[] individuals) {
		for (Individual<T> individual : individuals) {
			for (Chromosome<T> c : individual.chromosomes) {
				for (Gene<T> g : c.genes) {
					for (int i = 0; i < g.sequence.length; i++) {
						if (random.decideBinaryDecision(mutationRate)) {
							if (g.isPartOfHead(i) && random.decideTakeFunction()) {
								g.sequence[i] = random.pickElement(g.architecture.potentialFunctions);
							} else {
								g.sequence[i] = random.pickElement(g.architecture.potentialTerminals);
							}
							g.invalidateExpressionTreeCache();
						}
					}
				}
			}
		}
	}

	@Override
	public double getApplicationRate() {
		return mutationRate;
	}

	@Override
	public String getName() {
		return "Mutation";
	}

}
