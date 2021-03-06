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

import java.util.ArrayList;
import java.util.List;

import gep.random.RandomEngine;

/**
 * This class is a factory class to create random Chromosomes.
 * 
 * @author Johannes Wortmann
 *
 * @param <T>
 *            The return parameter type of the GeneElements contained in the
 *            created genes of the Chromosomes created by this factory.
 */
public class ChromosomalArchitecture<T> {

	/**
	 * The list of modifiable genes of the chromosomes created by this factory
	 */
	private ArrayList<Gene<T>> genes;

	/**
	 * The list of immutable genes (e.g. static linking functions) of the
	 * chromosomes created by this factory
	 */
	private ArrayList<Gene<T>> immutableGenes;

	/**
	 * The unmodifiable root gene of the chromosome created by this factory
	 */
	private Gene<T> rootGene = null;

	/**
	 * Creates a new factory for Chromosomes. Initially the chromosomes don't
	 * have any genes.
	 */
	public ChromosomalArchitecture() {
		this.genes = new ArrayList<Gene<T>>();
		this.immutableGenes = new ArrayList<Gene<T>>();
		this.rootGene = null;
	}

	/**
	 * Creates a new factory for Chromosomes. Initially the chromosomes contain
	 * the given genes.
	 * 
	 * @param genes
	 *            The genes of chromosomes produced by this factory
	 */
	public ChromosomalArchitecture(List<Gene<T>> genes) {
		this();
		addGenes(genes);
	}

	/**
	 * Adds a gene to the chromosomes produced by this factory.
	 * 
	 * @param geneArch
	 *            The architecture of the gene that should be added to the
	 *            chromsome.
	 * @return The unique geneId that identifies a gene in the chromosomes.
	 */
	public int addGene(GeneArchitecture<T> geneArch) {
		return addGene(geneArch.createRandomGene());
	}

	/**
	 * Adds a gene by its sequence to the chromosomes produced by this factory.
	 * The architecture is inferred from the sequence and if the sequence is not
	 * long enough it will be padded by terminals in the sequence.
	 * 
	 * @param sequence
	 *            The list of elements that define the genes
	 * 
	 * @return The unique geneId that identifies a gene in the chromosomes.
	 */
	public int addGene(List<GeneElement<T>> sequence) {
		return addGene(GeneArchitecture.createGeneFromSequence(sequence));
	}

	/**
	 * Adds the given gene to the chromosomes produced by this factory.
	 * 
	 * @param gene
	 *            the gene to be added
	 * @return The unique geneId that identifies a gene in the chromosomes.
	 */
	public int addGene(Gene<T> gene) {
		if (gene.architecture.isModifiable) {
			genes.add(gene);
			return genes.size();
		} else {
			immutableGenes.add(gene);
			return -immutableGenes.size();
		}
	}

	/**
	 * Convenient function that adds all given genes to this architecture and
	 * thus to the chromosomes produced by this factory.
	 * 
	 * @param genes
	 *            The list of genes to be added
	 * @return A list of gene ids. The order of returned ids matches the order
	 *         of genes in the given list.
	 */
	public List<Integer> addGenes(List<Gene<T>> genes) {
		ArrayList<Integer> ids = new ArrayList<>(genes.size());
		for (Gene<T> g : genes) {
			ids.add(addGene(g));
		}
		return ids;
	}

	/**
	 * <p>
	 * Adds the given gene to the architecture and sets the root of the
	 * chromosomes produced by this factory to that given gene. The given gene
	 * is not to be allowed to be part of the architecture already. Otherwise
	 * the behavior is undefined.The root is the gene of a chromosome from which
	 * the evaluation begins.
	 * </p>
	 * <p>
	 * Note: The root can just be set once.
	 * </p>
	 * 
	 * @param rootGene
	 *            The gene to be set as the root.
	 * 
	 * @throws IllegalStateException
	 *             If the root was already set before.
	 */
	public void setChromosomeRootToGene(Gene<T> rootGene) {
		if (this.rootGene != null) {
			throw new IllegalStateException("The Chromosomes root is unique and can only be set once!");
		}
		if (!rootGene.architecture.isModifiable) {
			this.rootGene = rootGene;
		} else {
			int rootId = addGene(rootGene);

			ArrayList<HomoeoticGeneElement<T>> link = new ArrayList<>();
			link.add(new HomoeoticGeneElement<T>("dummy static link", "dsl", rootId));
			GeneArchitecture<T> arch = new GeneArchitecture<T>(1, new ArrayList<>(), link);

			this.rootGene = arch.createRandomGene();
		}
	}

	/**
	 * <p>
	 * Sets the root of the chromosomes produced by this factory to the gene
	 * defined by the given geneId. The root is the gene of a chromosome from
	 * which the evaluation begins.
	 * </p>
	 * <p>
	 * Note: The root can just be set once.
	 * </p>
	 * 
	 * @param rootGene
	 *            The gene to be set as the root.
	 * 
	 * @throws IllegalStateException
	 *             If the root was already set before.
	 * @throws IllegalArgumentException
	 *             If the given geneId is invalid
	 */
	public void setChromosomeRootToGene(int geneId) {
		if (this.rootGene != null) {
			throw new IllegalStateException("The Chromosomes root is unique and can only be set once!");
		}

		if (!isValidGeneId(geneId)) {
			throw new IllegalArgumentException(
					geneId + " Is an invalid geneId (does not exist) in the current state. Add the gene first!");
		}

		ArrayList<HomoeoticGeneElement<T>> link = new ArrayList<>();
		link.add(new HomoeoticGeneElement<T>("dummy static link to " + geneId, "dsl", geneId));
		GeneArchitecture<T> arch = new GeneArchitecture<T>(1, new ArrayList<>(), link);

		this.rootGene = arch.createRandomGene();
	}

