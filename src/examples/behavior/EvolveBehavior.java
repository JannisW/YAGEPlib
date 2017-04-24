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
import gep.model.Chromosome;
import gep.model.GeneArchitecture;
import gep.model.GeneElement;
import gep.model.GeneFunction;
import gep.model.GeneTerminal;
import gep.model.HomoeoticGeneElement;
import gep.model.Individual;
import gep.model.IndividualArchitecture;
import gep.operators.GeneRecombination;
import gep.operators.GeneTransposition;
import gep.operators.Inversion;
import gep.operators.Mutation;
import gep.operators.OnePointRecombination;
import gep.operators.RegularTransposition;
import gep.operators.RootTransposition;
import gep.operators.TwoPointRecombination;
import gep.random.DefaultRandomEngine;
import gep.selection.RouletteWheelSelectionWithElitePreservation;
import gep.selection.SelectionMethod;

/**
 * 
 * @author Johannes Wortmann
 *
 */
public class EvolveBehavior {

	public static final int NUM_INDIVIDUALS = 50;
	public static final int MAX_NUM_GENERATIONS = 100;
	public static boolean USE_CLASSIC_FITNESS_FUNCTION = true;

	public static final int[] START_CONFIGURATIONS = {3};
	
	// start total chromosome heads length (inclusive)
	public static final int MIN_CHROMOSOME_HEAD_LENGTH = 17;
	// end total chromosome heads length (inclusive)
	public static final int MAX_CHROMOSOME_HEAD_LENGTH = 17;
	// the total length of the chromosome heads (sum of all gene head lengths)
	public static int chromosomeHeadLength = 12; // 16

	public static final int NUM_ITERATRIONS_FOR_BENCHMARK = 1000;

	public static final BehaviorFitnessFunction FITNESSFUNCTION_PER_MAP = USE_CLASSIC_FITNESS_FUNCTION
			? new ClassicFitnessFunction() : new AlternativeFitnessFunction();

	// the best possible value of the fitness function (for the classic ant
	// tracker map)
	public static final double MAX_FITNESS_VALUE = USE_CLASSIC_FITNESS_FUNCTION ? 44.0 : Double.MAX_VALUE;

