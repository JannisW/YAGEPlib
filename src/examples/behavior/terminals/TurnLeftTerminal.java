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

/**
 * This class represents a terminal that lets the agent changes its orientation
 * by turning left. It always returns success as there is no option in which an
 * agent can't be turned.
 * 
 * @author Johannes Wortmann
 *
 */
public class TurnLeftTerminal extends EnvironmentDependendTerminal<Boolean> {

	/**
	 * The version UID used for serialization.
	 */
	private static final long serialVersionUID = -3616928667009598670L;

	public TurnLeftTerminal(EvaluationEnvironment env) {
		super("turn left", "l", env);
	}

	@Override
	public Boolean apply() {
		final Orientation oldAgentOrientation = env.getAgentOrientation();
		switch (oldAgentOrientation) { // TODO replace switch-case to modulo
										// add/subtract mechanism
		case NORTH:
			env.setAgentOrientation(Orientation.WEST);
			break;
		case EAST:
			env.setAgentOrientation(Orientation.NORTH);
			break;
		case SOUTH:
			env.setAgentOrientation(Orientation.EAST);
			break;
		case WEST:
			env.setAgentOrientation(Orientation.SOUTH);
			break;
		}
		return true;
	}

}
