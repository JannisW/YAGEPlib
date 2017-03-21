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
	private final Gene<T>[] immutableGenes;

	private final Gene<T> linkingFunction;

	@SuppressWarnings("unchecked")
	public Chromosome(List<Gene<T>> genes, List<Gene<T>> immutableGenes, Gene<T> linkingFunction) {
		this.genes = new Gene[genes.size()];
		genes.toArray(this.genes);
		this.immutableGenes = new Gene[immutableGenes.size()];
		immutableGenes.toArray(this.immutableGenes);
		this.linkingFunction = linkingFunction;
	}

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
		this.linkingFunction = new Gene<T>(other.linkingFunction);
	}

	public ExpressionTreeNode<T> express() {
		ExpressionTreeNode<T> rootEtn = linkingFunction.express();
		return rootEtn;
	}

}
