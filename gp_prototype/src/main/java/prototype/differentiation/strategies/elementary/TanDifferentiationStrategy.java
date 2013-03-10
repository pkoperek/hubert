package prototype.differentiation.strategies.elementary;

import prototype.differentiation.Function;
import prototype.differentiation.TreeNode;
import prototype.differentiation.functions.Constant;
import prototype.differentiation.functions.Cos;
import prototype.differentiation.functions.Divide;
import prototype.differentiation.functions.Multiply;
import prototype.differentiation.strategies.DifferentiationStrategy;

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
