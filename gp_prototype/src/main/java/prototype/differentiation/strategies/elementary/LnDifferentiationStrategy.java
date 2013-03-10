package prototype.differentiation.strategies.elementary;

import org.apache.log4j.Logger;
import prototype.differentiation.Function;
import prototype.differentiation.TreeNode;
import prototype.differentiation.functions.Constant;
import prototype.differentiation.functions.Divide;
import prototype.differentiation.functions.Multiply;
import prototype.differentiation.strategies.DifferentiationStrategy;

/**
 * User: koperek
 * Date: 01.03.13
 * Time: 23:13
 */
public class LnDifferentiationStrategy extends DifferentiationStrategy {

    private Logger logger = Logger.getLogger(LnDifferentiationStrategy.class);

    @Override
    protected Function differentiateSpecific(TreeNode treeNode, String variable) {

        if(logger.isDebugEnabled()) {
            logger.debug("Ln: " + treeNode);
        }

        TreeNode child = treeNode.getChildren()[0];
        return new Multiply(
                new Divide(new Constant(1.0), translate(child)),
                differentiateChild(child, variable)
        );
    }
}
