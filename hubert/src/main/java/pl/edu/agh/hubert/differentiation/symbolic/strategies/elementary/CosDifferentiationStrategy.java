package pl.edu.agh.hubert.differentiation.symbolic.strategies.elementary;

import pl.edu.agh.hubert.differentiation.symbolic.Function;
import pl.edu.agh.hubert.differentiation.symbolic.TreeNode;
import pl.edu.agh.hubert.differentiation.symbolic.functions.Constant;
import pl.edu.agh.hubert.differentiation.symbolic.functions.Multiply;
import pl.edu.agh.hubert.differentiation.symbolic.functions.Sin;
import pl.edu.agh.hubert.differentiation.symbolic.strategies.DifferentiationStrategy;

/**
 * User: koperek
 * Date: 26.02.13
 * Time: 22:39
 */
public class CosDifferentiationStrategy extends DifferentiationStrategy {

    @Override
    protected Function differentiateSpecific(TreeNode treeNode, String variable) {
        TreeNode internalFunctionNode = treeNode.getChildren()[0];
        Function internalFunction = translate(internalFunctionNode);
        return new Multiply(new Multiply(new Constant(-1.0), new Sin(internalFunction)), internalFunctionNode.differentiate(variable));
    }
}
