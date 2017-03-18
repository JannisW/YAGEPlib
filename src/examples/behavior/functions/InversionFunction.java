package examples.behavior.functions;

import java.util.List;

import gep.model.ExpressionTreeNode;
import gep.model.GeneFunction;

public class InversionFunction extends GeneFunction<Boolean> {

	public InversionFunction() {
		super("Inversion", "I", 1);
	}

	@Override
	public Boolean apply(List<ExpressionTreeNode<Boolean>> expTreeChilds) {
		
		ExpressionTreeNode<Boolean> argument = expTreeChilds.get(0);
		
		return argument.getNodeElement().apply(argument.getChildren());
	}

}
