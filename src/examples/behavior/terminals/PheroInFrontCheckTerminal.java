package examples.behavior.terminals;

import examples.behavior.EvaluationEnvironment;
import gep.model.GeneTerminal;

public class PheroInFrontCheckTerminal extends GeneTerminal<Boolean> {

	private EvaluationEnvironment env;

	public PheroInFrontCheckTerminal(EvaluationEnvironment env) {
		super("phero?", "p");
		this.env = env;
	}

	@Override
	public Boolean apply() {
		return env.getFieldInFront().isPhero();
	}

}
