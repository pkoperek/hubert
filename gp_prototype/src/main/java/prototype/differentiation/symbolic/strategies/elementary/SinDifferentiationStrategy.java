package prototype.differentiation.symbolic.strategies.elementary;

import prototype.differentiation.symbolic.Function;
import prototype.differentiation.symbolic.TreeNode;
import prototype.differentiation.symbolic.functions.Cos;
import prototype.differentiation.symbolic.functions.Multiply;
import prototype.differentiation.symbolic.strategies.DifferentiationStrategy;

/**
 * User: koperek
 * Date: 26.02.13
 * Time: 22:44
 */
public class SinDifferentiationStrategy extends DifferentiationStrategy {

    @Override
    protected Function differentiateSpecific(TreeNode treeNode, String variable) {
        TreeNode internalFunctionNode = treeNode.getChildren()[0];
        Function internalFunction = translate(internalFunctionNode);
        return new Multiply(new Cos(internalFunction), internalFunctionNode.differentiate(variable));
    }
}
