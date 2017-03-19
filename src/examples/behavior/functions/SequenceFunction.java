package examples.behavior.functions;

import java.util.List;

import gep.model.ExpressionTreeNode;
import gep.model.GeneFunction;

public class SequenceFunction extends GeneFunction<Boolean> {

	public SequenceFunction() {
		super("Sequence node", "S", 2);
	}

	@Override
	public Boolean apply(List<ExpressionTreeNode<Boolean>> expTreeChilds) {
		for (ExpressionTreeNode<Boolean> child: expTreeChilds) {
			
			// execute every child as long as a child returns failure
			if(!child.execute()) {
				return false;
			}
		}
		return true;
	}

}
