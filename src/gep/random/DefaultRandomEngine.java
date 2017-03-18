package gep.random;

import java.util.ArrayList;
import java.util.Random;

import gep.model.GeneElement;

public class DefaultRandomEngine implements RandomEngine {

	/**
	 * Java's default random generator
	 */
	private Random r;

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
		return r.nextBoolean();
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
		return elements.get(r.nextInt(elements.size()));
	}

	@Override
	public boolean decideBinaryDecision(double probabilityYes) {
		return r.nextDouble() < probabilityYes;
	}

	@Override
	public boolean decideBinaryDecision() {
		return r.nextBoolean();
	}

}
