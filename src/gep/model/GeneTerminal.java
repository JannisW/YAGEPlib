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
	
	@Override
	final public T apply(List<ExpressionTreeNode<T>> expTreeChilds) {
		return apply();
	}
	
	public abstract T apply();
	
}