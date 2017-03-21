package examples.behavior.terminals;

import examples.behavior.EvaluationEnvironment;
import gep.model.ChromosomeIndependentGeneTerminal;

public class PheroInFrontCheckTerminal extends ChromosomeIndependentGeneTerminal<Boolean> {

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
