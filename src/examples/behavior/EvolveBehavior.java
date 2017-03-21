package examples.behavior;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

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
import gep.model.ExpressionTreeNode;
import gep.model.Gene;
import gep.model.GeneArchitecture;
import gep.model.GeneFunction;
import gep.model.GeneTerminal;
import gep.model.HomoeoticGeneElement;
import gep.model.Individual;
import gep.model.IndividualArchitecture;
import gep.operators.Mutation;
import gep.random.DefaultRandomEngine;
import gep.selection.RouletteWheelSelectionWithElitePreservation;
import gep.selection.SelectionMethod;

public class EvolveBehavior {

	public static void main(String[] args) {

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
		EvaluationEnvironment env = new EvaluationEnvironment(maps);

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
		
		ArrayList<HomoeoticGeneElement<Boolean>> homoeoticTerminals = new ArrayList<>();
		homoeoticTerminals.add(new HomoeoticGeneElement<>("link", "ll", basicGeneId));
		
		GeneArchitecture<Boolean> homoeoticArch = new GeneArchitecture<Boolean>(1, supportedBehaviorTreeNodes,
				homoeoticTerminals);
		Gene<Boolean> homoeoticGene = homoeoticArch.createRandomGene();
		
		chromosomeFactory.setChromosomeRoot(homoeoticGene);

		Individual<Boolean>[] population = IndividualArchitecture.createSingleChromosomalArchitecture(chromosomeFactory)
				.createRandomPopulation(10, new DefaultRandomEngine());

		ReproductionEnvironment re = new ReproductionEnvironment();
		re.addGeneticOperator(new Mutation(0.1));

		SelectionMethod sm = new RouletteWheelSelectionWithElitePreservation();

		GeneExpressionProgramming.run(population, env, sm, re, 10, Double.MAX_VALUE);
	}

}
