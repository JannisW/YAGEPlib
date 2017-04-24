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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import gep.model.ChromosomalArchitecture;
import gep.model.Chromosome;
import gep.model.ChromosomeIndependentGeneTerminal;
import gep.model.ExpressionTreeNode;
import gep.model.Gene;
import gep.model.GeneArchitecture;
import gep.model.GeneElement;
import gep.model.GeneFunction;
import gep.model.GeneTerminal;

public class GeneTest {

	@Test
	public void testBasicGeneExpression() {

		GeneFunction<Boolean> oneArgFunc = new GenericOneArgFunc(0);
		GeneFunction<Boolean> twoArgFunc1 = new GenericTwoArgFunc(0);
		GeneFunction<Boolean> twoArgFunc2 = new GenericTwoArgFunc(1);
		GeneFunction<Boolean> twoArgFunc3 = new GenericTwoArgFunc(2);
		GeneFunction<Boolean> twoArgFunc4 = new GenericTwoArgFunc(3);
		ArrayList<GeneFunction<Boolean>> potentialFunctions = new ArrayList<>();
		potentialFunctions.add(oneArgFunc);
		potentialFunctions.add(twoArgFunc1);
		potentialFunctions.add(twoArgFunc2);
		potentialFunctions.add(twoArgFunc3);
		potentialFunctions.add(twoArgFunc4);

		final int maxArity = 2;
		final int headLength = 7;
		final int geneLength = headLength + headLength * (maxArity - 1) + 1;
		ArrayList<GeneTerminal<Boolean>> potentialTerminals = new ArrayList<>();
		for (int i = 0; i < geneLength - potentialFunctions.size(); i++) {
			potentialTerminals.add(new GenericTerminal(i));
		}

		ArrayList<GeneElement<Boolean>> geneSeq = new ArrayList<>();
		geneSeq.add(twoArgFunc1);
		
		geneSeq.add(twoArgFunc2);
		geneSeq.add(oneArgFunc);
		geneSeq.add(potentialTerminals.get(0));
		geneSeq.add(twoArgFunc3);
		geneSeq.add(twoArgFunc4);
		for (int i = 6; i < geneLength; i++) {
			geneSeq.add(potentialTerminals.get(i - 5));
		}
		
		Gene<Boolean> gene = GeneArchitecture.createGeneFromSequence(geneSeq);
		
		Chromosome<Boolean> c = ChromosomalArchitecture.createSingleGenicChromosome(gene);

		// TODO check expression tree returned by the chromosome as well (not
		// just tree returned by the gene)

		ExpressionTreeNode<Boolean> etn = gene.express(c);

		System.out.println(etn);

		// check root
		assertEquals(twoArgFunc1, etn.getNodeElement());

		// check first level
		List<ExpressionTreeNode<Boolean>> currChildren = etn.getChildren();
		assertEquals(2, currChildren.size());
		assertEquals(twoArgFunc2, currChildren.get(0).getNodeElement());
		assertEquals(oneArgFunc, currChildren.get(1).getNodeElement());

		// check second level
		currChildren = etn.getChildren().get(0).getChildren();
		assertEquals(2, currChildren.size());
		assertEquals(potentialTerminals.get(0), currChildren.get(0).getNodeElement());
		assertEquals(twoArgFunc3, currChildren.get(1).getNodeElement());

		currChildren = etn.getChildren().get(1).getChildren();
		assertEquals(1, currChildren.size());
		assertEquals(twoArgFunc4, currChildren.get(0).getNodeElement());

		// check third level
		currChildren = etn.getChildren().get(0).getChildren().get(0).getChildren();
		assertEquals(0, currChildren.size());

		currChildren = etn.getChildren().get(0).getChildren().get(1).getChildren();
		assertEquals(2, currChildren.size());
		assertEquals(potentialTerminals.get(1), currChildren.get(0).getNodeElement());
		assertEquals(potentialTerminals.get(2), currChildren.get(1).getNodeElement());
		assertEquals(0, currChildren.get(0).getChildren().size());
		assertEquals(0, currChildren.get(1).getChildren().size());

		currChildren = etn.getChildren().get(1).getChildren().get(0).getChildren();
		assertEquals(2, currChildren.size());
		assertEquals(potentialTerminals.get(3), currChildren.get(0).getNodeElement());
		assertEquals(potentialTerminals.get(4), currChildren.get(1).getNodeElement());
		assertEquals(0, currChildren.get(0).getChildren().size());
		assertEquals(0, currChildren.get(1).getChildren().size());

	}

	class GenericOneArgFunc extends GeneFunction<Boolean> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public GenericOneArgFunc(int id) {
			super("generic one arg func (ID=" + id + ")", "o" + id, 1);
		}

		@Override
		public Boolean apply(List<ExpressionTreeNode<Boolean>> expTreeChilds) {
			// Auto-generated method stub
			return null;
		}

	}

	class GenericTwoArgFunc extends GeneFunction<Boolean> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public GenericTwoArgFunc(int id) {
			super("generic two arg func (ID=" + id + ")", "d" + id, 2);
		}

		@Override
		public Boolean apply(List<ExpressionTreeNode<Boolean>> expTreeChilds) {
			// Auto-generated method stub
			return null;
		}

	}

	class GenericTerminal extends ChromosomeIndependentGeneTerminal<Boolean> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public GenericTerminal(int id) {
			super("generic terminal(ID=" + id + ")", "t" + id);
		}

		@Override
		public Boolean apply() {
			// Auto-generated method stub
			return null;
		}

	}

}
