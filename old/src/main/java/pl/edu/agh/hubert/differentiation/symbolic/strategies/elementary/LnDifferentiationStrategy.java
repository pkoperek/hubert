package pl.edu.agh.hubert.differentiation.symbolic.strategies.elementary;

import org.apache.log4j.Logger;
import pl.edu.agh.hubert.differentiation.symbolic.Function;
import pl.edu.agh.hubert.differentiation.symbolic.TreeNode;
import pl.edu.agh.hubert.differentiation.symbolic.functions.Constant;
import pl.edu.agh.hubert.differentiation.symbolic.functions.Divide;
import pl.edu.agh.hubert.differentiation.symbolic.functions.Multiply;
import pl.edu.agh.hubert.differentiation.symbolic.strategies.DifferentiationStrategy;

/**
 * User: koperek
 * Date: 01.03.13
 * Time: 23:13
 */
public class LnDifferentiationStrategy extends DifferentiationStrategy {

    private Logger logger = Logger.getLogger(LnDifferentiationStrategy.class);

    @Override
    protected Function differentiateSpecific(TreeNode treeNode, String variable) {

        if (logger.isDebugEnabled()) {
            logger.debug("Ln: " + treeNode);
        }

        TreeNode child = treeNode.getChildren()[0];
        return new Multiply(
                new Divide(new Constant(1.0), translate(child)),
                differentiateChild(child, variable)
        );
    }
}