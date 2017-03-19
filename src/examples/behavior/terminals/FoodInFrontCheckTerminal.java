package examples.behavior.terminals;

import examples.behavior.EvaluationEnvironment;
import gep.model.GeneTerminal;

public class FoodInFrontCheckTerminal extends GeneTerminal<Boolean> {

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
