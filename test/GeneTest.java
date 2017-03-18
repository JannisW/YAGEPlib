import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import gep.model.ExpressionTreeNode;
import gep.model.Gene;
import gep.model.GeneArchitecture;
import gep.model.GeneFunction;
import gep.model.GeneTerminal;

public class GeneTest {

	@Test
	public void testBasicGeneExpression() {

		GeneFunction<Boolean> oneArgFunc = new GenericOneArgFunc();
		GeneFunction<Boolean> twoArgFunc1 = new GenericTwoArgFunc();
		GeneFunction<Boolean> twoArgFunc2 = new GenericTwoArgFunc();
		GeneFunction<Boolean> twoArgFunc3 = new GenericTwoArgFunc();
		GeneFunction<Boolean> twoArgFunc4 = new GenericTwoArgFunc();
		ArrayList<GeneFunction<Boolean>> potentialFunctions = new ArrayList<>();
		potentialFunctions.add(oneArgFunc);
		potentialFunctions.add(twoArgFunc1);
		potentialFunctions.add(twoArgFunc2);
		potentialFunctions.add(twoArgFunc3);
		potentialFunctions.add(twoArgFunc4);

		final int maxArity = 2;
		final int headLength = 7;
		final int geneLength = headLength * (maxArity - 1) + 1;
		ArrayList<GeneTerminal<Boolean>> potentialTerminals = new ArrayList<>();
		for (int i = 0; i < geneLength - potentialFunctions.size(); i++) {
			potentialTerminals.add(new GenericTerminal());
		}

		GeneArchitecture<Boolean> arch = new GeneArchitecture<>(headLength, potentialFunctions, potentialTerminals);

		Gene<Boolean> gene = new Gene<>(arch);
		gene.sequence[0] = twoArgFunc1;
		gene.sequence[1] = twoArgFunc2;
		gene.sequence[2] = oneArgFunc;
		gene.sequence[3] = potentialTerminals.get(0);
		gene.sequence[4] = twoArgFunc3;
		gene.sequence[5] = twoArgFunc4;
		for(int i = 6; i < geneLength; i++){
			gene.sequence[i] = potentialTerminals.get(i-5);
		}
		
		ExpressionTreeNode<Boolean> etn = gene.express();
		
		assertEquals(twoArgFunc1, etn.getNodeElement());
	}

	class GenericOneArgFunc extends GeneFunction<Boolean> {

		public GenericOneArgFunc() {
			super("generic one arg func", "o", 1);
		}

		@Override
		public Boolean apply(List<ExpressionTreeNode<Boolean>> expTreeChilds) {
			// Auto-generated method stub
			return null;
		}

	}

	class GenericTwoArgFunc extends GeneFunction<Boolean> {

		public GenericTwoArgFunc() {
			super("generic two arg func", "d", 2);
		}

		@Override
		public Boolean apply(List<ExpressionTreeNode<Boolean>> expTreeChilds) {
			// Auto-generated method stub
			return null;
		}

	}

	class GenericTerminal extends GeneTerminal<Boolean> {

		public GenericTerminal() {
			super("generic terminal", "t");
		}

		@Override
		public Boolean apply() {
			// Auto-generated method stub
			return null;
		}

	}

}
