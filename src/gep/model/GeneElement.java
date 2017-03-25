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

import java.io.Serializable;
import java.util.List;

public abstract class GeneElement<T> implements Serializable {
	
	/**
	 * The version UID used for serialization.
	 */
	private static final long serialVersionUID = -1464840519465038769L;
	
	String description;
	String shortDescription;


	public GeneElement(String description, String shortDescription) {
		this.description = description;
		this.shortDescription = shortDescription;
	}
	
	public abstract int getArity();
	
	public abstract T apply(List<ExpressionTreeNode<T>> expTreeChilds, Chromosome<T> executingChromosome);
	
	@Override
	public String toString() {
		return this.shortDescription;
	}
}
