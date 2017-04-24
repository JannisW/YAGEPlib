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
package gep.random;

import java.util.ArrayList;
import java.util.Random;

import gep.model.GeneElement;

/**
 * An implementation of the RandomEngine interface by using Java's default
 * random generator.
 * 
 * @author Johannes Wortmann
 */
public class DefaultRandomEngine extends Random implements RandomEngine {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = -7574675804698581874L;

	/**
	 * Creates an instance of the default random engine with the current time as
	 * seed.
	 */
	public DefaultRandomEngine() {
		super();
	}

	/**
	 * Creates an instance of the default random engine with the given seed.
	 * 
	 * @param seed
	 */
	public DefaultRandomEngine(long seed) {
		super(seed);
	}

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

	@Override
	public double getDouble() {
		return super.nextDouble();
	}

	@Override
	public int getInt(int fromIncl, int toExcl) {
		return fromIncl + super.nextInt(toExcl - fromIncl);
	}

}
