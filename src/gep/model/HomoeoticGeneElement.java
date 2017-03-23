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

public class HomoeoticGeneElement<T> extends GeneTerminal<T> {

	public final int linkedGeneId;
	
	public HomoeoticGeneElement(int linkedGeneId) {
		super("Link to gene with id " + linkedGeneId, "ln"+linkedGeneId);
		this.linkedGeneId = linkedGeneId;
	}

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
