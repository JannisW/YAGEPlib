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

/**
 * This class implements a Chromosome, which is basically a collection of
 * modifiable and unmodifiable genes linked by a linking function.
 * 
 * @author Johannes Wortmann
 * 
 * @param <T>
 *            The return parameter type of the GeneElements contained in this
 *            Chromosomes genes.
 * 
 *            TODO allow various types within one Chromosome (e.g.
 *            UntypedChromosome as a subclass or the need to provide conversion
 *            functions).
 */
public class Chromosome<T> implements Serializable {

	/**
	 * The version UID used for serialization.
	 */
	private static final long serialVersionUID = 8552801008044841155L;

	/**
	 * The collection of modifiable genes of the chromosome
	 */
	public final Gene<T>[] genes;

	/**
	 * The immutable genes of the chromosome (e.g. immutable sub linking
	 * constructs)
	 */
	private final Gene<T>[] immutableGenes;

	/**
	 * The top level linking function of the chromosome (immutable)
	 */
	private final Gene<T> staticLinkingFunction;

	/**
	 * Creates a new Chromosome with the given collections of genes, immutable
	 * genes and the given linking function.
	 * 
	 * It's not recommended to use this contructor directly. Instead use one of
	 * the methods provided by {@link ChromosomalArchitecture}.
	 * 
	 * @param genes
	 *            An array containing all modifiable genes of the chromosome
	 * @param immutableGenes
	 *            An array containing all unmodifiable genes of the chromosome
	 * @param linkingFunction
	 *            The chromosomes linking function encoded as gene
	 */
	Chromosome(Gene<T>[] genes, Gene<T>[] immutableGenes, Gene<T> linkingFunction) {
		this.genes = genes;
		this.immutableGenes = immutableGenes;
		this.staticLinkingFunction = linkingFunction;
	}

	/**
	 * Creates a new Chromosome with the copied genes and linking function from
	 * the provided other Chromosome.
	 * 
	 * @param other
	 *            The Chromosome to be copied.
	 */
	@SuppressWarnings("unchecked")
	public Chromosome(Chromosome<T> other) {
		this.genes = new Gene[other.genes.length];
		for (int i = 0; i < genes.length; i++) {
			this.genes[i] = new Gene<T>(other.genes[i]);
		}
		this.immutableGenes = new Gene[other.immutableGenes.length];
		for (int i = 0; i < immutableGenes.length; i++) {
			// TODO maybe no hard copy needed here as immutable (share objects)
			this.immutableGenes[i] = new Gene<T>(other.immutableGenes[i]);
		}
		// TODO no hard copy needed here as immutable
		this.staticLinkingFunction = new Gene<T>(other.staticLinkingFunction);
	}

	/**
	 * Expresses this Chromosome. This means this function creates and returns
	 * the ExpressionTree that encodes all the coding regions of this
	 * Chromosome.
	 * 
	 * @return The Chromosomes ExpressionTree
	 */
	public ExpressionTreeNode<T> express() {
		ExpressionTreeNode<T> rootEtn = staticLinkingFunction.express(this);
		return rootEtn;
	}

	/**
	 * Returns the gene of this Chromosome corresponding to the given gene id.
	 * 
	 * <pre>
	 * GeneId: = 0 linking function, 
	 *         > 0 normal gene (ascending) 
	 *         < 0 immutable gene (ascending, after id negation)
	 * </pre>
	 * 
	 * @param geneId
	 *            The gene's id
	 * @return The corresponding gene
	 */
	public Gene<T> getGene(int geneId) {
		if (geneId == 0) {
			return staticLinkingFunction;
		} else if (geneId > 0) {
			return genes[geneId - 1];
		} else {
			return immutableGenes[(-geneId) - 1];
		}
	}

}
