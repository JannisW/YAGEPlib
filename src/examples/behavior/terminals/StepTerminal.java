package examples.behavior.terminals;

import examples.behavior.EvaluationEnvironment;
import examples.behavior.EvaluationEnvironment.Orientation;
import gep.model.GeneTerminal;

public class StepTerminal extends GeneTerminal<Boolean> {

	private EvaluationEnvironment env;

	public StepTerminal(EvaluationEnvironment env) {
		super("step", "s");
		this.env = env;
	}

	@Override
	public Boolean apply() {
		final Orientation oldAgentOrientation = env.agentOrientation;
		switch (oldAgentOrientation) { // TODO replace switch-case to modulo add/subtract mechanism
		case NORTH:
			return env.moveTo(env.getPosAgentX(), env.getPosAgentY() + 1);
		case EAST:
			return env.moveTo(env.getPosAgentX() + 1, env.getPosAgentY());
		case SOUTH:
			return env.moveTo(env.getPosAgentX(), env.getPosAgentY() - 1);
		case WEST:
			return env.moveTo(env.getPosAgentX() - 1, env.getPosAgentY() + 1);
		}
		return true;
	}

}
