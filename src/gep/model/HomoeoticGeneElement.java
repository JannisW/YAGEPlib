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
