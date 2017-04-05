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

public class SetMemoryTerminal extends EnvironmentDependendTerminal<Boolean> {

	/**
	 * The version UID used for serialization.
	 */
	private static final long serialVersionUID = -7728819763182000043L;

	private final boolean memState;

	/**
	 * Creates a new Terminal that handles the one bit memory of the agent.
	 * 
	 * If the parameter memoryState is set to true, this terminal will set the agents memory to 1.
	 * If the parameters is instead set to false this terminal will set the memory to 0.
	 * 
	 * @param env
	 *            The environment in which this terminal is applied.
	 * @param memoryState
	 *            true if terminal should set memory to 1, false if it should set it to false
	 */
	public SetMemoryTerminal(EvaluationEnvironment env, boolean memoryState) {
		super("set memory", memoryState? "mem1": "mem0", env);
		this.memState = memoryState;
	}

	@Override
	public Boolean apply() {
		env.setAgentMemory(memState);
		return true;
	}

}
