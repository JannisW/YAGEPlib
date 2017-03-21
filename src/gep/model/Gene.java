package gep.model;

import java.util.ArrayDeque;
import java.util.Deque;

public class Gene<T> {

	final public GeneElement<T>[] sequence; // TODO maybe optimize by using
											// arrays
											// of int (index structure)
	// TODO maybe do explicit separation in head and tail

	public final GeneArchitecture<T> architecture;

	private ExpressionTreeNode<T> expressionTreeCache = null;

	// TODO check if the constructor can be protected from the outside... (worth
	// it? as it might complicate genetic operations
	/**
	 * Creates a new Gene linked to the given architecture. To make sure the
	 * sequence is consistent with the architecture this constructor should not
	 * be called directly. Instead it's recommended to use the factory method in
	 * GeneArchitecture to create a Gene according to the given architecture.
	 * 
	 * @param arch
	 *            the genes architecture reference.
	 */
	public Gene(GeneArchitecture<T> arch) {
		this.architecture = arch;
		this.sequence = new GeneElement[arch.getGeneLength()];
	}

	public Gene(Gene<T> other) {
		this.architecture = other.architecture;
		this.sequence = new GeneElement[other.sequence.length];
		System.arraycopy(other.sequence, 0, sequence, 0, sequence.length);
		this.expressionTreeCache = null;
	}
	
	/**
	 * Copies the sequence from the given other Gene into this one. This
	 * requires that both Genes share the exact same architecture.
	 * 
	 * @param other
	 *            The Gene from which the sequence should be copied.
	 */
	public void copyFrom(Gene<T> other) {
		if (this.architecture != other.architecture) {
			throw new IllegalArgumentException("The gene architecture has to be identical!");
		}
		System.arraycopy(other.sequence, 0, sequence, 0, sequence.length);
		expressionTreeCache = null;
	}

	/**
	 * Returns true if the given index belong to the head of the gene's
	 * sequence. This function assumes that all passed indices are greater or
	 * equal to zero.
	 * 
	 * @param idx
	 *            The index to be checked (>= 0)
	 * @return True, if idx is art of the head. False, otherwise.
	 */
	public boolean isPartOfHead(int idx) {
		return idx < architecture.headLength;
	}

	/**
	 * Creates the ExpressionTree which is encoded by the current configuration
	 * of this gene. The ExpressionTree will only contain those gene elements
	 * which are in the coding region of the Gene.
	 * 
	 * @param chromosome The chromosome that contains this gene (TODO better description??)
	 * 
	 * @return The ExpressionTree encoded by this Gene in its current
	 *         configuration.
	 */
	public ExpressionTreeNode<T> express(Chromosome<T> chromosome) {

		if (expressionTreeCache != null) {
			return expressionTreeCache;
		}

		Deque<ExpressionTreeNode<T>> elementQueue = new ArrayDeque<ExpressionTreeNode<T>>();
		ExpressionTreeNode<T> etnRoot = new ExpressionTreeNode<T>(sequence[0], chromosome);
		elementQueue.push(etnRoot);
		int idx = 1;

		while (!elementQueue.isEmpty()) {
			ExpressionTreeNode<T> currParent = elementQueue.removeFirst();
			for (int i = 0; i < currParent.getNodeElement().getArity(); i++) {
				ExpressionTreeNode<T> currChild = new ExpressionTreeNode<>(sequence[idx], chromosome);
				currParent.getChildren().add(currChild);
				elementQueue.addLast(currChild);
				idx++;
			}
		}

		expressionTreeCache = etnRoot;
		return etnRoot;
	}

	/**
	 * Invalidates the cache holding the latest expression tree.
	 * 
	 * This will make sure that the stored reference to the latest
	 * ExpressionTree will be deleted and a new one will be created when
	 * {@link express()} is called.
	 */
	public void invalidateExpressionTreeCache() {
		expressionTreeCache = null;
	}

}
