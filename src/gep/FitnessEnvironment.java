package gep;

import gep.model.Individual;

public abstract class FitnessEnvironment<T> {

	/**
	 * Evaluates the fitness for every individual of the given population.
	 * 
	 * @param population
	 *            The population to be assessed by this method.
	 * @return The fitness value of the best individual
	 */
	public double evaluateFitness(Individual<T>[] population) {
		double maxFitness = Double.MIN_VALUE;
		for (Individual<T> individual : population) { // TODO parallelize
			individual.setFitness(evaluateFitness(individual));
			maxFitness = Math.max(maxFitness, individual.getFitness());
		}
		return maxFitness;
	}

	abstract protected double evaluateFitness(Individual<T> individual);
	
	// maybe not abstract individual fitness evaluation (but add an init)

}
