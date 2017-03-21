package examples.behavior.terminals;

import examples.behavior.EvaluationEnvironment;
import gep.model.ChromosomeIndependentGeneTerminal;

public class WallInFrontCheckTerminal extends ChromosomeIndependentGeneTerminal<Boolean> {

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
