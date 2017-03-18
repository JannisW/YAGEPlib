package gep.operators;

import gep.model.Individual;

public interface GeneticOperator {

	/**
	 * Applies the genetic operator to the given set of individuals
	 * 
	 * @param individuals
	 *            The set of individuals
	 */
	void apply(Individual[] individuals);

	/**
	 * Returns the rate with which this operator is applied
	 * 
	 * @return the operators application rate
	 */
	double getApplicationRate();

	/**
	 * Returns the operators name.
	 * 
	 * @return the name of the genetic operator.
	 */
	String getName();

}
