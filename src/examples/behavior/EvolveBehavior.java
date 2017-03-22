package examples.behavior;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
		startWithOptimalSolution();
		
		//startGeneConfiguration1();
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

	private static void startWithOptimalSolution() {

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
		
		final int headLength = 4;
		final int maxArity = 2;

		GeneArchitecture<Boolean> arch = new GeneArchitecture<>(headLength, supportedBehaviorTreeNodes, potentialTerminals);
		
		Gene<Boolean> gene = new Gene<>(arch);
		gene.setSequenceAt(0, supportedBehaviorTreeNodes.get(0)); // selector node
		gene.setSequenceAt(1, supportedBehaviorTreeNodes.get(1)); // sequence node
		gene.setSequenceAt(2, potentialTerminals.get(1)); // turn left node
		gene.setSequenceAt(3, supportedBehaviorTreeNodes.get(0)); // selector node
		gene.setSequenceAt(4, potentialTerminals.get(0)); // step node
		gene.setSequenceAt(5, potentialTerminals.get(4)); // food? node
		gene.setSequenceAt(6, potentialTerminals.get(6)); // phero? node
		
		gene.setSequenceAt(7, potentialTerminals.get(3)); // wall? node as padding
		gene.setSequenceAt(8, potentialTerminals.get(2)); // turn right node as padding
		
		Chromosome<Boolean> cOpt = ChromosomalArchitecture.createSingleGenicChromosome(gene);
		
		// create optimal solution
		Individual<Boolean> indiOpt = new Individual<>(1);
		indiOpt.chromosomes[0] = cOpt;

		// create some random individuals
		ChromosomalArchitecture<Boolean> cArch = new ChromosomalArchitecture<>();
		cArch.setChromosomeRootToGene(gene);

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
