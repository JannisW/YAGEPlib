package gep.operators;

import java.util.ArrayList;

import gep.model.Chromosome;
import gep.model.Gene;
import gep.model.GeneElement;
import gep.model.GeneFunction;
import gep.random.RandomEngine;

public class RootTransposition extends GeneticOperator {

	public RootTransposition(double applicationRate) {
		super(applicationRate);
	}

	public RootTransposition(double applicationRate, RandomEngine re) {
		super(applicationRate, re);
	}

	@Override
	protected <T> void apply(Chromosome<T> c) {
		if (random.decideBinaryDecision(super.applicationRate)) {
			// apply operator
			int geneIdx = random.getInt(0, c.genes.length);
			final Gene<T> selectedRisGene = c.genes[geneIdx];

			int funcChosenStartIdx = random.getInt(0, selectedRisGene.architecture.headLength);
			while (!(selectedRisGene.sequenceAt(funcChosenStartIdx) instanceof GeneFunction)) {
				funcChosenStartIdx++;
				if (funcChosenStartIdx == selectedRisGene.architecture.headLength) {
					// we didn't find a function starting at the randomly chosen
					// element => The operator does nothing.
					return;
				}
			}

			int funcChosenEndIdx = random.getInt(funcChosenStartIdx, selectedRisGene.architecture.headLength);

			GeneElement<T>[] risElement = selectedRisGene.getSubsequence(funcChosenStartIdx,
					funcChosenEndIdx - funcChosenStartIdx + 1);

			// select gene in which the ris element should be inserted
			ArrayList<Gene<T>> genesWithMatchinArchitecture = new ArrayList<>();
			for (Gene<T> gene : c.genes) {
				if (gene.architecture.equals(selectedRisGene.architecture)
						&& gene.architecture.headLength >= risElement.length) {
					genesWithMatchinArchitecture.add(gene);
				}
			}
			Gene<T> selectedInsertionTarget = genesWithMatchinArchitecture
					.get(random.getInt(0, genesWithMatchinArchitecture.size()));

			// insert the ris element
			for (int i = selectedInsertionTarget.architecture.headLength - 1; i >= risElement.length; i--) {
				selectedInsertionTarget.setSequenceAt(i, selectedInsertionTarget.sequenceAt(i - risElement.length));
			}

			selectedInsertionTarget.setSequenceIntervall(0, risElement, 0, risElement.length);
		}

	}

	@Override
	protected <T> void apply(Gene<T> gene) {
		// can't be called => method stub!
		throw new UnsupportedOperationException("This operator works on chromosome level");
	}

	@Override
	public String getName() {
		return "Root Transposition";
	}

}
