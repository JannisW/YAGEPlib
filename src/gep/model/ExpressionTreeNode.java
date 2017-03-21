package gep.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ExpressionTreeNode<T> {

	private final GeneElement<T> nodeElement;

	private final ArrayList<ExpressionTreeNode<T>> children;
	
	private final Chromosome<T> relatedChromosome;

	public ExpressionTreeNode(GeneElement<T> nodeElement, Chromosome<T> relatedChromosome) {
		this.nodeElement = nodeElement;
		this.children = new ArrayList<ExpressionTreeNode<T>>(nodeElement.getArity());
		this.relatedChromosome = relatedChromosome;
	}
	
	public ExpressionTreeNode(GeneElement<T> nodeElement, ArrayList<ExpressionTreeNode<T>> children, Chromosome<T> relatedChromosome) {
		this.nodeElement = nodeElement;
		this.children = children;
		this.relatedChromosome = relatedChromosome;
	}

	// TODO maybe change to unmodifiable list (collections_unmodifiablelist)
	// TODO and split in modifiable view and unmodifiable view
	// e.g. public List<? extends UnmodifiableExpressionTreeNode<T>> getChildren()
	public List<ExpressionTreeNode<T>> getChildren() {
		return this.children;
	}

	public GeneElement<T> getNodeElement() {
		return this.nodeElement;
	}
	
	/**
	 * Executes this expression tree by using this node as the root.
	 * 
	 * @return The result of the execution of the program defined by this
	 *         expression tree.
	 */
	public T execute() {
		return this.nodeElement.apply(children, relatedChromosome);
	}

	@Override
	public String toString() {
		Deque<ExpressionTreeNode<T>> queue = new ArrayDeque<>();

		StringBuilder sb = new StringBuilder();
		queue.addLast(this);

		int numNodesInLvl = 1;
		int numNodesInNextLvl = 0;

		int processedNodesInLvl = 0;

		while (!queue.isEmpty()) {
			ExpressionTreeNode<T> currRoot = queue.removeFirst();
		
			sb.append(currRoot.nodeElement.shortDescription);
			processedNodesInLvl++;

			numNodesInNextLvl += currRoot.children.size();
			
			if (processedNodesInLvl == numNodesInLvl) {
				// we reached the last node in the level
				sb.append('\n');
				processedNodesInLvl = 0;
				numNodesInLvl = numNodesInNextLvl;
				numNodesInNextLvl = 0;
			} else {
				sb.append('\t');
			}

			for (ExpressionTreeNode<T> child : currRoot.children) {
				queue.addLast(child);
			}
		}
		
		return sb.toString();
	}
}
