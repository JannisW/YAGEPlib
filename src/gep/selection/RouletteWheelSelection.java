package gep.selection;

import java.util.Arrays;

import gep.model.Individual;
import gep.random.DefaultRandomEngine;
import gep.random.RandomEngine;

/**
 * 
 * Based on:
 * https://github.com/dwdyer/watchmaker/blob/master/framework/src/java/main/org/uncommons/watchmaker/framework/selection/RouletteWheelSelection.java
 * 
 * @author Johannes Wortmann, (Daniel Dyer)
 *
 */
public class RouletteWheelSelection implements SelectionMethod {

	private RandomEngine random;

	public RouletteWheelSelection() {
		random = new DefaultRandomEngine();
	}

	public RouletteWheelSelection(RandomEngine random) {
		this.random = random;
	}

	@Override
	public <T> void select(Individual<T>[] population) {
		select(population, this.random);
	}

	@Override
	public <T> void select(Individual<T>[] population, RandomEngine random) {

		// To avoid to copy too many objects keep the original objects before
		// copying
		// This exploits the fact that boolean arrays are initialized to false.
		boolean[] isPartOfNewPopulation = new boolean[population.length];

		Individual<T>[] oldPopulation = Arrays.copyOf(population, population.length);

		double comulativeFitness[] = new double[population.length];
		comulativeFitness[0] = population[0].getFitness();

		for (int i = 1; i < comulativeFitness.length; i++) {
			comulativeFitness[i] = comulativeFitness[i - 1] + population[i].getFitness();
		}

		for (int i = 0; i < population.length; i++) {
			double rndFitness = random.getDouble() * comulativeFitness[comulativeFitness.length - 1];
			int idx = Arrays.binarySearch(comulativeFitness, rndFitness);
			// translate negative results of binary search
			idx = Math.max(idx, -(idx + 1));

			if (isPartOfNewPopulation[idx]) {
				// just copy if could not reuse old one
				population[i] = new Individual<T>(oldPopulation[idx]);
			} else {
				population[i] = oldPopulation[idx];
				isPartOfNewPopulation[idx] = true;
			}
		}

	}

}
