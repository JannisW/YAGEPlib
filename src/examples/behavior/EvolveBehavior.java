package examples.behavior;

import java.util.ArrayList;

import examples.behavior.functions.InversionFunction;
import examples.behavior.functions.SelectorFunction;
import examples.behavior.functions.SequenceFunction;
import examples.behavior.terminals.EmptyInFrontCheckTerminal;
import examples.behavior.terminals.FoodInFrontCheckTerminal;
import examples.behavior.terminals.StepTerminal;
import examples.behavior.terminals.TurnLeftTerminal;
import examples.behavior.terminals.TurnRightTerminal;
import examples.behavior.terminals.WallInFrontCheckTerminal;
import gep.GeneExpressionProgramming;
import gep.ReproductionEnvironment;
import gep.model.ChromosomalArchitecture;
import gep.model.GeneArchitecture;
import gep.model.GeneFunction;
import gep.model.GeneTerminal;
import gep.model.Individual;
import gep.model.IndividualArchitecture;
import gep.operators.Mutation;
import gep.random.DefaultRandomEngine;
import gep.selection.RouletteWheelSelectionWithElitePreservation;
import gep.selection.SelectionMethod;


public class EvolveBehavior {

	public static void main(String[] args) {
		
		// TODO actually build multiple scenarios and allow the terminals handling more then just one at a time
		EvaluationEnvironment env = new EvaluationEnvironment(10, 10);

		ArrayList<GeneFunction<Boolean>> supportedBehaviorTreeNodes = new ArrayList<GeneFunction<Boolean>>(3);
		supportedBehaviorTreeNodes.add(new SelectorFunction());
		supportedBehaviorTreeNodes.add(new SequenceFunction());
		supportedBehaviorTreeNodes.add(new InversionFunction());
		// TODO support more nodes (including random)

		ArrayList<GeneTerminal<Boolean>> potentialTerminals = new ArrayList<GeneTerminal<Boolean>>(6);
		potentialTerminals.add(new StepTerminal(env));
		potentialTerminals.add(new TurnLeftTerminal(env));
		potentialTerminals.add(new TurnRightTerminal(env));
		potentialTerminals.add(new WallInFrontCheckTerminal(env));
		potentialTerminals.add(new FoodInFrontCheckTerminal(env));
		potentialTerminals.add(new EmptyInFrontCheckTerminal(env));
		
		// TODO maybe create meta structure which allows more readable
		// specification of terminals + function (which might get translated to
		// a more efficient representation e.g. index to array of meta objects)
		GeneArchitecture<Boolean> basicArch = new GeneArchitecture<Boolean>(16, supportedBehaviorTreeNodes, potentialTerminals);

		ChromosomalArchitecture chromosomalArch = new ChromosomalArchitecture();
		chromosomalArch.addBasicGene(basicArch);

		Individual[] population = IndividualArchitecture.createSingleChromosomalArchitecture(chromosomalArch)
				.createRandomPopulation(10, new DefaultRandomEngine());

		ReproductionEnvironment re = new ReproductionEnvironment();
		re.addGeneticOperator(new Mutation(0.1));
		
		SelectionMethod sm = new RouletteWheelSelectionWithElitePreservation();
		
		GeneExpressionProgramming.run(population, env, sm, re, 10, Double.MAX_VALUE);
	}

}
