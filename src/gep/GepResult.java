package gep;

import gep.model.Individual;

public class GepResult<T> {
	
	public final int numGenerations;
	public final int maxGenrations;
	public final Individual<T> bestIndivudal;
	
	public GepResult(int numGenerations, int maxGenerations, Individual<T> bestIndivudal) {
		this.numGenerations = numGenerations;
		this.maxGenrations = maxGenerations;
		this.bestIndivudal = bestIndivudal;
	}
	
	public double getFitnessOfBestIndivudal() {
		return bestIndivudal.getFitness();
	}

}
