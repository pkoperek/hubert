package prototype.differentiation.strategies.elementary;

import prototype.differentiation.functions.Exp;
import prototype.differentiation.functions.Multiply;
import prototype.differentiation.strategies.DifferentiationStrategy;
import prototype.differentiation.Function;
import prototype.differentiation.FunctionType;
import prototype.differentiation.TreeNode;

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
