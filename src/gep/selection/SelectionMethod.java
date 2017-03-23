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
package gep.selection;

import gep.model.Individual;
import gep.random.RandomEngine;

public interface SelectionMethod {

	/**
	 * Randomly selects individuals from a the current population to create a
	 * new one based on their fitness.
	 * 
	 * The returned index defines from which index in the population array later
	 * reproduction is allowed to occur. If the returned value is for example 2,
	 * it means that all following genetic operators should not change the
	 * elements at index 0 and 1.
	 * 
	 * @param population
	 *            The population which should be changed.
	 * 
	 * @return The index (inclusive) from which later reproduction is allowed to
	 *         occur.
	 */
	public <T> int select(Individual<T>[] population);

	/**
	 * Randomly selects individuals from a the current population to create a
	 * new one based on their fitness.
	 * 
	 * The returned index defines from which index in the population array later
	 * reproduction is allowed to occur. If the returned value is for example 2,
	 * it means that all following genetic operators should not change the
	 * elements at index 0 and 1.
	 * 
	 * @param population
	 *            The population which should be changed.
	 * @param random
	 *            The RandomEngine to be used
	 * 
	 * @return The index (inclusive) from which later reproduction is allowed to
	 *         occur.
	 */
	public <T> int select(Individual<T>[] population, RandomEngine random);

}
