package gep.random;

import java.util.ArrayList;
import java.util.Random;

import gep.model.GeneElement;

/**
 * An implementation of the RandomEngine interface by using Java's default
 * random generator.
 */
public class DefaultRandomEngine extends Random implements RandomEngine {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = -7574675804698581874L;

	/**
	 * Decides if a function (true) or a terminal (false) should be generated.
	 * The probability is uniformly distributed so both functions and terminals
	 * have a probability of 0.5
	 * 
	 * @return true, if function shall be generated, false if instead a terminal
	 *         should be generated
	 */
	@Override
	public boolean decideTakeFunction() {
		return super.nextBoolean();
	}

	/**
	 * Picks a element from the given set of elements using a uniform
	 * probability distribution.
	 * 
	 * @param elements
	 *            The gene elements from which an element should be chosen.
	 * @return The randomly picked element
	 */
	public <T extends GeneElement<?>> T pickElement(ArrayList<T> elements) {
		return elements.get(super.nextInt(elements.size()));
	}

	@Override
	public boolean decideBinaryDecision(double probabilityYes) {
		return super.nextDouble() < probabilityYes;
	}

	@Override
	public boolean decideBinaryDecision() {
		return super.nextBoolean();
	}

}
