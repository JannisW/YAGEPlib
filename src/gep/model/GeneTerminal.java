/*
 * Copyright 2017 Johannes Wortmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gep.model;

import java.util.List;

/**
 * This class represents a gene terminal. It is a leaf in an expression tree and
 * therefore its apply method does not get any childs as parameters.
 * 
 * @author Johannes Wortmann
 *
 * @param <T>
 *            The return type of the execution of this terminal
 */
public abstract class GeneTerminal<T> extends GeneElement<T> {

	/**
	 * The version UID used for serialization.
	 */
	private static final long serialVersionUID = -4752231745136597874L;

	/**
	 * Constructor for creating a gene terminal (leaf in an expression tree).
	 * 
	 * @param description
	 *            A string describing the function of the terminal
	 * @param shortDescription
	 *            A short form used to generate the string representation of the
	 *            terminal.
	 */
	protected GeneTerminal(String description, String shortDescription) {
		super(description, shortDescription);
	}

	@Override
	public final int getArity() {
		return 0;
	}

	@Override
	public final T apply(List<ExpressionTreeNode<T>> expTreeChilds, Chromosome<T> executingChromosome) {
		return apply(executingChromosome);
	}

	/**
	 * Executes this terminal and returns its result.
	 * 
	 * @param executingChromosome
	 *            A reference to the chromosome that includes this terminal and
	 *            wants to execute it.
	 * 
	 * @return The result of the execution of this terminal
	 */
	public abstract T apply(Chromosome<T> executingChromosome);

}
