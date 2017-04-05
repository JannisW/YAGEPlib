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
package gep;

import gep.model.Individual;
import gep.selection.SelectionMethod;

/**
 * This class contains the GEP control. It contains the static method that
 * executes the GEP workflow.
 * 
 * @author Johannes Wortmann
 *
 */
public class GeneExpressionProgramming {

	/**
	 * Runs Gene Expression Programming to find a solution to the problem
	 * defined by the given parameters. </br>
	 * This function will modify and update the given population array per
	 * generation.
	 * 
	 * @param population
	 *            The initial population
	 * @param fe
	 *            The fitness environment used to assess the fitness of individuals
	 * @param sm
	 *            The selection method to select individuals from the current
	 *            generation to be part of the next one
	 * @param re
	 *            The reproduction environment used to genetically modify
	 *            individuals
	 * @param maxNumGenerations
	 *            The maximum number of generations
	 * @param targetFitness
	 *            The optimal value of the fitness function (set to positive
	 *            infinity if not known)
	 * @param fitnessEpsilon
	 *            The error allowed when comparing the floating point
	 *            targetFitness with the current best fitness
	 * @return The result of the GEP execution.
	 */
	public static <T> GepResult<T> run(Individual<T>[] population, FitnessEnvironment<T> fe, SelectionMethod sm,
			ReproductionEnvironment re, final int maxNumGenerations, final double targetFitness,
			final double fitnessEpsilon) {

		int bestIndividualIdx;
		double fitnessOfBestIndividual;
		int currentGeneration = 0;

		System.out.println("Start GEP evolution...");
		bestIndividualIdx = fe.evaluateFitness(population);
		fitnessOfBestIndividual = population[bestIndividualIdx].getFitness();
		System.out.println(
				" Initial generation " + currentGeneration + " (Best fitness: " + fitnessOfBestIndividual + ")");

		do {
			int modStartIdx = sm.select(population);
			re.reproduce(population, modStartIdx);
			bestIndividualIdx = fe.evaluateFitness(population);
			fitnessOfBestIndividual = population[bestIndividualIdx].getFitness();
			currentGeneration++;
			System.out.println(
					"Finished generation " + currentGeneration + " (Best fitness: " + fitnessOfBestIndividual + ")");

		} while (Math.abs(fitnessOfBestIndividual - targetFitness) < fitnessEpsilon
				&& currentGeneration < maxNumGenerations);

		return new GepResult<>(currentGeneration, maxNumGenerations, population[bestIndividualIdx], targetFitness);
	}

	/**
	 * Runs Gene Expression Programming to find a solution to the problem
	 * defined by the given parameters. </br>
	 * This function will modify and update the given population array per
	 * generation. </br>
	 * This function sets the allowed error to zero. It is therefore only
	 * recommended for integer fitness functions. In case of possible floating
	 * point values use the overloaded functions that takes an epsilon as last
	 * parameter.
	 * 
	 * @param population
	 *            The initial population
	 * @param fe
	 *            The fitness environment used to assess the fitness of individuals
	 * @param sm
	 *            The selection method to select individuals from the current
	 *            generation to be part of the next one
	 * @param re
	 *            The reproduction environment used to genetically modify
	 *            individuals
	 * @param maxNumGenerations
	 *            The maximum number of generations
	 * @param targetFitness
	 *            The optimal value of the fitness function (set to positive
	 *            infinity if not known)
	 * @return The result of the GEP execution.
	 */
	public static <T> GepResult<T> run(Individual<T>[] population, FitnessEnvironment<T> fe, SelectionMethod sm,
			ReproductionEnvironment re, final int maxNumGenerations, final double targetFitness) {
		return run(population, fe, sm, re, maxNumGenerations, targetFitness, 0.0);
	}

}
