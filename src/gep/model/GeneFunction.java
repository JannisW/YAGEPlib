package gep.model;

import java.util.List;

public abstract class GeneFunction<T> extends GeneElement<T> {
	
	private final int arity;
	
	public GeneFunction(String description, String shortDescription, int arity) {
		super(description, shortDescription);
		this.arity = arity;
	}
	
	public T apply(List<ExpressionTreeNode<T>> expTreeChilds, Chromosome<T> executingChromosome) {
		// functions are chromosome independent
		return apply(expTreeChilds);
	}
	
	public abstract T apply(List<ExpressionTreeNode<T>> expTreeChilds);
	
	@Override
	public int getArity() {
		return arity;
	}

}
