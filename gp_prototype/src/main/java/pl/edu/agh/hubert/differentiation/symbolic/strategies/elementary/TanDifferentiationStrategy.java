package pl.edu.agh.hubert.differentiation.symbolic.strategies.elementary;

import pl.edu.agh.hubert.differentiation.symbolic.Function;
import pl.edu.agh.hubert.differentiation.symbolic.TreeNode;
import pl.edu.agh.hubert.differentiation.symbolic.functions.Cos;
import pl.edu.agh.hubert.differentiation.symbolic.functions.Divide;
import pl.edu.agh.hubert.differentiation.symbolic.functions.Multiply;
import pl.edu.agh.hubert.differentiation.symbolic.strategies.DifferentiationStrategy;

/**
 * User: koperek
 * Date: 03.03.13
 * Time: 23:58
 */
public class TanDifferentiationStrategy extends DifferentiationStrategy {
    @Override
    protected Function differentiateSpecific(TreeNode treeNode, String variable) {
        TreeNode child = treeNode.getChildren()[0];

        return new Divide(
                differentiateChild(child, variable), // instead of Constant(1.0) we put here the chain rule
                new Multiply(
                        new Cos(translate(child)),
                        new Cos(translate(child))
                )
        );
    }
}
