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
package gep.model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an individual with a set of chromosomes and fitness.
 * Multiple individuals form a population.
 * 
 * For the creation of Individuals use the factory methods in {@link IndividualArchitecture}.
 * 
 * @author Johannes Wortmann
 *
 * @param <T>
 *            The return type of the elements in the expression tree encoded by
 *            an individual
 *            
 * @see IndividualArchitecture
 */
public class Individual<T> implements Comparable<Individual<T>>, Serializable {

	/**
	 * The version UID used for serialization.
	 */
	private static final long serialVersionUID = -9199945441121316518L;

	/**
	 * The chromosomes of the individual
	 */
	public final Chromosome<T>[] chromosomes;

	/**
	 * The individuals fitness
	 */
	private double fitness;

	/**
	 * Creates a new Individual with space for the given amount of chromosomes.
	 * This function should not be called directly. Instead use the factory
	 * methods provided by {@link IndividualArchitecture}
	 * 
	 * @param numChromosomes
	 */
	@SuppressWarnings("unchecked")
	Individual(int numChromosomes) {
		// TODO find a better solution.... These Java generics argh...
		this.chromosomes = (Chromosome<T>[]) new Chromosome<?>[numChromosomes];
	}

	/**
	 * Copy constructor. Creates a new Individual with the same set of
	 * chromosomes. This method will recursively call the copy constructor of
	 * the {@link Chromosome} class. This leads to a deep copy of the other
	 * individual.
	 * 
	 * @param other
	 *            The individual to be copied.
	 */
	@SuppressWarnings("unchecked")
	public Individual(Individual<T> other) {
		this.chromosomes = (Chromosome<T>[]) new Chromosome<?>[other.chromosomes.length];
		for (int i = 0; i < chromosomes.length; i++) {
			this.chromosomes[i] = new Chromosome<T>(other.chromosomes[i]);
		}
		this.fitness = other.fitness;
	}

	/**
	 * Returns the fitness of this individual. This function does not calculate
	 * the fitness. It simply returns the value that was set before by using
	 * {@link Individual#setFitness()}.
	 * 
	 * @see Individual#setFitness().
	 * 
	 * @return the individuals fitness
	 */
	public double getFitness() {
		return this.fitness;
	}

	/**
	 * Sets the fitness of this individual to the given non negative value.
	 * 
	 * @param fitness
	 *            The individuals new fitness
	 */
	public void setFitness(double fitness) {
		if (fitness < 0.0) {
			throw new IllegalArgumentException(
					"The fitness of an individual has to be >= 0 (value was " + fitness + ")");
		}
		this.fitness = fitness;
	}

	/**
	 * Returns the expression trees (programs) encoded by all of this
	 * individuals chromosomes. The method guarantees that the order of the
	 * expression trees matches the order of the chromosomes.
	 * 
	 * @return The expression trees encoded by this individual
	 */
	public List<ExpressionTreeNode<T>> getExpressionTrees() {
		ArrayList<ExpressionTreeNode<T>> ets = new ArrayList<ExpressionTreeNode<T>>(chromosomes.length);

		for (int i = 0; i < chromosomes.length; i++) {
			ets.add(chromosomes[i].express());
		}

		return ets;
	}

	/**
	 * Compares two individuals based on their fitness values.
	 * 
	 * @param o
	 *            The other individual to which this one should be compared.
	 * 
	 * @return a negative integer, zero, or a positive integer as this
	 *         individuals fitness is less than, equal to, or greater than the
	 *         fitness of the specified object.
	 */
	@Override
	public int compareTo(Individual<T> o) {
		return Double.compare(fitness, o.fitness);
	}

	/**
	 * Writes this individual to file.
	 * 
	 * @param outPath
	 *            The path to which the individual should be written-
	 * @throws IOException
	 *             if an IOException occurs during serialization.
	 */
	public void writeToFile(Path outPath) throws IOException {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(outPath.toFile()))) {
			out.writeObject(this);
		}
	}

}
