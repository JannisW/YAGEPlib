package gep;

import gep.model.Individual;

public abstract class FitnessEnvironment {

	/**
	 * Evaluates the fitness for every individual of the given population.
	 * 
	 * @param population
	 *            The population to be assessed by this method.
	 * @return The fitness value of the best individual
	 */
	public double evaluateFitness(Individual[] population) {
		double maxFitness = Double.MIN_VALUE;
		for (Individual individual : population) { // TODO parallelize
			individual.setFitness(evaluateFitness(individual));
			maxFitness = Math.max(maxFitness, individual.getFitness());
		}
		return maxFitness;
	}

	abstract protected double evaluateFitness(Individual individual);
	
	// maybe not abstract individual fitness evaluation (but add an init)

}
