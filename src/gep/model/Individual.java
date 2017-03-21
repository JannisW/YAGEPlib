package gep.model;

import java.util.ArrayList;
import java.util.List;

public class Individual<T> implements Comparable<Individual<T>> {

	public final Chromosome<T>[] chromosomes;

	private double fitness;
	
	public Individual(int numChromosomes) {
		// TODO find a better solution.... These Java generics argh...
		this.chromosomes = (Chromosome<T>[]) new Chromosome<?>[numChromosomes];
	}

	public Individual(Individual<T> other) {
		this.chromosomes = (Chromosome<T>[]) new Chromosome<?>[other.chromosomes.length];
		for (int i = 0; i < chromosomes.length; i++) {
			this.chromosomes[i] = new Chromosome(other.chromosomes[i]);
		}
		this.fitness = other.fitness;
	}

	public double getFitness() {
		return this.fitness;
	}

	public void setFitness(double fitness) {
		if (fitness < 0.0) {
			throw new IllegalArgumentException(
					"The fitness of an individual has to be >= 0 (value was " + fitness + ")");
		}
		this.fitness = fitness; // TODO maybe replace this by function to
								// calculate fitness directly here
	}

	public List<ExpressionTreeNode<T>> getExpressionTrees() {
		ArrayList<ExpressionTreeNode<T>> ets = new ArrayList<ExpressionTreeNode<T>>(chromosomes.length);

		for (int i = 0; i < chromosomes.length; i++) {
			ets.add(chromosomes[i].express());
		}

		return ets;
	}

	@Override
	public int compareTo(Individual<T> o) {
		return Double.compare(fitness, o.fitness); // TODO check if has to be
													// inverted
	}

}
