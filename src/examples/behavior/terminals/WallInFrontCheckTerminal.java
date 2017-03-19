package examples.behavior.terminals;

import examples.behavior.EvaluationEnvironment;
import gep.model.GeneTerminal;

public class WallInFrontCheckTerminal extends GeneTerminal<Boolean> {

	private EvaluationEnvironment env;

	public WallInFrontCheckTerminal(EvaluationEnvironment env) {
		super("wall?", "w");
		this.env = env;
	}

	@Override
	public Boolean apply() {
		return env.getFieldInFront().isWall();
	}

}
