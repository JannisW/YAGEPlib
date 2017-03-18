package gep.model;

import java.util.ArrayList;

import gep.random.RandomEngine;

public class GeneArchitecture<T> {

	public final int headLength;

	public final int maxArity;

	public final ArrayList<GeneFunction<T>> potentialFunctions;
	public final ArrayList<GeneTerminal<T>> potentialTerminals;

	public GeneArchitecture(int headlength, ArrayList<GeneFunction<T>> potentialFunctions, ArrayList<GeneTerminal<T>> potentialTerminals) {
		this.headLength = headlength;
		this.potentialFunctions = potentialFunctions;
		this.potentialTerminals = potentialTerminals;
		int maxArity = 1;
		for (GeneFunction<T> geneFunction : potentialFunctions) {
			maxArity = Math.max(maxArity, geneFunction.getArity());
		}
		this.maxArity = maxArity;
	}

	/**
	 * Returns the length of genes which are using this architecture
	 * 
	 * @return The length of the corresponding gene
	 */
	public int getGeneLength() {
		return this.headLength * (maxArity - 1) + 1;
	}

	public Gene<T> createRandomGene(RandomEngine r) {
		Gene<T> generatedGene = new Gene<T>(this);

		// generate head
		for (int i = 0; i < this.headLength; i++) {
			if (r.decideTakeFunction()) {
				generatedGene.sequence[i] = r.pickElement(potentialFunctions);
			} else {
				generatedGene.sequence[i] = r.pickElement(potentialTerminals);
			}
		}

		// generate tail
		for (int i = this.headLength; i < generatedGene.sequence.length; i++) {
			generatedGene.sequence[i] = r.pickElement(potentialFunctions);
		}

		return generatedGene;
	}

}
