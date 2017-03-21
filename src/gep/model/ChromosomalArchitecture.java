package gep.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import gep.random.RandomEngine;

/**
 * 
 * @author jannis
 * 
 * TODO maybe delete ChromosomalArchitecture class and move contents to Chromosome
 *
 * @param <T>
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

	public int addGene(Gene<T> g) {
		if(g.architecture.isModifiable) {
			genes.add(g);
			return genes.size();
		} else {
			immutableGenes.add(g);
			return immutableGenes.size();
		}
	}
	
	public List<Integer> addGenes(List<Gene<T>> genes) {
		ArrayList<Integer> ids = new ArrayList<>(genes.size());
		for (Gene<T> g : genes) {
			ids.add(addGene(g));
		}
		return ids;
	}
	
	public void setChromosomeRoot(ExpressionTreeNode<T> staticLinkingFunctionAsRoot) {
		setChromosomeRoot(toStaticGene(staticLinkingFunctionAsRoot));
	}
	
	public void setChromosomeRoot(Gene<T> rootGene) {
		if(this.rootGene != null) {
			throw new IllegalStateException("The Chromosomes root is unique and can only be set once!");
		}
		
		this.rootGene = rootGene;
	}

	public void addStaticLinkingFunction(ExpressionTreeNode<T> etn) {
		immutableGenes.add(toStaticGene(etn));
	}
	
	private static <T> Gene<T> toStaticGene(ExpressionTreeNode<T> etn) {

		ArrayList<GeneElement<T>> sequence = new ArrayList<>();
		
		// perform a bfs to convert expression tree to gene
		Deque<ExpressionTreeNode<T>> queue = new ArrayDeque<>();
		queue.addLast(etn);
		
		while(!queue.isEmpty()) {
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
		
		if(genes.isEmpty()) {
			throw new IllegalStateException("Cannot create chromosomes without one modifiable gene");
		}
		
		if(rootGene == null) {
			throw new IllegalStateException("The Chromosomes root is not set!");
		}
		
		Gene<T> generatedRoot = rootGene.architecture.createRandomGene(r);
		
		@SuppressWarnings("unchecked")
		Gene<T>[] generatedGenes = new Gene[genes.size()];
		for(int i = 0; i < genes.size(); i++) {
			generatedGenes[i] = genes.get(i).architecture.createRandomGene(r);
		}
		@SuppressWarnings("unchecked")
		Gene<T>[] immutableGenes = new Gene[this.immutableGenes.size()];
		this.immutableGenes.toArray(immutableGenes);
		
		return new Chromosome<T>(generatedGenes, immutableGenes, generatedRoot);

	}
	
	/*
	 * Deque<Gene<T>> unresolvedDependenciesStack = new ArrayDeque<>();
		Set<Gene<T>> unresolvedDependenciesSet = new HashSet<Gene<T>>();
		unresolvedDependenciesStack.push(rootGene);
		unresolvedDependenciesSet.add(rootGene);
		
		HashMap<Gene<T>, Gene<T>> geneCopyMapping = new HashMap<>();
		
		while(!unresolvedDependenciesStack.isEmpty()) {
			Gene<T> curr = unresolvedDependenciesStack.peek();
			
			List<GeneTerminal<T>> potTerminals = curr.architecture.potentialTerminals;
			
			boolean unresolvedDependencyFound = false;
			for (GeneTerminal<T> potTerminal : potTerminals) {
				if(potTerminal instanceof HomoeoticGeneElement) {
					HomoeoticGeneElement<T> link = (HomoeoticGeneElement<T>) potTerminal;
					if(unresolvedDependenciesSet.add(link.linkedGene)) {
						// new dependency found
						unresolvedDependenciesStack.push(link.linkedGene);
						unresolvedDependencyFound = true;
					}
				}
			}
			if(!unresolvedDependencyFound) {
				GeneArchitecture<T>
			}
		}
	 */


}
