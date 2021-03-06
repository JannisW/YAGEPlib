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
 * This class represents a selector node of a behavior tree. It will execute its
 * children until the first one returned success (the selector returns success)
 * or every child has been executed once (the selector returns failure).
 * 
 * @author Johannes Wortmann
 *
 */
public class SelectorFunction extends GeneFunction<Boolean> {

	/**
	 * The version id used for serialization.
	 */
	private static final long serialVersionUID = 52397171578116652L;

	/**
	 * Creates a new instance of a selector node in a behavior tree with two
	 * children.
	 */
	public SelectorFunction() {
		this(2);
	}

	/**
	 * Creates a new instance of a selector node in a behavior tree with the
	 * given number of children.
	 * 
	 * @param numberOfArguments
	 *            The number of children of the selector
	 */
	public SelectorFunction(int numberOfArguments) {
		super("Selector (fallback) node", "F", numberOfArguments);
	}

	@Override
	public Boolean apply(List<ExpressionTreeNode<Boolean>> expTreeChilds) {
		for (ExpressionTreeNode<Boolean> child : expTreeChilds) {

			// execute every child as long as one child returns success
			if (child.execute()) {
				return true;
			}
		}
		return false;
	}

}
