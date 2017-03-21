package gep.model;

import java.util.List;

public abstract class GeneTerminal<T> extends GeneElement<T> {
	
	public GeneTerminal(String description, String shortDescription) {
		super(description, shortDescription);
	}
	
	@Override
	final public int getArity() {
		return 0;
	}
	
	public final T apply(List<ExpressionTreeNode<T>> expTreeChilds, Chromosome<T> executingChromosome) {
		return apply(executingChromosome);
	}
	
	public abstract T apply(Chromosome<T> executingChromosome);
	
}
