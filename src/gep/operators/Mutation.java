package gep.operators;

import gep.model.Chromosome;
import gep.model.Gene;
import gep.model.GeneElement;
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
	public void apply(Individual[] individuals) {
		for (Individual individual : individuals) {
			for (Chromosome c : individual.chromosomes) {
				for (Gene g : c.genes) {
					for (int i = 0; i < g.sequence.length; i++) {
						if (random.decideBinaryDecision(mutationRate)) {
							if (g.isPartOfHead(i) && random.decideTakeFunction()) {
								g.sequence[i] = random.pickElement(g.architecture.potentialFunctions);
							} else {
								g.sequence[i] = random.pickElement(g.architecture.potentialTerminals);
							}
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
