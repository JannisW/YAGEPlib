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

public class RandomChoiceFunction extends GeneFunction<Boolean> {

	private RandomEngine random;

	public RandomChoiceFunction() {
		this(2);
	}

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
