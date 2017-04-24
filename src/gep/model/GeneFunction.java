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

public abstract class GeneFunction<T> extends GeneElement<T> {

	/**
	 * The version UID used for serialization.
	 */
	private static final long serialVersionUID = -3756290864514509377L;

	/**
	 * The number of arguments the function takes when executed.
	 */
	private final int arity;

	/**
	 * Constructor for creating a gene function (regular node (non-leaf) in an
	 * expression tree).
	 * 
	 * @param description
	 *            A string describing the function of the terminal
	 * @param shortDescription
	 *            A short form used to generate the string representation of the
	 *            terminal.
	 * @param arity
	 *            the number of arguments the function takes
	 */
	protected GeneFunction(String description, String shortDescription, int arity) {
		super(description, shortDescription);
		this.arity = arity;
	}

	@Override
	public T apply(List<ExpressionTreeNode<T>> expTreeChilds, Chromosome<T> executingChromosome) {
		// functions are chromosome independent
		return apply(expTreeChilds);
	}

	/**
	 * Executes this gene function and returns its result.
	 * 
	 * @param expTreeChilds
	 *            The children (arguments) of this function in the expression
	 *            tree
	 * 
	 * @return The result of the execution of this funtion
	 */
	public abstract T apply(List<ExpressionTreeNode<T>> expTreeChilds);

	@Override
	public int getArity() {
		return arity;
	}

}
