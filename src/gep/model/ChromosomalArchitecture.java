package gep.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import gep.random.RandomEngine;

/**
 * 
 * @author Johannes Wortmann
 *
 * @param <T>
 *            The return parameter type of the GeneElements contained in the
 *            created genes of the Chromosomes created by this factory.
 */
public class ChromosomalArchitecture<T> {

	private ArrayList<Gene<T>> genes;
	private ArrayList<Gene<T>> immutableGenes;

	private Gene<T> rootGene = null;

	public ChromosomalArchitecture() {
		this.genes = new ArrayList<Gene<T>>();
		this.immutableGenes = new ArrayList<Gene<T>>();
		this.rootGene = null;
	}

	public ChromosomalArchitecture(List<Gene<T>> genes) {
		this();
		addGenes(genes);
	}

	public int addGene(GeneArchitecture<T> geneArch) {
		return addGene(geneArch.createRandomGene());
	}

	public int addGene(List<GeneElement<T>> sequence) {
		return addGene(GeneArchitecture.createGeneFromSequence(sequence));
	}

	public int addGene(Gene<T> g) {
		if (g.architecture.isModifiable) {
			genes.add(g);
			return genes.size();
		} else {
			immutableGenes.add(g);
			return -immutableGenes.size();
		}
	}

	public List<Integer> addGenes(List<Gene<T>> genes) {
		ArrayList<Integer> ids = new ArrayList<>(genes.size());
		for (Gene<T> g : genes) {
			ids.add(addGene(g));
		}
		return ids;
	}

	public void setChromosomeRootToStaticLinkingFunction(ExpressionTreeNode<T> staticLinkingFunctionAsRoot) {
		if (this.rootGene != null) {
			throw new IllegalStateException("The Chromosomes root is unique and can only be set once!");
		}

		this.rootGene = toStaticGene(staticLinkingFunctionAsRoot);
	}

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

	public void setChromosomeRootToGene(int geneId) {
		if (this.rootGene != null) {
			throw new IllegalStateException("The Chromosomes root is unique and can only be set once!");
		}

		if (!isValidGeneId(geneId)) {
			throw new IllegalStateException(
					geneId + " Is an invalid geneId (does not exist) in the current state. Add the gene first!");
		}

		ArrayList<HomoeoticGeneElement<T>> link = new ArrayList<>();
		link.add(new HomoeoticGeneElement<T>("dummy static link to " + geneId, "dsl", geneId));
		GeneArchitecture<T> arch = new GeneArchitecture<T>(1, new ArrayList<>(), link);

		this.rootGene = arch.createRandomGene();
	}

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
	 * TODO adjust because not longer usable (expressiontree requires
	 * chromosome)...
	 * 
	 * @param etn
	 */
	public void addStaticLinkingFunction(ExpressionTreeNode<T> etn) {
		immutableGenes.add(toStaticGene(etn));
	}

	private static <T> Gene<T> toStaticGene(ExpressionTreeNode<T> etn) {

		ArrayList<GeneElement<T>> sequence = new ArrayList<>();

		// perform a bfs to convert expression tree to gene
		Deque<ExpressionTreeNode<T>> queue = new ArrayDeque<>();
		queue.addLast(etn);

		while (!queue.isEmpty()) {
			ExpressionTreeNode<T> currRoot = queue.removeFirst();
			sequence.add(currRoot.getNodeElement());
			for (ExpressionTreeNode<T> child : currRoot.getChildren()) {
				queue.addLast(child);
			}
		}

		return GeneArchitecture.createGeneFromSequence(sequence, false);
	}

	public Chromosome<T> create() {
		return create(GeneArchitecture.DEFAULT_RANDOM_ENGINE);
	}

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
	 * @throws IllegalStateException if no modifiable gene exists or the root is not set
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
		ArrayList<HomoeoticGeneElement<T>> link = new ArrayList<>();

		// Link to the provided gene which gets the gene id 1 (0 is the dummy
		// linking function)
		link.add(new HomoeoticGeneElement<T>("dummy static link", "dsl", 1));

		GeneArchitecture<T> rootAarch = new GeneArchitecture<T>(1, new ArrayList<>(), link);
		Gene<T> generatedRoot = rootAarch.createRandomGene();

		return new Chromosome<T>(new Gene[] { new Gene<T>(gene) }, new Gene[] {}, generatedRoot);
	}

	/*
	 * Deque<Gene<T>> unresolvedDependenciesStack = new ArrayDeque<>();
	 * Set<Gene<T>> unresolvedDependenciesSet = new HashSet<Gene<T>>();
	 * unresolvedDependenciesStack.push(rootGene);
	 * unresolvedDependenciesSet.add(rootGene);
	 * 
	 * HashMap<Gene<T>, Gene<T>> geneCopyMapping = new HashMap<>();
	 * 
	 * while(!unresolvedDependenciesStack.isEmpty()) { Gene<T> curr =
	 * unresolvedDependenciesStack.peek();
	 * 
	 * List<GeneTerminal<T>> potTerminals =
	 * curr.architecture.potentialTerminals;
	 * 
	 * boolean unresolvedDependencyFound = false; for (GeneTerminal<T>
	 * potTerminal : potTerminals) { if(potTerminal instanceof
	 * HomoeoticGeneElement) { HomoeoticGeneElement<T> link =
	 * (HomoeoticGeneElement<T>) potTerminal;
	 * if(unresolvedDependenciesSet.add(link.linkedGene)) { // new dependency
	 * found unresolvedDependenciesStack.push(link.linkedGene);
	 * unresolvedDependencyFound = true; } } } if(!unresolvedDependencyFound) {
	 * GeneArchitecture<T> } }
	 */

}
