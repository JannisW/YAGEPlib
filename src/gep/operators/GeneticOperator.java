/*
 * Copyright 2017 Johannes Wortmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gep.operators;

import gep.model.Chromosome;
import gep.model.Gene;
import gep.model.Individual;
import gep.random.DefaultRandomEngine;
import gep.random.RandomEngine;

/**
 * <p>
 * This class describes a generic genetic operator. Every new genetic operator
 * has to inherit from this class.
 * </p><p>
 * Most of the basic operators described in Ferreira, C.: Gene Expression
 * Programming: Mathematical Modeling by an Artificial Intelligence. 2 edn.
 * Springer-Verlag Berlin Heidelberg (2006) are already implemented
 * </p>
 * 
 * @author Johannes Wortmann
 *
 */
public abstract class GeneticOperator {

	protected double applicationRate;

	protected final RandomEngine random;

	protected GeneticOperator(double applicationRate) {
		this.applicationRate = applicationRate;
		this.random = new DefaultRandomEngine();
	}

	protected GeneticOperator(double applicationRate, RandomEngine re) {
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
