package gep.selection;

import gep.model.Individual;
import gep.random.RandomEngine;

public interface SelectionMethod {

	/**
	 * Randomly selects individuals from a the current population to create a
	 * new one based on their fitness.
	 * 
	 * @param population
	 *            The population which should be changed.
	 */
	public <T> void select(Individual<T>[] population);

	/**
	 * Randomly selects individuals from a the current population to create a
	 * new one based on their fitness.
	 * 
	 * @param population
	 *            The population which should be changed.
	 * @param random
	 *            The RandomEngine to be used
	 */
	public <T> void select(Individual<T>[] population, RandomEngine random);

}
