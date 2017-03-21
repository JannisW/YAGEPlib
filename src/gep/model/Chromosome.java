package gep.model;

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

	/**
	 * Creates a Chromosome that only contains one Gene.
	 * 
	 * @param gene
	 *            The contained gene.
	 */
	@SuppressWarnings("unchecked")
	public Chromosome(Gene<T> gene) {
		this.genes = new Gene[0];
		this.immutableGenes = new Gene[0];
		this.linkingFunction = gene;
	}

	public Chromosome(Gene<T>[] genes, Gene<T>[] immutableGenes, Gene<T> linkingFunction) {
		this.genes = genes;
		this.immutableGenes = immutableGenes;
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
		ExpressionTreeNode<T> rootEtn = linkingFunction.express(this);
		return rootEtn;
	}

	/**
	 * GeneId: = 0 linking function, > 0 normal gene (ascending) < 0 immutable
	 * gene (ascending, when negate)
	 * 
	 * @param geneId
	 * @return
	 */
	public Gene<T> getGene(int geneId) {
		if (geneId == 0) {
			return linkingFunction;
		} else if (geneId > 0) {
			return genes[geneId - 1];
		} else {
			return immutableGenes[(-geneId) - 1];
		}
	}

}
