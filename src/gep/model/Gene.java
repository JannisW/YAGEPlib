package gep.model;

import java.util.ArrayDeque;
import java.util.Deque;

public class Gene<T> {

	final private GeneElement<T>[] sequence; // TODO maybe optimize by using
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

	/**
	 * Creates a new Gene with the copied (shallow) sequence and the same
	 * architecture as the provided gene.
	 * 
	 * @param other
	 *            The gene to be copied.
	 */
	public Gene(Gene<T> other) {
		this.architecture = other.architecture;
		this.sequence = new GeneElement[other.sequence.length];
		System.arraycopy(other.sequence, 0, sequence, 0, sequence.length);
		this.invalidateExpressionTreeCache();
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
		this.invalidateExpressionTreeCache();
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
	 * @param chromosome
	 *            The chromosome that contains this gene (TODO better
	 *            description??)
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
	 * Returns the element in the sequence at the given index
	 * 
	 * @param idx
	 *            The elements index
	 * @return The element at the given index
	 */
	public GeneElement<T> sequenceAt(int idx) {
		return sequence[idx];
	}

	/**
	 * Returns the subsequence specified by the parameters as a shallow copy
	 * 
	 * @param startIdx
	 *            The start index of the subsequence
	 * @param length
	 *            The length of the subsequence
	 * @return The subsequence as a shallow copy
	 * 
	 * @throws IndexOutOfBoundsException
	 *             - if copying would cause access of data outside array bounds.
	 */
	public GeneElement<T>[] getSubsequence(int startIdx, int length) {
		GeneElement<T>[] res = new GeneElement[length];
		System.arraycopy(sequence, startIdx, res, 0, length);
		return res;
	}

	/**
	 * Returns the element in the sequence at the given index
	 * 
	 * @param idx
	 *            The elements index
	 * @return The element at the given index
	 */
	public void setSequenceAt(int idx, GeneElement<T> newElement) {
		sequence[idx] = newElement;
		// TODO maybe change newElement to idx in architecture
		this.invalidateExpressionTreeCache();
	}

	/**
	 * Sets the sequence of this gene from pos to pos+length to the values given
	 * in the src array from srcPos to srcPos+length.
	 * 
	 * @param pos
	 *            The start position in the sequence
	 * @param src
	 *            The array from which the values should be copied
	 * @param srcPos
	 *            The start position in the source array
	 * @param length
	 *            The number of elements to be copied by the method
	 */
	public void setSequenceIntervall(int pos, GeneElement<T> src[], int srcPos, int length) {
		System.arraycopy(src, srcPos, sequence, pos, length);
		this.invalidateExpressionTreeCache();
	}

	/**
	 * Returns the length of the genetic sequence (length = length of head +
	 * length of tail).
	 * 
	 * Thus, the length includes non-coding regions of the gene.
	 * 
	 * @return The length of the sequence
	 */
	public int getSequenceLength() {
		return sequence.length;
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
