package prototype.differentiation.strategies;

import prototype.differentiation.Function;
import prototype.differentiation.FunctionType;
import prototype.differentiation.TreeNode;
import prototype.differentiation.functions.Constant;

/**
 * User: koperek
 * Date: 26.02.13
 * Time: 22:47
 */
public class ConstantDifferentiationStrategy extends DifferentiationStrategy {

    @Override
    protected Function differentiateSpecific(TreeNode treeNode, String variable) {
        return new Constant(0.0);
    }
}
