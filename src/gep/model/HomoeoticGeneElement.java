package gep.model;

public class HomoeoticGeneElement<T> extends GeneTerminal<T> {
	
	private final Gene<T> linkedGene;

	public HomoeoticGeneElement(String description, String shortDescription, Gene<T> linkedGene) {
		super(description, shortDescription);
		this.linkedGene = linkedGene;
	}

	@Override
	public T apply() {
		// use the fact that expression trees are cached so no overhead is created by that call.
		ExpressionTreeNode<T> etn = linkedGene.express();

		return linkedGene.sequence[0].apply(etn.getChildren());
	}

}
