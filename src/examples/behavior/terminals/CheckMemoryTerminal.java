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
package examples.behavior.terminals;

import examples.behavior.fitness.EvaluationEnvironment;

public class CheckMemoryTerminal extends EnvironmentDependendTerminal<Boolean> {
	
	/**
	 * The version UID used for serialization.
	 */
	private static final long serialVersionUID = -5314622750709569326L;
	

	public CheckMemoryTerminal(EvaluationEnvironment env) {
		super("memory set?", "mem?", env);
	}

	@Override
	public Boolean apply() {
		return env.getAgentMemory();
	}

}
