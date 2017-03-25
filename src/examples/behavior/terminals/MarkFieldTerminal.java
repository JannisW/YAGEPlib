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

public class MarkFieldTerminal extends EnvironmentDependendTerminal<Boolean> {

	/**
	 * The version UID used for serialization.
	 */
	private static final long serialVersionUID = 5264892072006949273L;

	private final boolean addMarker;

	/**
	 * Creates a new Terminal that handles the marking of fields.
	 * 
	 * If the parameter addMarker is set to true, this terminal will add a mark
	 * on the current position of the agent in the provided environment. If
	 * addMarker is set to false, this terminal will instead remove a marker
	 * from the current position (if present), when triggered.
	 * 
	 * @param env
	 *            The environment in which this terminal is applied.
	 * @param addMarker
	 *            true if terminal should add markers, false if it should remove
	 *            markers
	 */
	public MarkFieldTerminal(EvaluationEnvironment env, boolean addMarker) {
		super("mark", "m", env);
		this.addMarker = addMarker;
	}

	@Override
	public Boolean apply() {
		env.setMarkerOnCurrentPosition(addMarker);
		return true;
	}

}
