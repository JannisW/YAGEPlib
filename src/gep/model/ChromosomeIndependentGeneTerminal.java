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

/**
 * This class represents a gene terminal whose execution is independent from the
 * chromosome it is included in. That means its apply method does not need the
 * chromosome as a parameter.
 * 
 * @author Johannes Wortmann
 *
 * @param <T>
 *            The return type of the execution of this terminal
 */
public abstract class ChromosomeIndependentGeneTerminal<T> extends GeneTerminal<T> {

	/**
	 * The version UID used for serialization.
	 */
	private static final long serialVersionUID = -1555228040137706984L;

	/**
	 * Constructor for creating a gene terminal whose execution is independent
	 * from the chromosome it is included in.
	 * 
	 * @param description
	 *            A string describing the function of the terminal
	 * @param shortDescription
	 *            A short form used to generate the string representation of the
	 *            terminal.
	 */
	protected ChromosomeIndependentGeneTerminal(String description, String shortDescription) {
		super(description, shortDescription);
	}

	@Override
	public final T apply(Chromosome<T> executingChromosome) {
		return apply();
	}

	/**
	 * Executes this terminal and returns its result.
	 * 
	 * @return The result of the execution of this terminal
	 */
	public abstract T apply();

}
