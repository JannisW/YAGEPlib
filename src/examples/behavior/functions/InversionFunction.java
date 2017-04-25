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
package examples.behavior.functions;

import java.util.List;

import gep.model.ExpressionTreeNode;
import gep.model.GeneFunction;

/**
 * This class represents a inversion node of a behavior tree. It executes its
 * child and returns the inversion of its return value.
 * 
 * @author Johannes Wortmann
 *
 */
public class InversionFunction extends GeneFunction<Boolean> {

	/**
	 * The version id used for serialization.
	 */
	private static final long serialVersionUID = -7759013993227732330L;

	/**
	 * Creates a new instance of a inversion node in a behavior tree.
	 */
	public InversionFunction() {
		super("Inversion", "I", 1);
	}

	@Override
	public Boolean apply(List<ExpressionTreeNode<Boolean>> expTreeChilds) {

		return !expTreeChilds.get(0).execute();

	}

}
