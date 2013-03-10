package prototype.differentiation.strategies.elementary;

import prototype.differentiation.strategies.DifferentiationStrategy;
import prototype.differentiation.Function;
import prototype.differentiation.FunctionType;
import prototype.differentiation.TreeNode;
import prototype.differentiation.functions.Cos;
import prototype.differentiation.functions.Multiply;

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
