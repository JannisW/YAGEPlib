package gep.model;

/**
 * TODO class useful?
 * @author jannis
 *
 */
public class Chromosome {

	public final Gene<?>[] genes;
	
	private Gene<Gene<?>> staticLinkingFunction; // TODO maybe move to architecture
	
	public Chromosome(int numberOfGenes) {
		genes = new Gene<?>[numberOfGenes];
	}
	
	public ExpressionTreeNode<?> express() {
		ExpressionTreeNode<?>[] expTreeCache = new ExpressionTreeNode<?>[genes.length];
		
		int i = 0;
		while(i < staticLinkingFunction.sequence.length) {			
			//staticLinkingFunction.sequence[i].
		}
		
		return null; // TODO implement that
	}

}
