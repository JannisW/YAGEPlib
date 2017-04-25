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
import gep.random.DefaultRandomEngine;
import gep.random.RandomEngine;

/**
 * This class represents a random choice node of a behavior tree. It randomly
 * selects one of its children, executes it and returns its return value.
 * 
 * @author Johannes Wortmann
 *
 */
public class RandomChoiceFunction extends GeneFunction<Boolean> {

	/**
	 * The version id used for serialization.
	 */
	private static final long serialVersionUID = 1131663166244611831L;

	/**
	 * The used random engine.
	 */
	private RandomEngine random;

	/**
	 * Creates a new instance of a random choice node in a behavior tree
	 * handling a random choice between two subtrees.
	 */
	public RandomChoiceFunction() {
		this(2);
	}

	/**
	 * Creates a new instance of a random choice node in a behavior tree
	 * handling a random choice between the given number of subtrees.
	 * 
	 * @param numberOfArguments
	 *            The number of subtrees this operator should choose from.
	 */
	public RandomChoiceFunction(int numberOfArguments) {
		super("random choice node", "R", numberOfArguments);
		random = new DefaultRandomEngine();
	}

	@Override
	public Boolean apply(List<ExpressionTreeNode<Boolean>> expTreeChilds) {

		int idx = random.getInt(0, expTreeChilds.size());

		return expTreeChilds.get(idx).execute();
	}

}
