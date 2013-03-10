package prototype.differentiation.strategies.elementary;

import prototype.differentiation.functions.Constant;
import prototype.differentiation.functions.Multiply;
import prototype.differentiation.functions.Sin;
import prototype.differentiation.strategies.DifferentiationStrategy;
import prototype.differentiation.Function;
import prototype.differentiation.FunctionType;
import prototype.differentiation.TreeNode;

/**
 * User: koperek
 * Date: 26.02.13
 * Time: 22:39
 */
public class CosDifferentiationStrategy extends DifferentiationStrategy {

    @Override
    protected Function differentiateSpecific(TreeNode treeNode, String variable) {
        TreeNode internalFunctionNode = treeNode.getChildren()[0];
        Function internalFunction = translate(internalFunctionNode);
        return new Multiply(new Multiply(new Constant(-1.0), new Sin(internalFunction)), internalFunctionNode.differentiate(variable));
    }
}
