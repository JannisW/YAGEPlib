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
package examples.behavior;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import examples.behavior.fitness.AlternativeFitnessFunction;
import examples.behavior.fitness.BehaviorFitnessFunction;
import examples.behavior.fitness.ClassicFitnessFunction;
import examples.behavior.fitness.EvaluationEnvironment;
import examples.behavior.functions.InversionFunction;
import examples.behavior.functions.SelectorFunction;
import examples.behavior.functions.SequenceFunction;
import examples.behavior.terminals.EmptyInFrontCheckTerminal;
import examples.behavior.terminals.FoodInFrontCheckTerminal;
import examples.behavior.terminals.MarkFieldTerminal;
import examples.behavior.terminals.MarkerInFrontCheckTerminal;
import examples.behavior.terminals.PheroInFrontCheckTerminal;
import examples.behavior.terminals.StepTerminal;
import examples.behavior.terminals.TurnLeftTerminal;
import examples.behavior.terminals.TurnRightTerminal;
import examples.behavior.terminals.WallInFrontCheckTerminal;
import examples.behavior.world.WorldMap;
import gep.GeneExpressionProgramming;
import gep.GepResult;
import gep.ReproductionEnvironment;
import gep.model.ChromosomalArchitecture;
import gep.model.GeneArchitecture;
import gep.model.GeneFunction;
import gep.model.GeneTerminal;
import gep.model.HomoeoticGeneElement;
import gep.model.Individual;
import gep.model.IndividualArchitecture;
import gep.operators.GeneRecombination;
import gep.operators.Inversion;
import gep.operators.Mutation;
import gep.operators.OnePointRecombination;
import gep.operators.TwoPointRecombination;
import gep.random.DefaultRandomEngine;
import gep.selection.RouletteWheelSelectionWithElitePreservation;
import gep.selection.SelectionMethod;

public class EvolveBehaviorExtended {

	public static final int NUM_INDIVIDUALS = 50;
	public static final int MAX_NUM_GENERATIONS = 100;
	public static boolean USE_CLASSIC_FITNESS_FUNCTION = true;

	public static final int START_CONFIGURATION = 1;

	// start total chromosome heads length (inclusive)
	public static final int MIN_CHROMOSOME_HEAD_LENGTH = 3;
	// end total chromosome heads length (inclusive)
	public static final int MAX_CHROMOSOME_HEAD_LENGTH = 20;
	// the total length of the chromosome heads (sum of all gene head lengths)
	public static int chromosomeHeadLength = 12;

	public static final int NUM_ITERATRIONS_FOR_BENCHMARK = 1000;
	
	public static final int NUM_FOOD = 135;
	
	public static final String SUB_DIR = "all_maps";

	public static final BehaviorFitnessFunction FITNESSFUNCTION_PER_MAP = USE_CLASSIC_FITNESS_FUNCTION
			? new ClassicFitnessFunction() : new AlternativeFitnessFunction();

