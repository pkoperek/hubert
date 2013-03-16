package prototype.differentiation.symbolic.strategies;

import prototype.differentiation.symbolic.Function;
import prototype.differentiation.symbolic.TreeNode;
import prototype.differentiation.symbolic.functions.Subtract;

/**
 * User: koperek
 * Date: 26.02.13
 * Time: 19:38
 */
public class SubtractDifferentiationStrategy extends DifferentiationStrategy {

    @Override
    protected Function differentiateSpecific(TreeNode treeNode, String variable) {
        TreeNode[] children = treeNode.getChildren();
        return new Subtract(differentiateChild(children[0], variable), differentiateChild(children[1], variable));
    }
}
