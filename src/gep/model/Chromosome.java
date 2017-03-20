package gep.model;

import java.util.List;

/**
 * TODO class useful?
 * 
 * @author jannis
 * 
 *         TODO allow various types within one Chromosome (e.g.
 *         UntypedChromosome as a subclass or the need to provide conversion
 *         functions)
 *         
 *         TODO speedup creation currently lists are copied on construction...
 *
 */
public class Chromosome<T> {

	public final Gene<T>[] genes;
	public final Gene<T>[] immutableGenes;

	private final Gene<T> linkingFunction;
	
	@SuppressWarnings("unchecked")
	public Chromosome(List<Gene<T>> genes, List<Gene<T>> immutableGenes, Gene<T> linkingFunction) {
		this.genes = new Gene[genes.size()];
		genes.toArray(this.genes);
		this.immutableGenes = new Gene[immutableGenes.size()];
		immutableGenes.toArray(this.immutableGenes);
		this.linkingFunction = linkingFunction;
	}

	public ExpressionTreeNode<T> express() {
		ExpressionTreeNode<T> rootEtn = linkingFunction.express();
		return rootEtn;
	}

}
