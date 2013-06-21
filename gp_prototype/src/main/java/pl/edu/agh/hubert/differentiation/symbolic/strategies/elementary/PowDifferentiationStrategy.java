package pl.edu.agh.hubert.differentiation.symbolic.strategies.elementary;

import pl.edu.agh.hubert.differentiation.symbolic.Function;
import pl.edu.agh.hubert.differentiation.symbolic.TreeNode;
import pl.edu.agh.hubert.differentiation.symbolic.strategies.DifferentiationStrategy;

/**
 * User: koperek
 * Date: 26.02.13
 * Time: 22:48
 */
public class PowDifferentiationStrategy extends DifferentiationStrategy {

    @Override
    protected Function differentiateSpecific(TreeNode treeNode, String variable) {
        throw new UnsupportedOperationException();
    }
}
