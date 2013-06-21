package pl.edu.agh.hubert.differentiation.symbolic.strategies;

import pl.edu.agh.hubert.differentiation.symbolic.Function;
import pl.edu.agh.hubert.differentiation.symbolic.TreeNode;
import pl.edu.agh.hubert.differentiation.symbolic.functions.Divide;
import pl.edu.agh.hubert.differentiation.symbolic.functions.Multiply;
import pl.edu.agh.hubert.differentiation.symbolic.functions.Subtract;

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