	/**
	 * Checks if the given geneId is valid (does not violate any boundaries)
	 * 
	 * @param geneId
	 *            The gene id to be checked
	 * @return true if there is a gene related to the given id, false otherwise.
	 */
	private boolean isValidGeneId(int geneId) {
		if (geneId == 0)
			return rootGene != null;
		else if (geneId > 0)
			return geneId <= genes.size();
		else // geneId < 0
			return (-geneId) <= immutableGenes.size();
	}

	/**
	 * 
	 * Adds a static linking function to the architecture. This linking function
	 * is represented as an unmodifiable gene. Its expression tree defines the
	 * linking function.
	 * 
	 * @param staticLinkingFunction
	 *            The unmodifiable gene encoding a static linking function.
	 * 
	 * @throws IllegalArgumentException
	 *             if the given gene is modifiable
	 */
	public void addStaticLinkingFunction(Gene<T> staticLinkingFunction) {
		if (staticLinkingFunction.architecture.isModifiable) {
			throw new IllegalArgumentException(
					"The given gene has to be immutable to be used as static linking function.");
		}
		immutableGenes.add(staticLinkingFunction);
	}

	/**
	 * Creates a random Chromosome with respect to the possible elements in the
	 * architectures genes. The order of the genes will be preserved only the
	 * genes itself are randomly created.
	 * 
	 * Convienient method which uses the default random engine.
	 * 
	 * @return A randomly created Chromosome
	 */
	public Chromosome<T> create() {
		return create(GeneArchitecture.DEFAULT_RANDOM_ENGINE);
	}

	/**
	 * Creates a random Chromosome with respect to the possible elements in the
	 * architectures genes. The order of the genes will be preserved only the
	 * genes itself are randomly created.
	 * 
	 * @param r
	 *            The random engine to be used for creation.
	 * @return A randomly created Chromosome
	 */
	public Chromosome<T> create(RandomEngine r) {

		if (genes.isEmpty()) {
			throw new IllegalStateException("Cannot create chromosomes without one modifiable gene");
		}

		if (rootGene == null) {
			throw new IllegalStateException("The Chromosomes root is not set!");
		}

		Gene<T> generatedRoot = rootGene.architecture.createRandomGene(r);

		@SuppressWarnings("unchecked")
		Gene<T>[] generatedGenes = new Gene[genes.size()];
		for (int i = 0; i < genes.size(); i++) {
			generatedGenes[i] = genes.get(i).architecture.createRandomGene(r);
		}
		@SuppressWarnings("unchecked")
		Gene<T>[] immutableGenes = new Gene[this.immutableGenes.size()];
		this.immutableGenes.toArray(immutableGenes);

		return new Chromosome<T>(generatedGenes, immutableGenes, generatedRoot);

	}

	/**
	 * Creates a chromosome that contains all the genes as they are set in this
	 * architecture. This means there will be no random generation of gene
	 * sequences. This is only useful if the genes were created by a sequence.
	 * 
	 * @return A chromosome which is an exact replica of all the genes in this
	 *         architecture.
	 * 
	 * @throws IllegalStateException
	 *             if no modifiable gene exists or the root is not set
	 */
	public Chromosome<T> createReplica() {

		if (genes.isEmpty()) {
			throw new IllegalStateException("Cannot create chromosomes without one modifiable gene");
		}

		if (rootGene == null) {
			throw new IllegalStateException("The Chromosomes root is not set!");
		}

		Gene<T> generatedRoot = new Gene<T>(rootGene);

		@SuppressWarnings("unchecked")
		Gene<T>[] generatedGenes = new Gene[genes.size()];
		for (int i = 0; i < genes.size(); i++) {
			generatedGenes[i] = new Gene<T>(genes.get(i));
		}
		@SuppressWarnings("unchecked")
		Gene<T>[] immutableGenes = new Gene[this.immutableGenes.size()];
		this.immutableGenes.toArray(immutableGenes);

		return new Chromosome<T>(generatedGenes, immutableGenes, generatedRoot);

	}

	/**
	 * Creates a Chromosome that only contains one modifiable Gene and no
	 * unmodifiable. To create another kind of Chromosomes use the non static
	 * methods of this class.
	 * 
	 * @param gene
	 *            The contained modifiable gene.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Chromosome<T> createSingleGenicChromosome(Gene<T> gene) {
		if (!gene.architecture.isModifiable) {
			throw new IllegalArgumentException("The given gene has to be set to modifiable");
		}

		ArrayList<HomoeoticGeneElement<T>> link = new ArrayList<>();

		// Link to the provided gene which gets the gene id 1 (0 is the dummy
		// linking function)
		link.add(new HomoeoticGeneElement<T>("dummy static link", "dsl", 1));

		GeneArchitecture<T> rootAarch = new GeneArchitecture<T>(1, new ArrayList<>(), link);
		Gene<T> generatedRoot = rootAarch.createRandomGene();

		return new Chromosome<T>(new Gene[] { new Gene<T>(gene) }, new Gene[] {}, generatedRoot);
	}

}
