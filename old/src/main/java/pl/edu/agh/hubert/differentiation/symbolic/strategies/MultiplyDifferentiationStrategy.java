package pl.edu.agh.hubert.differentiation.symbolic.strategies;

import org.apache.log4j.Logger;
import pl.edu.agh.hubert.differentiation.symbolic.Function;
import pl.edu.agh.hubert.differentiation.symbolic.TreeNode;
import pl.edu.agh.hubert.differentiation.symbolic.functions.Add;
import pl.edu.agh.hubert.differentiation.symbolic.functions.Multiply;

/**
 * User: koperek
 * Date: 26.02.13
 * Time: 22:09
 */
public class MultiplyDifferentiationStrategy extends DifferentiationStrategy {

    private static final Logger logger = Logger.getLogger(MultiplyDifferentiationStrategy.class);

    @Override
    protected Function differentiateSpecific(TreeNode treeNode, String variable) {
        if (logger.isDebugEnabled()) {
            logger.debug(treeNode.toString());
        }

        TreeNode[] children = treeNode.getChildren();

        Function f_diff = differentiateChild(children[0], variable);
        Function g_diff = differentiateChild(children[1], variable);
        Function f = translate(children[0]);
        Function g = translate(children[1]);

        return new Add(new Multiply(f_diff, g), new Multiply(f, g_diff));
    }

}
