package hubert.differentiation.symbolic.strategies;

import hubert.differentiation.symbolic.Function;
import hubert.differentiation.symbolic.TreeNode;
import hubert.differentiation.symbolic.functions.Add;

/**
 * User: koperek
 * Date: 26.02.13
 * Time: 19:19
 */
public class AddDifferentiationStrategy extends DifferentiationStrategy {

    @Override
    protected Function differentiateSpecific(TreeNode treeNode, String variable) {
        TreeNode[] children = treeNode.getChildren();
        return new Add(differentiateChild(children[0], variable), differentiateChild(children[1], variable));
    }
}
