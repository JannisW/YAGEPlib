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
import examples.behavior.world.Orientation;

public class StepTerminal extends EnvironmentDependendTerminal<Boolean> {

	/**
	 * The version UID used for serialization.
	 */
	private static final long serialVersionUID = -7648448858533698600L;
	
	public StepTerminal(EvaluationEnvironment env) {
		super("step", "s", env);
	}

	@Override
	public Boolean apply() {
		final Orientation oldAgentOrientation = env.getAgentOrientation();
		switch (oldAgentOrientation) { // TODO replace switch-case to modulo add/subtract mechanism
		case NORTH:
			return env.moveTo(env.getPosAgentX(), env.getPosAgentY() + 1);
		case EAST:
			return env.moveTo(env.getPosAgentX() + 1, env.getPosAgentY());
		case SOUTH:
			return env.moveTo(env.getPosAgentX(), env.getPosAgentY() - 1);
		case WEST:
			return env.moveTo(env.getPosAgentX() - 1, env.getPosAgentY());
		}
		return true;
	}

}
