package examples.behavior.terminals;

import examples.behavior.EvaluationEnvironment;
import examples.behavior.EvaluationEnvironment.Orientation;
import gep.model.GeneTerminal;

public class TurnLeftTerminal extends GeneTerminal<Boolean> {
	
	private EvaluationEnvironment env;

	public TurnLeftTerminal(EvaluationEnvironment env) {
		super("turn left", "l");
		this.env = env;
	}

	@Override
	public Boolean apply() {
		final Orientation oldAgentOrientation = env.agentOrientation;
		switch(oldAgentOrientation) { // TODO replace switch-case to modulo add/subtract mechanism  
		case NORTH:
			env.agentOrientation = Orientation.WEST;
		case EAST:
			env.agentOrientation = Orientation.NORTH;
		case SOUTH:
			env.agentOrientation = Orientation.EAST;
		case WEST:
			env.agentOrientation = Orientation.SOUTH;
		}
		return true;
	}
	

}