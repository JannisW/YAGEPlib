package examples.behavior.terminals;

import examples.behavior.EvaluationEnvironment;
import gep.model.GeneTerminal;

public class EmptyInFrontCheckTerminal extends GeneTerminal<Boolean> {

	private EvaluationEnvironment env;

	public EmptyInFrontCheckTerminal(EvaluationEnvironment env) {
		super("empty?", "e");
		this.env = env;
	}

	@Override
	public Boolean apply() {
		return env.getFieldInFront().isEmpty();
	}

}
