package gep.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	private final Gene<T> rootGene;
	
	public ChromosomalArchitecture(Gene<T> rootGene) {
		this.genes = new ArrayList<Gene<T>>();
		this.immutableGenes = new ArrayList<Gene<T>>();
		this.rootGene = rootGene;
	}

	public ChromosomalArchitecture(List<Gene<T>> genes, Gene<T> rootGene) {
		this(rootGene);
		addGenes(genes);
	}
	
	public ChromosomalArchitecture(ExpressionTreeNode<T> staticLinkingFunction) {
		this(toStaticGene(staticLinkingFunction));
	}
	
	public ChromosomalArchitecture(List<Gene<T>> genes, ExpressionTreeNode<T> staticLinkingFunction) {
		this(toStaticGene(staticLinkingFunction));
		addGenes(genes);
	}

	public Chromosome<T> createRandomChromosome(RandomEngine r) {
		
		if(genes.isEmpty()) {
			throw new IllegalStateException("Cannot create chromosomes without one modifiable gene");
		}
		
		return new Chromosome<T>(genes, immutableGenes, rootGene);

	}

	public void addGene(Gene<T> g) {
		if(g.architecture.isModifiable) {
			genes.add(g);
		} else {
			immutableGenes.add(g);
		}
	}
	
	public void addGenes(List<Gene<T>> genes) {
		for (Gene<T> g : genes) {
			addGene(g);
		}
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
	
	private Chromosome<T> create() {
		
		Deque<Gene<T>> unresolvedDependenciesStack = new ArrayDeque<>();
		Set<Gene<T>> unresolvedDependenciesSet = new HashSet<Gene<T>>();
		unresolvedDependenciesStack.push(rootGene);
		unresolvedDependenciesSet.add(rootGene);
		
		HashMap<Gene<T>, Gene<T>> geneCopyMapping = new HashMap<>();
		
		while(!unresolvedDependenciesStack.isEmpty()) {
			Gene<T> curr = unresolvedDependenciesStack.peek();
			
			List<GeneTerminal<T>> potTerminals = curr.architecture.potentialTerminals;
			
			boolean dependencyFound = false;
			for (GeneTerminal<T> potTerminal : potTerminals) {
				if(potTerminal instanceof HomoeoticGeneElement) {
					HomoeoticGeneElement<T> link = (HomoeoticGeneElement<T>) potTerminal;
					if(unresolvedDependenciesSet.add(link.linkedGene)) {
						// new dependency found
						unresolvedDependenciesStack.push(link.linkedGene);
						dependencyFound = true;
					}
				}
			}
		}
		
		
	}


}
