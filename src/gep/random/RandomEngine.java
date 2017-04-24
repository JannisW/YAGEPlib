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

import gep.model.GeneElement;

/**
 * This interface defines all methods required for a random engine used by this
 * GEP framework.
 * 
 * @author Johannes Wortmann
 *
 */
public interface RandomEngine {

	/**
	 * Decides if a function (true) or a terminal (false) should be taken if
	 * both decisions are possible.
	 * 
	 * @return true, if function shall be taken, false if instead a terminal
	 *         should be taken
	 */
	boolean decideTakeFunction();

	/**
	 * Picks a element from the given set of elements.
	 * 
	 * @param elements
	 *            The elements from which an element should be chosen.
	 * @return The randomly picked element
	 */
	<T extends GeneElement<?>> T pickElement(ArrayList<T> elements);

	/**
	 * Decides a binary decision with respect to the given probability of
	 * deciding for yes. If probabilityYes is 0.5 this is equal to toss a fair
	 * coin.
	 * 
	 * @param probabilityYes
	 *            The probability to say yes
	 * @return true, if the decision is yes. False, otherwise.
	 */
	boolean decideBinaryDecision(double probabilityYes);

	/**
	 * Decides a binary decision where both possibilities have the same
	 * probability (This is equal to toss a fair coin).
	 * 
	 * @return true, if the decision is yes. False, otherwise.
	 */
	boolean decideBinaryDecision();

	/**
	 * Returns a double between 0.0 and 1.0.
	 * 
	 * @return a double between 0.0 and 1.0
	 */
	double getDouble();

	/**
	 * Returns a integer between from (inclusive) and to (exclusive)
	 * 
	 * @param from
	 *            The smallest possible returned int
	 * @param to
	 *            The upper bound (exclusive)
	 * @return a random integer from the interval [from, to)
	 */
	public int getInt(int from, int to);
}
