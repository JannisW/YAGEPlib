package examples.behavior.terminals;

import examples.behavior.fitness.EvaluationEnvironment;
import gep.model.ChromosomeIndependentGeneTerminal;

public class EmptyInFrontCheckTerminal extends ChromosomeIndependentGeneTerminal<Boolean> {

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
