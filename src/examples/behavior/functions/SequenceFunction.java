package examples.behavior.functions;

import java.util.List;

import gep.model.ExpressionTreeNode;
import gep.model.GeneElement;
import gep.model.GeneFunction;

public class SequenceFunction extends GeneFunction<Boolean> {

	public SequenceFunction() {
		super("Sequence node", "S", 2);
	}

	@Override
	public Boolean apply(List<ExpressionTreeNode<Boolean>> expTreeChilds) {
		for (ExpressionTreeNode<Boolean> child: expTreeChilds) {
			GeneElement<Boolean> childElem = child.getNodeElement();
			
			// execute every child as long as a child returns failure
			if(!childElem.apply(child.getChildren())){
				return false;
			}
		}
		return true;
	}

}
