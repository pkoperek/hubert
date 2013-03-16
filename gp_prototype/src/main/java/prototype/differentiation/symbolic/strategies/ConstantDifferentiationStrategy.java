package prototype.differentiation.symbolic.strategies;

import prototype.differentiation.symbolic.Function;
import prototype.differentiation.symbolic.TreeNode;
import prototype.differentiation.symbolic.functions.Constant;

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
