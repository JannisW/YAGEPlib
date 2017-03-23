package examples.behavior;

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
import gep.ReproductionEnvironment;
import gep.model.ChromosomalArchitecture;
import gep.model.Chromosome;
import gep.model.Gene;
import gep.model.GeneArchitecture;
import gep.model.GeneElement;
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

public class EvolveBehavior {

	public static final int NUM_INDIVIDUALS = 50;
	public static final int MAX_NUM_GENERATIONS = 100;
	public static boolean USE_CLASSIC_FITNESS_FUNCTION = true;

	public static final BehaviorFitnessFunction FITNESSFUNCTION_PER_MAP = USE_CLASSIC_FITNESS_FUNCTION
			? new ClassicFitnessFunction() : new AlternativeFitnessFunction();

	public static void main(String[] args) {
		//startWithSolution();
		
		startGeneConfiguration1();
	}
	
	// TODO maybe move to a different class with a new main function
	private static void startGeneConfiguration1() {

		ArrayList<WorldMap> maps = new ArrayList<WorldMap>();
		System.out.print("Create maps...");
		try {
			// TODO actually build multiple scenarios and add them
			maps.add(new WorldMap(Paths.get("src/examples/behavior/maps/lecturemap.txt")));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
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

		// TODO maybe create meta structure which allows more readable
		// specification of terminals + function (which might get translated to
		// a more efficient representation e.g. index to array of meta objects)
		GeneArchitecture<Boolean> basicArch = new GeneArchitecture<Boolean>(16, supportedBehaviorTreeNodes,
				potentialTerminals);

		Gene<Boolean> basicGene = basicArch.createRandomGene();

		ChromosomalArchitecture<Boolean> chromosomeFactory = new ChromosomalArchitecture<>();
		int basicGeneId = chromosomeFactory.addGene(basicGene);

		ArrayList<HomoeoticGeneElement<Boolean>> homoeoticGeneTerminals = new ArrayList<>();
		homoeoticGeneTerminals.add(new HomoeoticGeneElement<>("link", "ll", basicGeneId));

		GeneArchitecture<Boolean> homoeoticArch = new GeneArchitecture<Boolean>(1, supportedBehaviorTreeNodes,
				homoeoticGeneTerminals);
		Gene<Boolean> homoeoticGene = homoeoticArch.createRandomGene();

		chromosomeFactory.setChromosomeRootToGene(homoeoticGene);

		Individual<Boolean>[] population = IndividualArchitecture.createSingleChromosomalArchitecture(chromosomeFactory)
				.createRandomPopulation(NUM_INDIVIDUALS, new DefaultRandomEngine());

		ReproductionEnvironment re = new ReproductionEnvironment();
		re.addGeneticOperator(new Mutation(0.2));
		re.addGeneticOperator(new Inversion(0.1));
		re.addGeneticOperator(new GeneRecombination(0.3));
		re.addGeneticOperator(new OnePointRecombination(0.5)); // 0.8
		re.addGeneticOperator(new TwoPointRecombination(0.5));

		SelectionMethod sm = new RouletteWheelSelectionWithElitePreservation(0.05);

		GeneExpressionProgramming.run(population, env, sm, re, MAX_NUM_GENERATIONS, Double.MAX_VALUE);
	}
	
	private static void startGeneConfiguration2() {

		ArrayList<WorldMap> maps = new ArrayList<WorldMap>();
		System.out.print("Create maps...");
		try {
			// TODO actually build multiple scenarios and add them
			maps.add(new WorldMap(Paths.get("src/examples/behavior/maps/lecturemap.txt")));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
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

		// TODO maybe create meta structure which allows more readable
		// specification of terminals + function (which might get translated to
		// a more efficient representation e.g. index to array of meta objects)
		GeneArchitecture<Boolean> basicArch = new GeneArchitecture<Boolean>(16, supportedBehaviorTreeNodes,
				potentialTerminals);

		Gene<Boolean> basicGene = basicArch.createRandomGene();

		ChromosomalArchitecture<Boolean> chromosomeFactory = new ChromosomalArchitecture<>();
		int basicGeneId = chromosomeFactory.addGene(basicGene);

		ArrayList<HomoeoticGeneElement<Boolean>> homoeoticGeneTerminals = new ArrayList<>();
		homoeoticGeneTerminals.add(new HomoeoticGeneElement<>("link", "ll", basicGeneId));

		GeneArchitecture<Boolean> homoeoticArch = new GeneArchitecture<Boolean>(1, supportedBehaviorTreeNodes,
				homoeoticGeneTerminals);
		Gene<Boolean> homoeoticGene = homoeoticArch.createRandomGene();

		chromosomeFactory.setChromosomeRootToGene(homoeoticGene);

		Individual<Boolean>[] population = IndividualArchitecture.createSingleChromosomalArchitecture(chromosomeFactory)
				.createRandomPopulation(NUM_INDIVIDUALS, new DefaultRandomEngine());

		ReproductionEnvironment re = new ReproductionEnvironment();
		re.addGeneticOperator(new Mutation(0.2));
		re.addGeneticOperator(new Inversion(0.1));
		re.addGeneticOperator(new GeneRecombination(0.3));
		re.addGeneticOperator(new OnePointRecombination(0.5)); // 0.8
		re.addGeneticOperator(new TwoPointRecombination(0.5));

		SelectionMethod sm = new RouletteWheelSelectionWithElitePreservation(0.05);

		GeneExpressionProgramming.run(population, env, sm, re, MAX_NUM_GENERATIONS, Double.MAX_VALUE);
	}

	private static void startWithSolution() {

		ArrayList<WorldMap> maps = new ArrayList<WorldMap>();
		System.out.print("Create maps...");
		try {
			// TODO actually build multiple scenarios and add them
			maps.add(new WorldMap(Paths.get("src/examples/behavior/maps/lecturemap.txt")));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		System.out.println("done (" + maps.size() + " map(s) created)");
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
		stepGeneSeq.add(sequenceFunc);	// sequence node
		stepGeneSeq.add(selectorFunc);	// selector node
		stepGeneSeq.add(stepTerm); 		// step node
		stepGeneSeq.add(foodTestTerm);  // food? node
		stepGeneSeq.add(pheroTestTerm);	// phero? node
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
		
		Chromosome<Boolean> cOpt = cArch.createReplica();

		// create optimal solution
		Individual<Boolean> indiOpt = new Individual<>(1);
		indiOpt.chromosomes[0] = cOpt;

		// create some random individuals
		Individual<Boolean> indi1 = new Individual<>(1);
		indi1.chromosomes[0] = cArch.create();
		
		Individual<Boolean> indi2 = new Individual<>(1);
		indi2.chromosomes[0] = cArch.create();
		
		Individual<Boolean> indi3 = new Individual<>(1);
		indi3.chromosomes[0] = cArch.create();

		@SuppressWarnings("unchecked")
		Individual<Boolean>[] population = new Individual[] {indiOpt, indi1, indi2, indi3};
		
		
		ReproductionEnvironment re = new ReproductionEnvironment();
		re.addGeneticOperator(new Mutation(0.2));
		re.addGeneticOperator(new Inversion(0.1));
		re.addGeneticOperator(new GeneRecombination(0.3));
		re.addGeneticOperator(new OnePointRecombination(0.5)); // 0.8
		re.addGeneticOperator(new TwoPointRecombination(0.5));

		SelectionMethod sm = new RouletteWheelSelectionWithElitePreservation(0.05);
		
		GeneExpressionProgramming.run(population, env, sm, re, 1,
						44.0 /* Best value for classic map */);

		//GeneExpressionProgramming.run(population, env, sm, re, MAX_NUM_GENERATIONS,
		//		44.0 /* Best value for classic map */);
	}

}
