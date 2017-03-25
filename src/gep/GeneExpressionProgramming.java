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

import java.io.IOException;
import java.nio.file.Paths;

import gep.model.Individual;
import gep.selection.SelectionMethod;

public class GeneExpressionProgramming {

	public static <T> void run(Individual<T>[] population, FitnessEnvironment<T> fe, SelectionMethod sm,
			ReproductionEnvironment re, int maxNumGenerations, double targetFitness) {

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

		} while (fitnessOfBestIndividual < targetFitness && currentGeneration < maxNumGenerations);

		try {
			population[bestIndividualIdx].writeToFile(Paths.get("./test.ser"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
