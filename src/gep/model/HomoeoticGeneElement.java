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
 * This class represents a homoeotic gene terminal. It is a leaf in an
 * expression tree but links to another gene that will be executed when this
 * terminal will be executed. This allows to evolve the hierarchy of genes in
 * GEP by itself.
 * 
 * @author Johannes Wortmann
 *
 * @param <T>
 *            The return type of the execution of this terminal
 */
public class HomoeoticGeneElement<T> extends GeneTerminal<T> {

	/**
	 * The version UID used for serialization.
	 */
	private static final long serialVersionUID = 4812792159220536809L;

	/**
	 * The index of the gene in the chromosome that is linked by this homoeotic
	 * terminal.
	 */
	public final int linkedGeneId;

	/**
	 * Creates a homoeotic gene that links to the gene with the given gene id.
	 * 
	 * The description is automatically generated.
	 * 
	 * @param linkedGeneId
	 *            The id of the gene this gene element links to
	 */
	public HomoeoticGeneElement(int linkedGeneId) {
		super("Link to gene with id " + linkedGeneId, "ln" + linkedGeneId);
		this.linkedGeneId = linkedGeneId;
	}

	/**
	 * Creates a homoeotic gene that links to the gene with the given gene id.
	 * 
	 * @param linkedGeneId
	 *            The id of the gene this gene element links to
	 * 
	 * @param description
	 *            A string describing the function of the terminal
	 * @param shortDescription
	 *            A short form used to generate the string representation of the
	 *            terminal.
	 */
	public HomoeoticGeneElement(String description, String shortDescription, int linkedGeneId) {
		super(description, shortDescription);
		this.linkedGeneId = linkedGeneId;
	}

	@Override
	public T apply(Chromosome<T> executingChromosome) {
		Gene<T> linkedGene = executingChromosome.getGene(linkedGeneId);

		// use the fact that expression trees are cached so no overhead is
		// created by that call.
		ExpressionTreeNode<T> etn = linkedGene.express(executingChromosome);

		return linkedGene.sequenceAt(0).apply(etn.getChildren(), executingChromosome);
	}

}
