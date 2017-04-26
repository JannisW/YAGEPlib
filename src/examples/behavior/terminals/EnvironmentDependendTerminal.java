package examples.behavior.terminals;

import examples.behavior.fitness.EvaluationEnvironment;
import gep.model.ChromosomeIndependentGeneTerminal;

/**
 * Super class for all terminals that depend on the environment to be executed.
 * This means that they have a reference to the current fitness environment.
 * 
 * @author Johannes Wortmann
 *
 * @param <T>
 *            The return type of the terminal
 */
public abstract class EnvironmentDependendTerminal<T> extends ChromosomeIndependentGeneTerminal<T> {

	/**
	 * The version UID used for serialization.
	 */
	private static final long serialVersionUID = 3370070892189792917L;

	transient protected EvaluationEnvironment env;

	protected EnvironmentDependendTerminal(String description, String shortDescription, EvaluationEnvironment env) {
		super(description, shortDescription);
		this.env = env;
	}

	public void setEvaluationEnvironment(EvaluationEnvironment env) {
		this.env = env;
	}

}
