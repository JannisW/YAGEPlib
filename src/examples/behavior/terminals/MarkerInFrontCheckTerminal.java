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

/**
 * This class represents a terminal that returns success if the field in front
 * of the agent was marked by the agent, and failure otherwise.
 * 
 * @author Johannes Wortmann
 *
 */
public class MarkerInFrontCheckTerminal extends EnvironmentDependendTerminal<Boolean> {

	/**
	 * The version UID used for serialization.
	 */
	private static final long serialVersionUID = 5315742182746593086L;
	

	public MarkerInFrontCheckTerminal(EvaluationEnvironment env) {
		super("marker?", "m?", env);
	}

	@Override
	public Boolean apply() {
		return env.getFieldInFront().isMarker();
	}

}
