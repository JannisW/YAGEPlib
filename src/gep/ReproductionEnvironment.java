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
package gep;

import java.util.ArrayList;

import gep.model.Individual;
import gep.operators.GeneticOperator;

public class ReproductionEnvironment {

	private ArrayList<GeneticOperator> operators;
	
	public ReproductionEnvironment() {
		operators = new ArrayList<GeneticOperator>();
	}

	/**
	 * Adds the given genetic operator to the reproduction environment. This
	 * means the genetic operator will be applied during the reproduction phase
	 * of GEP. The order of the operators is determined by calls to this
	 * function. This means that the order of the operators is the same as the
	 * order of calls to this function.
	 * 
	 * @param op
	 *            The genetic operator
	 */
	public void addGeneticOperator(GeneticOperator op) {
		operators.add(op);
	}

	public <T> void reproduce(Individual<T>[] individuals, int fromIdx) {
		for (GeneticOperator geneticOperator : operators) {
			geneticOperator.apply(individuals, fromIdx);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Operator\t\tRate" + System.lineSeparator());
		for (GeneticOperator geneticOperator : operators) {
			sb.append(geneticOperator.getName());
			sb.append("\t\t");
			sb.append(geneticOperator.getApplicationRate());
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

}
