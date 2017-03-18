package gep.model;

import java.util.ArrayList;
import java.util.List;

public class ExpressionTreeNode<T> {
	
	private final GeneElement<T> nodeElement;
	
	private final ArrayList<ExpressionTreeNode<T>> children;

	public ExpressionTreeNode(GeneElement<T> nodeElement, ArrayList<ExpressionTreeNode<T>> children) {
		this.nodeElement = nodeElement;
		this.children = children;
	}
	
	// TODO maybe change to unmodifiable list (collections_unmodifiablelist)
	public List<ExpressionTreeNode<T>> getChildren() {
		return this.children;
	}
	
	public GeneElement<T> getNodeElement() {
		return this.nodeElement;
	}

}
