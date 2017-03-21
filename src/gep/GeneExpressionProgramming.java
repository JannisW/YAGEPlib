package gep;

import gep.model.Individual;
import gep.selection.SelectionMethod;

public class GeneExpressionProgramming {

	public static <T> void run(Individual<T>[] population, FitnessEnvironment<T> fe, SelectionMethod sm,
			ReproductionEnvironment re, int maxNumGenerations, double targetFitness) {

		double fitnessOfBestIndividual;
		int currentGeneration = 0;

		System.out.println("Start GEP evolution...");

		do {
			fitnessOfBestIndividual = fe.evaluateFitness(population);
			sm.select(population);
			re.reproduce(population);
			currentGeneration++;
			System.out.println(
					"Finished generation " + currentGeneration + " (Best fitness: " + fitnessOfBestIndividual + ")");
		} while (fitnessOfBestIndividual < targetFitness && currentGeneration <= maxNumGenerations);

	}

}
