package prototype.differentiation.strategies;

import prototype.differentiation.Function;
import prototype.differentiation.FunctionType;
import prototype.differentiation.TreeNode;
import prototype.differentiation.functions.Subtract;

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
