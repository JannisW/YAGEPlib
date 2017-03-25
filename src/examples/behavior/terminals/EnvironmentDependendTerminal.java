package examples.behavior.terminals;

import examples.behavior.fitness.EvaluationEnvironment;
import gep.model.ChromosomeIndependentGeneTerminal;

public abstract class EnvironmentDependendTerminal<T> extends ChromosomeIndependentGeneTerminal<T> {

	/**
	 * The version UID used for serialization.
	 */
	private static final long serialVersionUID = 3370070892189792917L;
	
	transient protected EvaluationEnvironment env;

	public EnvironmentDependendTerminal(String description, String shortDescription, EvaluationEnvironment env) {
		super(description, shortDescription);
		this.env = env;
	}
	
	public void setEvaluationEnvironment(EvaluationEnvironment env) {
		this.env = env;
	}

}
