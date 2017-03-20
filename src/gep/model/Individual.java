package gep.model;

import java.util.ArrayList;
import java.util.List;

public class Individual<T> {

	public final Chromosome<T>[] chromosomes;

	private double fitness;

	public Individual(int numChromosomes) {
		// TODO find a better solution.... These Java generics argh...
		this.chromosomes = (Chromosome<T>[]) new Chromosome<?>[numChromosomes]; 
	}

	public double getFitness() {
		return this.fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness; // TODO maybe replace this by function to
								// calculate fitness directly here
	}
	
	public List<ExpressionTreeNode<T>> getExpressionTrees() {
		ArrayList<ExpressionTreeNode<T>> ets = new ArrayList<ExpressionTreeNode<T>>(chromosomes.length);
		
		for(int i = 0; i < chromosomes.length; i++) {
			ets.add(chromosomes[i].express());
		}
		
		return ets;
	}

}
