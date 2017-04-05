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

/**
 * This class is a factory class to create random genes and works as a
 * identifier for genes to test if they were produced by the same architecture
 * and therefore can be genetically merged.
 * 
 * An instance of this class includes meta information shared by all genes
 * created by this factory.
 * 
 * @author Johannes Wortmann
 *
 * @param <T>
 *            The return type of a node in the encoded expression tree.
 */
public class GeneArchitecture<T> implements Serializable {

	/**
	 * The version UID used for serialization.
	 */
	private static final long serialVersionUID = 3231873693626540222L;

	/**
	 * A reference to the default random engine.
	 */
	transient public static final RandomEngine DEFAULT_RANDOM_ENGINE = new DefaultRandomEngine();

	/**
	 * The length of the head of a gene that was created with respect to this
	 * gene architecture.
	 */
	public final int headLength;

	/**
	 * The maximum number of arguments a function in the set of possibles
	 * functions can take.
	 */
	public final int maxArity;

	/**
	 * The list of possible function for a gene that was created with respect to
	 * this gene architecture.
	 */
	public final ArrayList<GeneFunction<T>> potentialFunctions;

	/**
	 * The list of possible terminals for a gene that was created with respect
	 * to this gene architecture.
	 */
	public final ArrayList<? extends GeneTerminal<T>> potentialTerminals;

	/**
	 * True if this gene is allowed to be modified by genetic operators, false
	 * otherwise (e.g. static linking functions).
	 */
	public final boolean isModifiable;

	/**
	 * Creates a new factory for modifiable genes with respect to the given
	 * factory parameters.
	 * 
	 * @param headlength
	 *            The length of the head of generated genes.
	 * @param potentialFunctions
	 *            The set of potential functions for generated genes.
	 * @param potentialTerminals
	 *            The set of potential terminals for generated genes.
	 */
	public GeneArchitecture(int headlength, ArrayList<GeneFunction<T>> potentialFunctions,
			ArrayList<? extends GeneTerminal<T>> potentialTerminals) {
		this(headlength, potentialFunctions, potentialTerminals, true);
	}

	/**
	 * Creates a new factory for genes with respect to the given factory
	 * parameters.
	 * 
	 * @param headlength
	 *            The length of the head of generated genes.
	 * @param potentialFunctions
	 *            The set of potential functions for generated genes.
	 * @param potentialTerminals
	 *            The set of potential terminals for generated genes.
	 * @param isModifiable
	 *            True if the generated genes should be modifiable by genetic
	 *            operators, false otherwise.
	 */
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
	 * The length is the sum of the head and the tail of genes and includes
	 * non-coding regions of the gene.
	 * 
	 * @return The length of the corresponding gene
	 */
	public int getGeneLength() {
		// headLength + tailLength (tailLength == headLength * (maxArity - 1) +
		// 1)
		return this.headLength * maxArity + 1;
	}

	/**
	 * Creates a random instance of a gene using this architecture.
	 * 
	 * This function uses the default random engine.
	 * 
	 * @return A random instance of a gene using this architecture.
	 */
	public Gene<T> createRandomGene() {
		return createRandomGene(DEFAULT_RANDOM_ENGINE);
	}

	/**
	 * Creates a random instance of a gene using this architecture
	 * 
	 * @param r
	 *            The random engine that should be used.
	 * 
	 * @return A random instance of a gene using this architecture.
	 */
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

	/**
	 * Convenient function to create multiple random instances of genes using
	 * this architecture.
	 * 
	 * This function uses the default random engine.
	 * 
	 * @param numberOfInstances
	 *            The number of random genes that should be created.
	 * @return A list of randomly created genes that use this architecture
	 */
	public List<Gene<T>> createRandomGenes(int numberOfInstances) {
		return createRandomGenes(DEFAULT_RANDOM_ENGINE, numberOfInstances);
	}

	/**
	 * Convenient function to create multiple random instances of genes using
	 * this architecture.
	 * 
	 * This function uses the default random engine.
	 * 
	 * @param r
	 *            The random engine that should be used.
	 * 
	 * @param numberOfInstances
	 *            The number of random genes that should be created.
	 * @return A list of randomly created genes that use this architecture
	 */
	public List<Gene<T>> createRandomGenes(RandomEngine r, int numberOfInstances) {
		List<Gene<T>> genes = new ArrayList<Gene<T>>(numberOfInstances);
		for (int i = 0; i < numberOfInstances; i++) {
			genes.add(createRandomGene(r));
		}
		return genes;
	}

	/**
	 * Creates a new modifiable gene from the given sequence. If the sequence
	 * does not contain enough elements in the tail to be a valid gene this
	 * function will pad the gene with random terminals from the set of
	 * terminals contained in the sequence.
	 * <p>
	 * The corresponding GeneArchitecture can be accessed by accessing the gene
	 * architecture member of the returned gene. This architecture can then be
	 * used to create genes using the same architecture but with random
	 * sequence.
	 * 
	 * @param sequence
	 *            The sequence of the created gene.
	 * @return A new instance of a modifiable gene using the given sequence.
	 */
	public static <T> Gene<T> createGeneFromSequence(List<GeneElement<T>> sequence) {
		return createGenesFromSequence(sequence, true, 1).get(0);
	}

	/**
	 * Creates a new gene from the given sequence. If the sequence does not
	 * contain enough elements in the tail to be a valid gene this function will
	 * pad the gene with random terminals from the set of terminals contained in
	 * the sequence.
	 * <p>
	 * The corresponding GeneArchitecture can be accessed by accessing the gene
	 * architecture member of the returned gene. This architecture can then be
	 * used to create genes using the same architecture but with a random
	 * sequence.
	 * 
	 * @param sequence
	 *            The sequence of the created gene
	 * 
	 * @param isModifiable
	 *            True if the gene should be modifiable by genetic operators,
	 *            false otherwise.
	 * @return A new instance of a modifiable gene using the given sequence.
	 */
	public static <T> Gene<T> createGeneFromSequence(List<GeneElement<T>> sequence, boolean isModifiable) {
		return createGenesFromSequence(sequence, isModifiable, 1).get(0);
	}

	/**
	 * Creates multiple genes from the given sequence. If the sequence does not
	 * contain enough elements in the tail to be a valid gene this function will
	 * pad the gene with random terminals from the set of terminals contained in
	 * the sequence.
	 * <p>
	 * The corresponding GeneArchitecture can be accessed by accessing the gene
	 * architecture member of one of the returned genes. This architecture can
	 * then be used to create genes using the same architecture but with a
	 * random sequence.
	 * 
	 * @param sequence
	 *            The sequence of the created gene
	 * 
	 * @param isModifiable
	 *            True if the gene should be modifiable by genetic operators,
	 *            false otherwise.
	 * @param numInstances
	 *            the number of genes that should be created from the given
	 *            sequence.
	 * @return A new instance of a modifiable gene using the given sequence.
	 */
	public static <T> List<Gene<T>> createGenesFromSequence(List<GeneElement<T>> sequence, boolean isModifiable,
			int numInstances) {
		
		if(numInstances < 1) {
			throw new IllegalArgumentException("The number of instances has to be greater or equal than 1");
		}

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
