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
