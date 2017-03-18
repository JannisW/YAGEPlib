package gep.model;

import java.util.ArrayList;

import gep.random.RandomEngine;

public class ChromosomalArchitecture {

	// TODO maybe combine both fields
	private ArrayList<GeneArchitecture> basicGenes;
	private ArrayList<GeneArchitecture> homoeoticGenes;
	
	public ChromosomalArchitecture() {
		basicGenes = new ArrayList<GeneArchitecture>();
		homoeoticGenes = new ArrayList<GeneArchitecture>();
	}
	
	public ChromosomalArchitecture(ArrayList<GeneArchitecture> basicGenes) {
		this.basicGenes = basicGenes;
		homoeoticGenes = new ArrayList<GeneArchitecture>();
	}
	
	public ChromosomalArchitecture(ArrayList<GeneArchitecture> basicGenes, ArrayList<GeneArchitecture> homoeoticGenes) {
		this.basicGenes = basicGenes;
		this.homoeoticGenes = homoeoticGenes;
	}

	public Chromosome createRandomChromosome(RandomEngine r) {
		
		if(basicGenes.isEmpty()) {
			throw new IllegalStateException("Cannot create chromosomes without a regular gene");
		}
		
		Chromosome c = new Chromosome(basicGenes.size() + homoeoticGenes.size());

		int idx = 0;
		for (GeneArchitecture geneArchitecture : basicGenes) {
			c.genes[idx] = geneArchitecture.createRandomGene(r);
			idx++;
		}
		for (GeneArchitecture geneArchitecture : homoeoticGenes) {
			c.genes[idx] = geneArchitecture.createRandomGene(r);
			idx++;
		}

		return c;
	}

	public void addBasicGene(GeneArchitecture ga) {
		basicGenes.add(ga);
	}

	public void addHomoeoticGene(GeneArchitecture hga) {
		homoeoticGenes.add(hga);
	}

}