	public static void main(String[] args) {

		for (chromosomeHeadLength = MIN_CHROMOSOME_HEAD_LENGTH; chromosomeHeadLength <= MAX_CHROMOSOME_HEAD_LENGTH; chromosomeHeadLength++) {

			String suffix = "";
			if (START_CONFIGURATION == 0) {
				suffix = "_solution";
			} else if (START_CONFIGURATION == 1) {
				suffix = "_singleChrom";
			} else if (START_CONFIGURATION == 2) {
				suffix = "_twoGeneChromSpecNormal";
			} else if (START_CONFIGURATION == 3) {
				suffix = "_twoGeneChromSpec";
			}

			try (BufferedWriter bw = new BufferedWriter(new FileWriter(
					Paths.get("benchmarks", SUB_DIR, "benchresult" + chromosomeHeadLength + suffix + ".tsv").toFile()))) {

				bw.write("num_gen\tmax_gen\tbest_fitness\tlen_chromosome");
				bw.newLine();

				for (int i = 0; i < NUM_ITERATRIONS_FOR_BENCHMARK; i++) {

					GepResult<Boolean> r = null;
					if (START_CONFIGURATION == 1) {
						r = startGeneConfiguration1();
					} else if (START_CONFIGURATION == 2) {
						r = startGeneConfiguration2();
					} else if (START_CONFIGURATION == 3) {
						r = startGeneConfiguration3();
					}

					bw.write(r.numGenerations + "\t" + r.maxGenrations + "\t" + r.getFitnessOfBestIndivudal() + "\t"
							+ chromosomeHeadLength);
					bw.newLine();

				}

			} catch (IOException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// TODO maybe move to a different class with a new main function
	private static GepResult<Boolean> startGeneConfiguration1() {

		ArrayList<WorldMap> maps = createMaps();
		System.out.println("done (" + maps.size() + " map(s) created)");
		EvaluationEnvironment env = new EvaluationEnvironment(maps, FITNESSFUNCTION_PER_MAP);

		ArrayList<GeneFunction<Boolean>> supportedBehaviorTreeNodes = new ArrayList<GeneFunction<Boolean>>(3);
		supportedBehaviorTreeNodes.add(new SelectorFunction());
		supportedBehaviorTreeNodes.add(new SequenceFunction());
		supportedBehaviorTreeNodes.add(new InversionFunction());
		// TODO support more nodes (including random)

		ArrayList<GeneTerminal<Boolean>> potentialTerminals = new ArrayList<GeneTerminal<Boolean>>(7);
		potentialTerminals.add(new StepTerminal(env));
		potentialTerminals.add(new TurnLeftTerminal(env));
		potentialTerminals.add(new TurnRightTerminal(env));
		potentialTerminals.add(new WallInFrontCheckTerminal(env));
		potentialTerminals.add(new FoodInFrontCheckTerminal(env));
		potentialTerminals.add(new EmptyInFrontCheckTerminal(env));
		potentialTerminals.add(new PheroInFrontCheckTerminal(env));

		ChromosomalArchitecture<Boolean> chromosomeFactory = new ChromosomalArchitecture<>();
		int basicGeneId = chromosomeFactory
				.addGene(new GeneArchitecture<Boolean>(chromosomeHeadLength, supportedBehaviorTreeNodes, potentialTerminals));
		chromosomeFactory.setChromosomeRootToGene(basicGeneId);

		Individual<Boolean>[] population = IndividualArchitecture.createSingleChromosomalArchitecture(chromosomeFactory)
				.createRandomPopulation(NUM_INDIVIDUALS, new DefaultRandomEngine());

		ReproductionEnvironment re = new ReproductionEnvironment();
		re.addGeneticOperator(new Mutation(0.2));
		re.addGeneticOperator(new Inversion(0.1));
		re.addGeneticOperator(new GeneRecombination(0.3));
		re.addGeneticOperator(new OnePointRecombination(0.5)); // 0.8
		re.addGeneticOperator(new TwoPointRecombination(0.5));

		SelectionMethod sm = new RouletteWheelSelectionWithElitePreservation(0.05);

		return GeneExpressionProgramming.run(population, env, sm, re, MAX_NUM_GENERATIONS, NUM_FOOD);
		// GeneExpressionProgramming.run(population, env, sm, re,
		// MAX_NUM_GENERATIONS, Double.MAX_VALUE);
	}
	
	private static GepResult<Boolean> startGeneConfiguration2() {

		ArrayList<WorldMap> maps = createMaps();
		EvaluationEnvironment env = new EvaluationEnvironment(maps, FITNESSFUNCTION_PER_MAP);

		ArrayList<GeneFunction<Boolean>> supportedBehaviorTreeNodes = new ArrayList<GeneFunction<Boolean>>(3);
		supportedBehaviorTreeNodes.add(new SelectorFunction());
		supportedBehaviorTreeNodes.add(new SequenceFunction());
		supportedBehaviorTreeNodes.add(new InversionFunction());
		// TODO support more nodes (including random)

		// create step controlling gene
		ArrayList<GeneTerminal<Boolean>> potentialTerminals = new ArrayList<GeneTerminal<Boolean>>(7);
		potentialTerminals.add(new StepTerminal(env));
		potentialTerminals.add(new WallInFrontCheckTerminal(env));
		potentialTerminals.add(new FoodInFrontCheckTerminal(env));
		potentialTerminals.add(new EmptyInFrontCheckTerminal(env));
		potentialTerminals.add(new PheroInFrontCheckTerminal(env));
		potentialTerminals.add(new MarkerInFrontCheckTerminal(env));
		potentialTerminals.add(new TurnLeftTerminal(env));
		potentialTerminals.add(new TurnRightTerminal(env));
		potentialTerminals.add(new MarkFieldTerminal(env, true));
		potentialTerminals.add(new MarkFieldTerminal(env, false));

		ChromosomalArchitecture<Boolean> chromosomeFactory = new ChromosomalArchitecture<>();
		int basicGeneId = chromosomeFactory
				.addGene(new GeneArchitecture<Boolean>(Math.floorDiv(chromosomeHeadLength, 3), supportedBehaviorTreeNodes, potentialTerminals));

		// create homoeotic turn-controlling gene
		ArrayList<GeneTerminal<Boolean>> potentialHomoeoticGeneTerminals = new ArrayList<GeneTerminal<Boolean>>(potentialTerminals);
		potentialHomoeoticGeneTerminals.add(new HomoeoticGeneElement<>(basicGeneId));

		int homoeoticGeneId = chromosomeFactory
				.addGene(new GeneArchitecture<>((int) Math.ceil(chromosomeHeadLength * 2.0 / 3.0),
						supportedBehaviorTreeNodes, potentialHomoeoticGeneTerminals));
		chromosomeFactory.setChromosomeRootToGene(homoeoticGeneId);

		// create population
		Individual<Boolean>[] population = IndividualArchitecture.createSingleChromosomalArchitecture(chromosomeFactory)
				.createRandomPopulation(NUM_INDIVIDUALS, new DefaultRandomEngine());

		ReproductionEnvironment re = new ReproductionEnvironment();
		re.addGeneticOperator(new Mutation(0.2));
		re.addGeneticOperator(new Inversion(0.1));
		re.addGeneticOperator(new GeneRecombination(0.3));
		re.addGeneticOperator(new OnePointRecombination(0.5)); // 0.8
		re.addGeneticOperator(new TwoPointRecombination(0.5));

		SelectionMethod sm = new RouletteWheelSelectionWithElitePreservation(0.05);

		return GeneExpressionProgramming.run(population, env, sm, re, MAX_NUM_GENERATIONS, NUM_FOOD);
		// GeneExpressionProgramming.run(population, env, sm, re,
		// MAX_NUM_GENERATIONS, Double.MAX_VALUE);
	}

	private static GepResult<Boolean> startGeneConfiguration3() {

		ArrayList<WorldMap> maps = createMaps();
		EvaluationEnvironment env = new EvaluationEnvironment(maps, FITNESSFUNCTION_PER_MAP);

		ArrayList<GeneFunction<Boolean>> supportedBehaviorTreeNodes = new ArrayList<GeneFunction<Boolean>>(3);
		supportedBehaviorTreeNodes.add(new SelectorFunction());
		supportedBehaviorTreeNodes.add(new SequenceFunction());
		supportedBehaviorTreeNodes.add(new InversionFunction());
		// TODO support more nodes (including random)

		// create step controlling gene
		ArrayList<GeneTerminal<Boolean>> potentialStepTerminals = new ArrayList<GeneTerminal<Boolean>>(7);
		potentialStepTerminals.add(new StepTerminal(env));
		potentialStepTerminals.add(new WallInFrontCheckTerminal(env));
		potentialStepTerminals.add(new FoodInFrontCheckTerminal(env));
		potentialStepTerminals.add(new EmptyInFrontCheckTerminal(env));
		potentialStepTerminals.add(new PheroInFrontCheckTerminal(env));
		potentialStepTerminals.add(new MarkerInFrontCheckTerminal(env));

		ChromosomalArchitecture<Boolean> chromosomeFactory = new ChromosomalArchitecture<>();
		int basicGeneId = chromosomeFactory
				.addGene(new GeneArchitecture<Boolean>(Math.floorDiv(chromosomeHeadLength, 3), supportedBehaviorTreeNodes, potentialStepTerminals));

		// create homoeotic turn-controlling gene
		ArrayList<GeneTerminal<Boolean>> potentialHomoeoticGeneTerminals = new ArrayList<GeneTerminal<Boolean>>(7);
		potentialHomoeoticGeneTerminals.add(new TurnLeftTerminal(env));
		potentialHomoeoticGeneTerminals.add(new TurnRightTerminal(env));
		potentialHomoeoticGeneTerminals.add(new HomoeoticGeneElement<>(basicGeneId));
		potentialHomoeoticGeneTerminals.add(new MarkFieldTerminal(env, true));
		potentialHomoeoticGeneTerminals.add(new MarkFieldTerminal(env, false));

		int homoeoticGeneId = chromosomeFactory
				.addGene(new GeneArchitecture<>((int) Math.ceil(chromosomeHeadLength * 2.0 / 3.0),
						supportedBehaviorTreeNodes, potentialHomoeoticGeneTerminals));
		chromosomeFactory.setChromosomeRootToGene(homoeoticGeneId);

		// create population
		Individual<Boolean>[] population = IndividualArchitecture.createSingleChromosomalArchitecture(chromosomeFactory)
				.createRandomPopulation(NUM_INDIVIDUALS, new DefaultRandomEngine());

		ReproductionEnvironment re = new ReproductionEnvironment();
		re.addGeneticOperator(new Mutation(0.2));
		re.addGeneticOperator(new Inversion(0.1));
		re.addGeneticOperator(new GeneRecombination(0.3));
		re.addGeneticOperator(new OnePointRecombination(0.5)); // 0.8
		re.addGeneticOperator(new TwoPointRecombination(0.5));

		SelectionMethod sm = new RouletteWheelSelectionWithElitePreservation(0.05);

		return GeneExpressionProgramming.run(population, env, sm, re, MAX_NUM_GENERATIONS, NUM_FOOD);
		// GeneExpressionProgramming.run(population, env, sm, re,
		// MAX_NUM_GENERATIONS, Double.MAX_VALUE);
	}

	private static ArrayList<WorldMap> createMaps() {
		ArrayList<WorldMap> maps = new ArrayList<WorldMap>();
		System.out.print("Create maps...");
		try {
			maps.add(new WorldMap(Paths.get("src/examples/behavior/maps/lecturemap.txt")));
			maps.add(new WorldMap(Paths.get("src/examples/behavior/maps/branchmap.txt")));
			maps.add(new WorldMap(Paths.get("src/examples/behavior/maps/maze1.txt")));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		System.out.println("done (" + maps.size() + " map(s) created)");
		return maps;
	}

}
