/*
 * Copyright 2017 Johannes Wortmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gep.model;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * This class represents a node in a expression tree. It is either a function
 * (regular node with children) or a terminal (leaf). Programs are encoded by
 * expression trees that itself are the result of the expression of the coding
 * regions of genes.
 * 
 * @author Johanes Wortmann
 *
 * @param <T>
 *            The return type of the execution of this node
 */
public class ExpressionTreeNode<T> implements Serializable {

	/**
	 * The version UID used for serialization.
	 */
	private static final long serialVersionUID = 1356055862072269250L;

	/**
	 * The actual function encoded by this node.
	 */
	private final GeneElement<T> nodeElement;

	/**
	 * The arguments of the function (empty list if terminal) encoded by this
	 * nodes element.
	 */
	private final ArrayList<ExpressionTreeNode<T>> children;

	/**
	 * The chromosome that contains the gene encoding this expression tree
	 */
	private final Chromosome<T> relatedChromosome;

	/**
	 * Creates a new node in an expression tree with an empty list of children.
	 * (This requires manually adding of children afterwards)
	 * 
	 * @param nodeElement
	 *            The actual function
	 * @param relatedChromosome
	 *            The chromosome that contains the gene that encodes this
	 *            expression tree
	 */
	ExpressionTreeNode(GeneElement<T> nodeElement, Chromosome<T> relatedChromosome) {
		this.nodeElement = nodeElement;
		this.children = new ArrayList<ExpressionTreeNode<T>>(nodeElement.getArity());
		this.relatedChromosome = relatedChromosome;
	}

	/**
	 * Creates a new node in an expression tree.
	 * 
	 * @param nodeElement
	 *            The actual function
	 * @param children
	 *            The arguments of the function (empty list if terminal)
	 * @param relatedChromosome
	 *            The chromosome that contains the gene that encodes this
	 *            expression tree
	 */
	ExpressionTreeNode(GeneElement<T> nodeElement, ArrayList<ExpressionTreeNode<T>> children,
			Chromosome<T> relatedChromosome) {
		this.nodeElement = nodeElement;
		this.children = children;
		this.relatedChromosome = relatedChromosome;
	}

	/**
	 * Returns the children of this node in the expression tree. The children
	 * are the arguments of the function encoded in this expression tree node.
	 * If this node is a leaf an empty list is returned. </br>
	 * </br>
	 * 
	 * The returned list is modifiable and not copied for performance reasons.
	 * However, the list must remain unmodified during the execution of the tree
	 * to ensure it's proper execution.
	 * 
	 * @return The list of children (arguments) of this node in the expression
	 *         tree
	 */
	public List<ExpressionTreeNode<T>> getChildren() {
		// TODO maybe change to unmodifiable list (collections_unmodifiablelist)
		// TODO and split in modifiable view and unmodifiable view
		// e.g. public List<? extends UnmodifiableExpressionTreeNode<T>>
		// getChildren()
		return this.children;
	}

	/**
	 * Returns this nodes gene element (the actual encoded function/terminal)
	 * 
	 * @return this nodes gene element.
	 */
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
