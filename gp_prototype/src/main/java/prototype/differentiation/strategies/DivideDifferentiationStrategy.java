package prototype.differentiation.strategies;

import prototype.differentiation.Function;
import prototype.differentiation.FunctionType;
import prototype.differentiation.TreeNode;
import prototype.differentiation.functions.Divide;
import prototype.differentiation.functions.Multiply;
import prototype.differentiation.functions.Subtract;

/**
 * User: koperek
 * Date: 26.02.13
 * Time: 22:09
 */
public class DivideDifferentiationStrategy extends DifferentiationStrategy {

    @Override
    protected Function differentiateSpecific(TreeNode treeNode, String variable) {
        TreeNode[] children = treeNode.getChildren();

        Function f_diff = differentiateChild(children[0], variable);
        Function g_diff = differentiateChild(children[1], variable);

        Function f = translate(children[0]);
        Function g = translate(children[1]);

        return
                new Divide(
                        new Subtract(
                                new Multiply(f_diff, g),
                                new Multiply(f, g_diff)),
                        new Multiply(g, g));
    }
}
