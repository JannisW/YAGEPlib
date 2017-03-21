package gep.selection;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

import gep.model.Individual;
import gep.random.DefaultRandomEngine;
import gep.random.RandomEngine;

public class RouletteWheelSelectionWithElitePreservation implements SelectionMethod {

	private RandomEngine random;

	private final double preservationPercentage;

	public RouletteWheelSelectionWithElitePreservation() {
		this(new DefaultRandomEngine(), 0.1);
	}

	public RouletteWheelSelectionWithElitePreservation(double preservationPercentage) {
		this(new DefaultRandomEngine(), preservationPercentage);
	}

	public RouletteWheelSelectionWithElitePreservation(RandomEngine random, double preservationPercentage) {
		this.random = random;
		this.preservationPercentage = preservationPercentage;
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

		final Individual<T>[] oldPopulation = Arrays.copyOf(population, population.length);

		final int numElitesPreserved = Math.max(1, (int) (population.length * preservationPercentage));
		PriorityQueue<Integer> eliteIdx = new PriorityQueue<>(numElitesPreserved, new IndexedIndividualComparator<>(oldPopulation));

		double comulativeFitness[] = new double[population.length];
		comulativeFitness[0] = population[0].getFitness();
		eliteIdx.add(0);
		// TODO might be improved by filling the complete pq here (so one if in
		// the loop can be deleted)

		for (int i = 1; i < comulativeFitness.length; i++) {
			comulativeFitness[i] = comulativeFitness[i - 1] + population[i].getFitness();
			if (eliteIdx.size() == numElitesPreserved) {
				// priority queue is full
				if (oldPopulation[eliteIdx.peek()].compareTo(population[i]) < 0) {
					// the worst individual of the elite is worse than the
					// current individual
					// => replace it by the current one
					eliteIdx.poll();
					eliteIdx.add(i);
				}
			} else {
				eliteIdx.add(i);
			}
		}

		// preserve elite
		for (int i = 0; i < numElitesPreserved; i++) {
			final int idx = eliteIdx.poll();
			population[i] = oldPopulation[idx];
			isPartOfNewPopulation[idx] = true;
		}

		// fill remaining population
		for (int i = numElitesPreserved; i < population.length; i++) {
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

	class IndexedIndividualComparator<T> implements Comparator<Integer> {

		private final Individual<T>[] oldPopulation;

		public IndexedIndividualComparator(Individual<T>[] oldPopulation) {
			this.oldPopulation = oldPopulation;
		}

		@Override
		public int compare(Integer idx1, Integer idx2) {
			return oldPopulation[idx1].compareTo(oldPopulation[idx2]);
		}

	}

}
