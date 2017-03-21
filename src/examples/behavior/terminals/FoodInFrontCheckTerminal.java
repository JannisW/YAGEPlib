package examples.behavior.terminals;

import examples.behavior.fitness.EvaluationEnvironment;
import gep.model.ChromosomeIndependentGeneTerminal;

public class FoodInFrontCheckTerminal extends ChromosomeIndependentGeneTerminal<Boolean> {

	private EvaluationEnvironment env;

	public FoodInFrontCheckTerminal(EvaluationEnvironment env) {
		super("food?", "f");
		this.env = env;
	}

	@Override
	public Boolean apply() {
		return env.getFieldInFront().isFood();
	}

}
