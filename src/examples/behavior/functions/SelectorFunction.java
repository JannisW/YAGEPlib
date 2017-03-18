package examples.behavior.functions;

import java.util.List;

import gep.model.ExpressionTreeNode;
import gep.model.GeneElement;
import gep.model.GeneFunction;

public class SelectorFunction extends GeneFunction<Boolean> {

	public SelectorFunction() {
		super("Selector (fallback) node", "F", 2);
	}

	@Override
	public Boolean apply(List<ExpressionTreeNode<Boolean>> expTreeChilds) {
		for (ExpressionTreeNode<Boolean> child: expTreeChilds) {
			GeneElement<Boolean> childElem = child.getNodeElement();
			
			// execute every child as long as one child returns success
			if(childElem.apply(child.getChildren())){
				return true;
			}
		}
		return false;
	}

}
