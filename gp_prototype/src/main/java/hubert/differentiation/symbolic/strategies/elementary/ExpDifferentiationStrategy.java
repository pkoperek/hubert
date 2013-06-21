package hubert.differentiation.symbolic.strategies.elementary;

import hubert.differentiation.symbolic.Function;
import hubert.differentiation.symbolic.TreeNode;
import hubert.differentiation.symbolic.functions.Exp;
import hubert.differentiation.symbolic.functions.Multiply;
import hubert.differentiation.symbolic.strategies.DifferentiationStrategy;

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
