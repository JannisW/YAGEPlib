package gep.operators;

import gep.model.Chromosome;
import gep.model.Gene;
import gep.model.Individual;
import gep.random.DefaultRandomEngine;
import gep.random.RandomEngine;

public abstract class GeneticOperator {

	protected double applicationRate;

	protected final RandomEngine random;

	public GeneticOperator(double applicationRate) {
		this.applicationRate = applicationRate;
		this.random = new DefaultRandomEngine();
	}

	public GeneticOperator(double applicationRate, RandomEngine re) {
		this.applicationRate = applicationRate;
		this.random = re;
	}

	/**
	 * Applies the genetic operator to the given array of individuals starting
	 * at index fromIdx. This is the only public function and thus the function
	 * called by the GEP framework.
	 * 
	 * If the genetic operator works on the level of populations this method has
	 * to be overridden. In this case make sure the from parameter is respected.
	 * 
	 * @param fromIdx
	 *            The index (inclusive) from which on the population should be
	 *            changed by this operator.
	 * 
	 * @param population
	 *            The set of individuals
	 */
	public <T> void apply(Individual<T>[] population, int fromIdx) {
		for (int i = fromIdx; i < population.length; i++) {
			apply(population[i]);
		}
	}

	/**
	 * Applies the genetic operator to the given individual.
	 * 
	 * If the genetic operator works on the level of a individual this method
	 * has to be overridden.
	 * 
	 * @param individual
	 *            The individual
	 */
	protected <T> void apply(Individual<T> individual) {
		for (Chromosome<T> c : individual.chromosomes) {
			apply(c);
		}
	}

	/**
	 * Applies the genetic operator to the given Chromosome.
	 * 
	 * If the genetic operator works on the level of a chromosome this method
	 * has to be overridden.
	 * 
	 * @param c
	 *            The Chromosome
	 */
	protected <T> void apply(Chromosome<T> c) {
		for (Gene<T> g : c.genes) {
			apply(g);
		}
	}

	/**
	 * Applies the genetic operator to the given gene.
	 * 
	 * @param gene
	 *            The gene that should be modified.
	 */
	protected abstract <T> void apply(Gene<T> gene);

	/**
	 * Returns the rate with which this operator is applied
	 * 
	 * @return the operators application rate
	 */
	public double getApplicationRate() {
		return this.applicationRate;
	}

	/**
	 * Returns the operators name.
	 * 
	 * @return the name of the genetic operator.
	 */
	public abstract String getName();

}
