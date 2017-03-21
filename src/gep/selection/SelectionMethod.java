package gep.selection;

import gep.model.Individual;
import gep.random.RandomEngine;

public interface SelectionMethod {

	/**
	 * Randomly selects individuals from a the current population to create a
	 * new one based on their fitness.
	 * 
	 * The returned index defines from which index in the population array later
	 * reproduction is allowed to occur. If the returned value is for example 2,
	 * it means that all following genetic operators should not change the
	 * elements at index 0 and 1.
	 * 
	 * @param population
	 *            The population which should be changed.
	 * 
	 * @return The index (inclusive) from which later reproduction is allowed to
	 *         occur.
	 */
	public <T> int select(Individual<T>[] population);

	/**
	 * Randomly selects individuals from a the current population to create a
	 * new one based on their fitness.
	 * 
	 * The returned index defines from which index in the population array later
	 * reproduction is allowed to occur. If the returned value is for example 2,
	 * it means that all following genetic operators should not change the
	 * elements at index 0 and 1.
	 * 
	 * @param population
	 *            The population which should be changed.
	 * @param random
	 *            The RandomEngine to be used
	 * 
	 * @return The index (inclusive) from which later reproduction is allowed to
	 *         occur.
	 */
	public <T> int select(Individual<T>[] population, RandomEngine random);

}
