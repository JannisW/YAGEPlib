package examples.behavior;

import java.util.ArrayList;

import examples.behavior.functions.InversionFunction;
import examples.behavior.functions.SelectorFunction;
import examples.behavior.functions.SequenceFunction;
import examples.behavior.terminals.TurnLeftTerminal;
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

		ArrayList<GeneFunction<Boolean>> supportedBehaviorTreeNodes = new ArrayList<GeneFunction<Boolean>>(3);
		supportedBehaviorTreeNodes.add(new SelectorFunction());
		supportedBehaviorTreeNodes.add(new SequenceFunction());
		supportedBehaviorTreeNodes.add(new InversionFunction());
		// TODO support more nodes (including random)
		// char[] potentialTerminals = { 'u' /* up */, 'r' /* right */, 'd' /*
		// down */, 'l' /* left */,
		// 'w' /* wall? */, };
		ArrayList<GeneTerminal<Boolean>> potentialTerminals = new ArrayList<GeneTerminal<Boolean>>(6);
		potentialTerminals.add(new StepTerminal(env));
		potentialTerminals.add(new TurnLeftTerminal(env));
		potentialTerminals.add(new TurnRightTerminal(env));
		potentialTerminals.add(new WallInFrontCheckTerminal(env));
		potentialTerminals.add(new FoodInFrontCheckTerminal(env));
		potentialTerminals.add(new EmptyInFrontCheckTerminal(env));
		
			{ new GeneTerminal("step", "s"), new GeneTerminal("turn right", "r"),
				new TurnLeftTerminal(env), new GeneTerminal("wall in front?", "w?"),
				new GeneTerminal("food in front?", "f?"), new GeneTerminal("empty in front?", "e") };

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
		
		GeneExpressionProgramming.run(population, fe, sm, re, 10, Double.MAX_VALUE);
	}

}
