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
package gep.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import gep.random.DefaultRandomEngine;
import gep.random.RandomEngine;

public class GeneArchitecture<T> implements Serializable {

	/**
	 * The version UID used for serialization.
	 */
	private static final long serialVersionUID = 3231873693626540222L;

	transient public static final RandomEngine DEFAULT_RANDOM_ENGINE = new DefaultRandomEngine();

	public final int headLength;

	public final int maxArity;

	public final ArrayList<GeneFunction<T>> potentialFunctions;
	public final ArrayList<? extends GeneTerminal<T>> potentialTerminals;

	public final boolean isModifiable;

	public GeneArchitecture(int headlength, ArrayList<GeneFunction<T>> potentialFunctions,
			ArrayList<? extends GeneTerminal<T>> potentialTerminals) {
		this(headlength, potentialFunctions, potentialTerminals, true);
	}

	public GeneArchitecture(int headlength, ArrayList<GeneFunction<T>> potentialFunctions,
			ArrayList<? extends GeneTerminal<T>> potentialTerminals, boolean isModifiable) {

		if (potentialTerminals.isEmpty()) {
			throw new IllegalArgumentException("The set of terminals can't be empty.");
		}

		if (headlength < 1) {
			throw new IllegalArgumentException("The head length has to be greater than 0.");
		}

		this.headLength = headlength;
		this.potentialFunctions = potentialFunctions;
		this.potentialTerminals = potentialTerminals;
		int maxArity = 1;
		for (GeneFunction<T> geneFunction : potentialFunctions) {
			maxArity = Math.max(maxArity, geneFunction.getArity());
		}
		this.maxArity = maxArity;
		this.isModifiable = isModifiable;
	}

	/**
	 * Returns the length of genes that are using this architecture.
	 * 
	 * The length includes non-coding regions of the gene.
	 * 
	 * @return The length of the corresponding gene
	 */
	public int getGeneLength() {
		// headLength + tailLength (tailLength == headLength * (maxArity - 1) +
		// 1)
		return this.headLength * maxArity + 1;
	}

	public Gene<T> createRandomGene() {
		return createRandomGene(DEFAULT_RANDOM_ENGINE);
	}

	public Gene<T> createRandomGene(RandomEngine r) {

		Gene<T> generatedGene = new Gene<T>(this);

		// generate head
		for (int i = 0; i < this.headLength; i++) {
			if (r.decideTakeFunction() && !potentialFunctions.isEmpty()) {
				generatedGene.setSequenceAt(i, r.pickElement(potentialFunctions));
			} else {
				generatedGene.setSequenceAt(i, r.pickElement(potentialTerminals));
			}
		}

		// generate tail
		for (int i = this.headLength; i < generatedGene.getSequenceLength(); i++) {
			generatedGene.setSequenceAt(i, r.pickElement(potentialTerminals));
		}

		return generatedGene;
	}

	public List<Gene<T>> createRandomGenes(int numberOfInstances) {
		return createRandomGenes(DEFAULT_RANDOM_ENGINE, numberOfInstances);
	}

	public List<Gene<T>> createRandomGenes(RandomEngine r, int numberOfInstances) {
		List<Gene<T>> genes = new ArrayList<Gene<T>>(numberOfInstances);
		for (int i = 0; i < numberOfInstances; i++) {
			genes.add(createRandomGene(r));
		}
		return genes;
	}
	
	public static <T> Gene<T> createGeneFromSequence(List<GeneElement<T>> sequence) {
		return createGenesFromSequence(sequence, true, 1).get(0);
	}

	public static <T> Gene<T> createGeneFromSequence(List<GeneElement<T>> sequence, boolean isModifiable) {
		return createGenesFromSequence(sequence, isModifiable, 1).get(0);
	}

	public static <T> List<Gene<T>> createGenesFromSequence(List<GeneElement<T>> sequence, boolean isModifiable,
			int numInstances) {

		// identify the architecture...
		Set<GeneFunction<T>> potentialFunctions = new HashSet<GeneFunction<T>>();
		Set<GeneTerminal<T>> potentialTerminals = new HashSet<GeneTerminal<T>>();

		int headlength = 1;
		int elemCounter = 0;
		for (GeneElement<T> geneElement : sequence) {
			elemCounter++;
			if (geneElement instanceof GeneTerminal) {
				potentialTerminals.add((GeneTerminal<T>) geneElement);
			} else {
				if (geneElement instanceof GeneFunction) {
					potentialFunctions.add((GeneFunction<T>) geneElement);
					headlength = elemCounter;
				}
			}
		}

		// create the architecture
		GeneArchitecture<T> arch = new GeneArchitecture<>(headlength,
				new ArrayList<GeneFunction<T>>(potentialFunctions), new ArrayList<GeneTerminal<T>>(potentialTerminals),
				isModifiable);

		Gene<T> generatedGene = new Gene<T>(arch);

		// copy the sequence
		int idx = 0;
		Iterator<GeneElement<T>> it = sequence.iterator();
		while (idx < generatedGene.getSequenceLength() && it.hasNext()) {
			generatedGene.setSequenceAt(idx, it.next());
			idx++;
		}

		// if the sequence does not contain enough elements in the tail pad it
		// with random terminals
		if (idx < generatedGene.getSequenceLength()) {
			RandomEngine random = new DefaultRandomEngine();
			for (; idx < generatedGene.getSequenceLength(); idx++) {
				generatedGene.setSequenceAt(idx, random.pickElement(arch.potentialTerminals));
			}
		}

		// create (additional) gene instances
		ArrayList<Gene<T>> genes = new ArrayList<>(numInstances);
		genes.add(generatedGene);
		for (int i = 1; i < numInstances; i++) {
			Gene<T> g = new Gene<T>(arch);
			g.copyFrom(generatedGene);
			genes.add(g);
		}
		return genes;
	}

}
