package gep.model;

/**
 * TODO class useful?
 * @author jannis
 * 
 * TODO allow various types within one Chromosome (e.g. UntypedChromosome as a subclass or the need
 * to provide conversion functions)
 *
 */
public class Chromosome<T> {

	public final Gene<T>[] genes;
	
	private Gene<T> linkingFunction; // TODO maybe move to architecture
	
	public Chromosome(int numberOfGenes) {
		// TODO maybe modify the constructor such that it gets the clazz of T so
		// (T[])Array.newInstance(clazz, capacity); can be used
		genes = (Gene<T>[]) new Gene<?>[numberOfGenes]; 
	}
	
	public ExpressionTreeNode<T> express() {
		ExpressionTreeNode<T> rootEtn = linkingFunction.express();
		return rootEtn;
	}

}
