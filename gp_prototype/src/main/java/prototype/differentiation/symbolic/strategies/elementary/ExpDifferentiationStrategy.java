package prototype.differentiation.symbolic.strategies.elementary;

import prototype.differentiation.symbolic.Function;
import prototype.differentiation.symbolic.TreeNode;
import prototype.differentiation.symbolic.functions.Exp;
import prototype.differentiation.symbolic.functions.Multiply;
import prototype.differentiation.symbolic.strategies.DifferentiationStrategy;

/**
 * User: koperek
 * Date: 26.02.13
 * Time: 22:47
 */
public class ExpDifferentiationStrategy extends DifferentiationStrategy {

    @Override
    protected Function differentiateSpecific(TreeNode treeNode, String variable) {
        TreeNode child = treeNode.getChildren()[0];
        return new Multiply(new Exp(translate(child)), differentiateChild(child, variable));
    }
}
