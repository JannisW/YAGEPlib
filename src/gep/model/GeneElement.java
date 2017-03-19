package gep.model;

import java.util.List;

public abstract class GeneElement<T> {
	
	String description;
	String shortDescription;


	public GeneElement(String description, String shortDescription) {
		this.description = description;
		this.shortDescription = shortDescription;
	}
	
	public abstract int getArity();
	
	public abstract T apply(List<ExpressionTreeNode<T>> expTreeChilds);
	
	@Override
	public String toString() {
		return this.shortDescription;
	}
}
