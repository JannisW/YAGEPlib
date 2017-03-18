package gep.model;

import java.util.ArrayList;
import java.util.List;

public class Individual {

	public final Chromosome[] chromosomes;

	private double fitness;

	public Individual(int numChromosomes) {
		this.chromosomes = new Chromosome[numChromosomes];
	}

	public double getFitness() {
		return this.fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness; // TODO maybe replace this by function to
								// calculate fitness directly here
	}
	
	public List<ExpressionTreeNode<?>> getExpressionTrees() {
		ArrayList<ExpressionTreeNode<?>> ets = new ArrayList<ExpressionTreeNode<?>>(chromosomes.length);
		
		for(int i = 0; i < ets.size(); i++) {
			ets.add(chromosomes[i].express());
		}
		
		return ets;
	}

}
