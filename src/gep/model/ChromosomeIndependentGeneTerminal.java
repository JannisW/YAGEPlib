package gep.model;

public abstract class ChromosomeIndependentGeneTerminal<T> extends GeneTerminal<T> {

	public ChromosomeIndependentGeneTerminal(String description, String shortDescription) {
		super(description, shortDescription);
	}

	@Override
	public final T apply(Chromosome<T> executingChromosome) {
		return apply();
	}
	
	public abstract T apply();

}
