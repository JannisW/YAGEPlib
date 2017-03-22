package examples.behavior.terminals;

import examples.behavior.fitness.EvaluationEnvironment;
import examples.behavior.world.Orientation;
import gep.model.ChromosomeIndependentGeneTerminal;

public class TurnLeftTerminal extends ChromosomeIndependentGeneTerminal<Boolean> {
	
	private EvaluationEnvironment env;

	public TurnLeftTerminal(EvaluationEnvironment env) {
		super("turn left", "l");
		this.env = env;
	}

	@Override
	public Boolean apply() {
		final Orientation oldAgentOrientation = env.getAgentOrientation();
		switch(oldAgentOrientation) { // TODO replace switch-case to modulo add/subtract mechanism  
		case NORTH:
			env.setAgentOrientation(Orientation.WEST);
			break;
		case EAST:
			env.setAgentOrientation(Orientation.NORTH);
			break;
		case SOUTH:
			env.setAgentOrientation(Orientation.EAST);
			break;
		case WEST:
			env.setAgentOrientation(Orientation.SOUTH);
			break;
		}
		return true;
	}
	

}
