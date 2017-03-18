package examples.behavior;

import gep.FitnessEnvironment;
import gep.model.Individual;

public class EvaluationEnvironment extends FitnessEnvironment {
	
	// TODO maybe use getter + setter
	public int posAgentX;
	public int posAgentY;
	public Orientation agentOrientation; 
	
	// TODO change 2d array to 1d array
	Field[][] grid;
	
	public class Field {
		
	}
	
	public enum Orientation {
		NORTH, EAST, SOUTH, WEST;
	}

	@Override
	protected double evaluateFitness(Individual individual) {
		// TODO Auto-generated method stub
		return 0;
	}

}
