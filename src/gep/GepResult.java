package gep;

import gep.model.Individual;

/**
 * This class represents the result of a GEP run and includes meta information
 * and information about the best individual.
 * 
 * @author Johannes Wortmann
 *
 * @param <T>
 *            The return type of a node in the encoded expression tree (=the
 *            type of gene elements) and therefore the type used by individuals.
 */
public class GepResult<T> {

	/**
	 * The number of generations until the GEP run stopped.
	 */
	public final int numGenerations;

	/**
	 * The maximum number of generations the GEP run was allowed to use.
	 */
	public final int maxGenrations;

	/**
	 * A reference to the best found individual.
	 */
	public final Individual<T> bestIndividual;

	/**
	 * The target fitness of the GEP run (might be infinity if optimum not
	 * known)
	 */
	public final double targetFitness;

	/**
	 * Creates a new GepResult. (used by the GEP engine to publish its result)
	 * 
	 * @param numGenerations
	 *            The number of generations until the GEP run stopped.
	 * @param maxGenerations
	 *            The maximum number of generations the GEP run was allowed to
	 *            use
	 * @param targetFitness
	 *            The target fitness of the GEP run (might be infinity if
	 *            optimum not known)
	 * @param bestIndividual
	 *            The best found individual
	 */
	GepResult(int numGenerations, int maxGenerations, Individual<T> bestIndividual, double targetFitness) {
		this.numGenerations = numGenerations;
		this.maxGenrations = maxGenerations;
		this.bestIndividual = bestIndividual;
		this.targetFitness = targetFitness;
	}

	/**
	 * Convenient function that returns the fitness of the best found
	 * individual.
	 * 
	 * @return The fitness of the best individual.
	 */
	public double getFitnessOfBestIndivudal() {
		return bestIndividual.getFitness();
	}

	/**
	 * Returns true if an optimal solution was found. False otherwise. An
	 * optimal solution has been found if there is an individual that reached
	 * the target fitness.
	 * 
	 * @param epsilon
	 *            The error allowed when comparing the floating point fitness
	 *            values
	 * 
	 * @return True, if optimal solution was found. False, otherwise.
	 */
	public boolean isOptimalSolution(double epsilon) {
		return Math.abs(getFitnessOfBestIndivudal() - targetFitness) < epsilon;
	}

}
