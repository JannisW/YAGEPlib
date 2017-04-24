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

import java.io.Serializable;
import java.util.List;

/**
 * This class represents an element in a genes sequence. It is either a terminal
 * (leaf) or a function (regular node) in the expression tree.
 * 
 * @author Johannes Wortmann
 *
 * @param <T>
 *            The return type of the execution of this gene element
 */
public abstract class GeneElement<T> implements Serializable {

	/**
	 * The version UID used for serialization.
	 */
	private static final long serialVersionUID = -1464840519465038769L;

	/**
	 * A string describing the function of this element
	 */
	protected final String description;

	/**
	 * A short form used to generate the string representation of the element.
	 */
	protected final String shortDescription;

	/**
	 * Constructor for creating a gene element (token in gene sequence and node
	 * in expression tree).
	 * 
	 * @param description
	 *            A string describing the function of the element
	 * @param shortDescription
	 *            A short form used to generate the string representation of the
	 *            element.
	 */
	protected GeneElement(String description, String shortDescription) {
		this.description = description;
		this.shortDescription = shortDescription;
	}

	/**
	 * Returns the number of argument this element takes when executed. If it is
	 * a terminal this function is constant and has to return 0.
	 * 
	 * @return The number of arguments for the encoded function.
	 */
	public abstract int getArity();

	/**
	 * Executes this gene element and returns its result.
	 * 
	 * @param expTreeChilds
	 *            The children (arguments) of this element in the expression
	 *            tree
	 * @param executingChromosome
	 *            A reference to the chromosome that includes this element and
	 *            wants to execute it.
	 * 
	 * @return The result of the execution of this element
	 */
	public abstract T apply(List<ExpressionTreeNode<T>> expTreeChilds, Chromosome<T> executingChromosome);

	@Override
	public String toString() {
		return this.shortDescription;
	}
}
