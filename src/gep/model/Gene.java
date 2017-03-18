package gep.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class Gene<T> {

	final public GeneElement<T>[] sequence; // TODO maybe optimize by using arrays
											// of int (index structure)
	// TODO maybe do explicit separation in head and tail

	public final GeneArchitecture<T> architecture;

	public Gene(GeneArchitecture<T> arch) {
		this.architecture = arch;
		this.sequence = new GeneElement[arch.getGeneLength()];
	}

	public void copyFrom(Gene<T> other) {
		if (this.architecture != other.architecture) {
			throw new IllegalArgumentException("The gene architecture has to be identical!");
		}
		System.arraycopy(sequence, 0, other.sequence, 0, sequence.length);
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
	
	public ExpressionTreeNode<T> express() {
		
		Deque<GeneElement<T>> elementStack = new ArrayDeque<GeneElement<T>>();
		Deque<ArrayList<ExpressionTreeNode<T>>> treeStack = new ArrayDeque<>();
		
		int idx = 0;
		elementStack.push(sequence[idx]);
		treeStack.push(new ArrayList<>(sequence[idx].getArity()));
		
		while(!elementStack.isEmpty()) { // could also be true as it will always return
			if(elementStack.peek().getArity() == treeStack.peek().size()) {
				ExpressionTreeNode<T> etn = new ExpressionTreeNode<>(elementStack.pop(), treeStack.pop());
				if(treeStack.isEmpty()) {
					return etn;
				}
				treeStack.peek().add(etn);
			} else {
				for(int i = 0; i< elementStack.peek().getArity(); i++) {
					// TODO maybe not just arity but also consider the number of already created expression trees					
					idx++;
					elementStack.push(sequence[idx]);
					treeStack.push(new ArrayList<>(sequence[idx].getArity()));
				}
			}
		}
		
		// can never happen
		return null;
	}

}
