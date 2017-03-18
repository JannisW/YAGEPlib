package gep.selection;

import gep.model.Individual;

public interface SelectionMethod {

	/**
	 * Randomly selects individuals from a the current population to create a
	 * new one based on their fitness.
	 * 
	 * @param population
	 *            The population which should be changed.
	 */
	public void select(Individual[] population);

}
