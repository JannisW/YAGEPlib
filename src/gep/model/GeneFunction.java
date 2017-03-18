package gep.model;

public abstract class GeneFunction<T> extends GeneElement<T> {
	
	private final int arity;
	
	public GeneFunction(String description, String shortDescription, int arity) {
		super(description, shortDescription);
		this.arity = arity;
	}
	
	@Override
	public int getArity() {
		return arity;
	}

}
