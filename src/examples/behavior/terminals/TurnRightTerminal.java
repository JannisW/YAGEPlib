package examples.behavior.terminals;

import examples.behavior.EvaluationEnvironment;
import examples.behavior.EvaluationEnvironment.Orientation;
import gep.model.GeneTerminal;

public class TurnRightTerminal extends GeneTerminal<Boolean> {
	
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
		case EAST:
			env.agentOrientation = Orientation.SOUTH;
		case SOUTH:
			env.agentOrientation = Orientation.WEST;
		case WEST:
			env.agentOrientation = Orientation.NORTH;
		}
		return true;
	}
	

}
