package examples.behavior.terminals;

import examples.behavior.fitness.EvaluationEnvironment;
import examples.behavior.world.Orientation;
import gep.model.ChromosomeIndependentGeneTerminal;

public class TurnRightTerminal extends ChromosomeIndependentGeneTerminal<Boolean> {
	
	private EvaluationEnvironment env;

	public TurnRightTerminal(EvaluationEnvironment env) {
		super("turn right", "r");
		this.env = env;
	}

	@Override
	public Boolean apply() {
		final Orientation oldAgentOrientation = env.agentOrientation;
		switch(oldAgentOrientation) { // TODO replace switch-case to modulo add/subtract mechanism  
		case NORTH:
			env.agentOrientation = Orientation.EAST;
			break;
		case EAST:
			env.agentOrientation = Orientation.SOUTH;
			break;
		case SOUTH:
			env.agentOrientation = Orientation.WEST;
			break;
		case WEST:
			env.agentOrientation = Orientation.NORTH;
			break;
		}
		return true;
	}
	

}
