package gep;

import gep.model.Individual;
import gep.selection.SelectionMethod;

public class GeneExpressionProgramming {

	public static void run(Individual[] population, FitnessEnvironment fe, SelectionMethod sm,
			ReproductionEnvironment re, int maxNumGenerations, double targetFitness) {

		double fitnessOfBestIndividual;
		int currentGeneration = 0;

		do {
			fitnessOfBestIndividual = fe.evaluateFitness(population);
			sm.select(population);
			re.reproduce(population);
			currentGeneration++;
		} while (fitnessOfBestIndividual < targetFitness && currentGeneration <= maxNumGenerations);

	}

}