	public static void main(String[] args) {

		double bestFitness = -1.0;
		
		for (int startConfig : START_CONFIGURATIONS) {
			
			for (chromosomeHeadLength = MIN_CHROMOSOME_HEAD_LENGTH; chromosomeHeadLength <= MAX_CHROMOSOME_HEAD_LENGTH; chromosomeHeadLength++) {
	
				String suffix = "";
				if (startConfig == 0) {
					suffix = "_solution";
				} else if (startConfig == 1) {
					suffix = "_singleChrom";
				} else if (startConfig == 2) {
					suffix = "_twoGeneChromSpecNormal";
				} else if (startConfig == 3) {
					suffix = "_twoGeneChromSpec";
				} else {
					throw new IllegalArgumentException(startConfig + " is not a valid start configuration");
				}
	
				try (BufferedWriter bw = new BufferedWriter(new FileWriter(
						Paths.get("benchmarks", "lecturemap", "benchresult" + chromosomeHeadLength + suffix + ".tsv")
								.toFile()))) {
	
					bw.write("num_gen\tmax_gen\tbest_fitness\tlen_chromosome");
					bw.newLine();
					
					long totalTime = 0;
	
					for (int i = 0; i < NUM_ITERATRIONS_FOR_BENCHMARK; i++) {
	
						long startTime = System.currentTimeMillis();
						
						GepResult<Boolean> r = null;
						if (startConfig == 0) {
							r = startWithSolution();
						} else if (startConfig == 1) {
							r = startGeneConfiguration1();
						} else if (startConfig == 2) {
							r = startGeneConfiguration2();
						} else if (startConfig == 3) {
							r = startGeneConfiguration3();
						} else {
							throw new IllegalArgumentException(startConfig + " is not a valid start configuration");
						}
						
						totalTime += System.currentTimeMillis() - startTime;
	
						bw.write(r.numGenerations + "\t" + r.maxGenrations + "\t" + r.getFitnessOfBestIndivudal() + "\t"
								+ chromosomeHeadLength);
						bw.newLine();
	
						if (r.getFitnessOfBestIndivudal() > bestFitness) {
							r.bestIndividual.writeToFile(Paths.get("./bestIndividual.ser"));
							bestFitness = r.getFitnessOfBestIndivudal();
						}
	
					}
					
					System.out.println("avg Time per run = " + totalTime / NUM_ITERATRIONS_FOR_BENCHMARK + "ms");
	
				} catch (IOException e) {
					// Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Start Configuration 1: One chromosome containing one gene which encodes
	 * the complete program
	 * 
	 * @return
	 */
	private static GepResult<Boolean> startGeneConfiguration1() {

		ArrayList<WorldMap> maps = createMaps();
		EvaluationEnvironment env = new EvaluationEnvironment(maps, FITNESSFUNCTION_PER_MAP);

		ArrayList<GeneFunction<Boolean>> supportedBehaviorTreeNodes = new ArrayList<GeneFunction<Boolean>>(3);
		supportedBehaviorTreeNodes.add(new SelectorFunction());
		supportedBehaviorTreeNodes.add(new SequenceFunction());
		supportedBehaviorTreeNodes.add(new InversionFunction());

		ArrayList<GeneTerminal<Boolean>> potentialTerminals = new ArrayList<GeneTerminal<Boolean>>(7);
		potentialTerminals.add(new StepTerminal(env));
		potentialTerminals.add(new TurnLeftTerminal(env));
		potentialTerminals.add(new TurnRightTerminal(env));
		potentialTerminals.add(new WallInFrontCheckTerminal(env));
		potentialTerminals.add(new FoodInFrontCheckTerminal(env));
		potentialTerminals.add(new EmptyInFrontCheckTerminal(env));
		potentialTerminals.add(new PheroInFrontCheckTerminal(env));

		ChromosomalArchitecture<Boolean> chromosomeFactory = new ChromosomalArchitecture<>();
		int basicGeneId = chromosomeFactory.addGene(
				new GeneArchitecture<Boolean>(chromosomeHeadLength, supportedBehaviorTreeNodes, potentialTerminals));
		chromosomeFactory.setChromosomeRootToGene(basicGeneId);

		Individual<Boolean>[] population = IndividualArchitecture.createSingleChromosomalArchitecture(chromosomeFactory)
				.createRandomPopulation(NUM_INDIVIDUALS, new DefaultRandomEngine());

		ReproductionEnvironment re = new ReproductionEnvironment();
		re.addGeneticOperator(new Mutation(0.2));
		re.addGeneticOperator(new Inversion(0.1));
		re.addGeneticOperator(new GeneRecombination(0.3));
		re.addGeneticOperator(new OnePointRecombination(0.5)); // 0.8
		re.addGeneticOperator(new TwoPointRecombination(0.5));

		re.addGeneticOperator(new GeneTransposition(0.3));
		re.addGeneticOperator(new RootTransposition(0.1));
		re.addGeneticOperator(new RegularTransposition(0.3));

		SelectionMethod sm = new RouletteWheelSelectionWithElitePreservation(0.05);

		return GeneExpressionProgramming.run(population, env, sm, re, MAX_NUM_GENERATIONS, MAX_FITNESS_VALUE);
	}

	/**
	 * Configuration 2: One chromosome containing two genes (one homoeotic)
	 * where all genes contain the same terminals (the homoeotic includes the
	 * homoetic one in addition)
	 * 
	 * @return
	 */
	private static GepResult<Boolean> startGeneConfiguration2() {

		ArrayList<WorldMap> maps = createMaps();
		EvaluationEnvironment env = new EvaluationEnvironment(maps, FITNESSFUNCTION_PER_MAP);

		ArrayList<GeneFunction<Boolean>> supportedBehaviorTreeNodes = new ArrayList<GeneFunction<Boolean>>(3);
		supportedBehaviorTreeNodes.add(new SelectorFunction());
		supportedBehaviorTreeNodes.add(new SequenceFunction());
		supportedBehaviorTreeNodes.add(new InversionFunction());

		ArrayList<GeneTerminal<Boolean>> potentialTerminals = new ArrayList<GeneTerminal<Boolean>>(7);
		potentialTerminals.add(new StepTerminal(env));
		potentialTerminals.add(new WallInFrontCheckTerminal(env));
		potentialTerminals.add(new FoodInFrontCheckTerminal(env));
		potentialTerminals.add(new EmptyInFrontCheckTerminal(env));
		potentialTerminals.add(new PheroInFrontCheckTerminal(env));
		potentialTerminals.add(new TurnLeftTerminal(env));
		potentialTerminals.add(new TurnRightTerminal(env));

		ChromosomalArchitecture<Boolean> chromosomeFactory = new ChromosomalArchitecture<>();
		int basicGeneId = chromosomeFactory.addGene(new GeneArchitecture<Boolean>(
				Math.floorDiv(chromosomeHeadLength, 3), supportedBehaviorTreeNodes, potentialTerminals));

		// create homoeotic gene
		ArrayList<GeneTerminal<Boolean>> potentialHomoeoticGeneTerminals = new ArrayList<GeneTerminal<Boolean>>(
				potentialTerminals);
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

		return GeneExpressionProgramming.run(population, env, sm, re, MAX_NUM_GENERATIONS, MAX_FITNESS_VALUE);
	}

	/**
	 * Configuration 3: One chromosome containing two genes (one homoeotic)
	 * where the regular gene does not contain the turn terminals and the
	 * homoeotic gene does not contain the step terminal and the guards. The
	 * main idea is that one gene codes for the stepping and the other what to
	 * do if no stepping should be done and the step triggering.
	 * 
	 * @return
	 */
	private static GepResult<Boolean> startGeneConfiguration3() {

		ArrayList<WorldMap> maps = createMaps();
		EvaluationEnvironment env = new EvaluationEnvironment(maps, FITNESSFUNCTION_PER_MAP);

		ArrayList<GeneFunction<Boolean>> supportedBehaviorTreeNodes = new ArrayList<GeneFunction<Boolean>>(3);
		supportedBehaviorTreeNodes.add(new SelectorFunction());
		supportedBehaviorTreeNodes.add(new SequenceFunction());
		supportedBehaviorTreeNodes.add(new InversionFunction());

		// create step controlling gene
		ArrayList<GeneTerminal<Boolean>> potentialStepTerminals = new ArrayList<GeneTerminal<Boolean>>(7);
		potentialStepTerminals.add(new StepTerminal(env));
		potentialStepTerminals.add(new WallInFrontCheckTerminal(env));
		potentialStepTerminals.add(new FoodInFrontCheckTerminal(env));
		potentialStepTerminals.add(new EmptyInFrontCheckTerminal(env));
		potentialStepTerminals.add(new PheroInFrontCheckTerminal(env));

		// good value for head length was 6
		ChromosomalArchitecture<Boolean> chromosomeFactory = new ChromosomalArchitecture<>();
		int basicGeneId = chromosomeFactory.addGene(new GeneArchitecture<Boolean>(
				Math.floorDiv(chromosomeHeadLength, 3), supportedBehaviorTreeNodes, potentialStepTerminals));

		// create homoeotic turn-controlling gene
		ArrayList<GeneTerminal<Boolean>> potentialHomoeoticGeneTerminals = new ArrayList<GeneTerminal<Boolean>>(7);
		potentialHomoeoticGeneTerminals.add(new TurnLeftTerminal(env));
		potentialHomoeoticGeneTerminals.add(new TurnRightTerminal(env));
		potentialHomoeoticGeneTerminals.add(new HomoeoticGeneElement<>(basicGeneId));

		// good value for head length was 12
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

		/*
		 * Transposition in the current version is not working here as it may
		 * corrupt the chromosome structure (two different genes pair up etc.)
		 * and may lead to infinite loops (self referring homoeotic genes) TODO
		 * improve
		 */
		// re.addGeneticOperator(new GeneTransposition(0.3));
		// re.addGeneticOperator(new RootTransposition(0.1));
		// re.addGeneticOperator(new RegularTransposition(0.3));

		SelectionMethod sm = new RouletteWheelSelectionWithElitePreservation(0.05);

		return GeneExpressionProgramming.run(population, env, sm, re, MAX_NUM_GENERATIONS, MAX_FITNESS_VALUE);
	}

	private static GepResult<Boolean> startWithSolution() {

		ArrayList<WorldMap> maps = createMaps();
		EvaluationEnvironment env = new EvaluationEnvironment(maps, FITNESSFUNCTION_PER_MAP);

		// functions
		final GeneFunction<Boolean> selectorFunc = new SelectorFunction();
		final GeneFunction<Boolean> sequenceFunc = new SequenceFunction();

		// terminals
		final GeneTerminal<Boolean> stepTerm = new StepTerminal(env);
		final GeneTerminal<Boolean> turnLeftTerm = new TurnLeftTerminal(env);
		final GeneTerminal<Boolean> turnRightTerm = new TurnRightTerminal(env);
		final GeneTerminal<Boolean> foodTestTerm = new FoodInFrontCheckTerminal(env);
		final GeneTerminal<Boolean> pheroTestTerm = new PheroInFrontCheckTerminal(env);

		ChromosomalArchitecture<Boolean> cArch = new ChromosomalArchitecture<>();

		// add basic gene (defining step regulation)
		ArrayList<GeneElement<Boolean>> stepGeneSeq = new ArrayList<>();
		stepGeneSeq.add(sequenceFunc); // sequence node
		stepGeneSeq.add(selectorFunc); // selector node
		stepGeneSeq.add(stepTerm); // step node
		stepGeneSeq.add(foodTestTerm); // food? node
		stepGeneSeq.add(pheroTestTerm); // phero? node
		// the remaining sequence will be padded automatically
		int stepGeneId = cArch.addGene(stepGeneSeq);

		// add homoeotic gene controlling when to turn and to trigger the step
		ArrayList<GeneElement<Boolean>> homoeoticGeneSeq = new ArrayList<>();
		final HomoeoticGeneElement<Boolean> homoeoticTerm = new HomoeoticGeneElement<>(stepGeneId);
		homoeoticGeneSeq.add(selectorFunc);
		homoeoticGeneSeq.add(homoeoticTerm);
		homoeoticGeneSeq.add(selectorFunc);
		homoeoticGeneSeq.add(sequenceFunc);
		homoeoticGeneSeq.add(sequenceFunc);
		homoeoticGeneSeq.add(turnLeftTerm);
		homoeoticGeneSeq.add(homoeoticTerm);
		homoeoticGeneSeq.add(turnRightTerm);
		homoeoticGeneSeq.add(sequenceFunc);
		homoeoticGeneSeq.add(turnRightTerm);
		homoeoticGeneSeq.add(homoeoticTerm);
		// the remaining sequence will be padded automatically
		int homoeoticGeneId = cArch.addGene(homoeoticGeneSeq);

		cArch.setChromosomeRootToGene(homoeoticGeneId);

		// create optimal solution
		Chromosome<Boolean> cOpt = cArch.createReplica();

		// create some random individuals
		Individual<Boolean>[] population = IndividualArchitecture.createSingleChromosomalArchitecture(cArch)
				.createRandomPopulation(4);

		// set one solution to the optimum
		population[0].chromosomes[0] = cOpt;

		ReproductionEnvironment re = new ReproductionEnvironment();
		re.addGeneticOperator(new Mutation(0.2));
		re.addGeneticOperator(new Inversion(0.1));
		re.addGeneticOperator(new GeneRecombination(0.3));
		re.addGeneticOperator(new OnePointRecombination(0.5)); // 0.8
		re.addGeneticOperator(new TwoPointRecombination(0.5));

		re.addGeneticOperator(new GeneTransposition(0.3));
		re.addGeneticOperator(new RootTransposition(0.1));
		re.addGeneticOperator(new RegularTransposition(0.3));

		SelectionMethod sm = new RouletteWheelSelectionWithElitePreservation(0.05);

		return GeneExpressionProgramming.run(population, env, sm, re, MAX_NUM_GENERATIONS, MAX_FITNESS_VALUE);
	}

	private static ArrayList<WorldMap> createMaps() {
		ArrayList<WorldMap> maps = new ArrayList<WorldMap>();
		System.out.print("Create maps...");
		try {
			maps.add(new WorldMap(Paths.get("src/examples/behavior/maps/lecturemap.txt")));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		System.out.println("done (" + maps.size() + " map(s) created)");
		return maps;
	}

}
