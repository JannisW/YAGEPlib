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
import java.util.List;

import gep.model.Individual;
import gep.operators.GeneticOperator;

/**
 * This class represents the reproduction environment of the GEP algorithm. It
 * includes all genetic operators to be applied in the current run. The order of
 * application is well defined by the order of adds to this environment. This
 * also means that operators can be added multiple times to allow their
 * application at different times (and after other operators).
 * 
 * @author Johannes Wortmann
 *
 */
public class ReproductionEnvironment {

	/**
	 * The list of genetic operators to be used in the reproduction phase.
	 */
	private ArrayList<GeneticOperator> operators;

	/**
	 * Creates a new instance of a reproduction environment with an empty list
	 * of operators.
	 */
	public ReproductionEnvironment() {
		operators = new ArrayList<GeneticOperator>();
	}

	/**
	 * Creates a new instance of a reproduction environment with a list of
	 * operators in the same order as returned by the iterator of the given
	 * list.
	 * 
	 * @param operators
	 *            The list whose operators are to be placed into this
	 *            environments list of used operators.
	 */
	public ReproductionEnvironment(List<GeneticOperator> operators) {
		operators = new ArrayList<GeneticOperator>(operators);
	}

	/**
	 * Adds the given genetic operator to the reproduction environment. This
	 * means the genetic operator will be applied during the reproduction phase
	 * of GEP. The order of the operators is determined by calls to this
	 * function. This means that the order of the operators is the same as the
	 * order of calls to this function.
	 * 
	 * @param op
	 *            The genetic operator to be added
	 */
	public void addGeneticOperator(GeneticOperator op) {
		operators.add(op);
	}

	/**
	 * This method applies all genetic operators to the population defined by
	 * the given array of individuals. The order of the operators is defined by
	 * the order of calls to
	 * {@link ReproductionEnvironment#addGeneticOperator(GeneticOperator)}. All
	 * individuals with a lower index then fromIdx will be excluded from genetic
	 * operations.
	 * 
	 * @param individuals
	 *            The population
	 * @param fromIdx
	 *            The index in the population array from which the operators are
	 *            applied.s
	 */
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
