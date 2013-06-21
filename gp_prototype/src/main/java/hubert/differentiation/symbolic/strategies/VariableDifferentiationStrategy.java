package hubert.differentiation.symbolic.strategies;

import hubert.data.VariablesValuesContainer;
import hubert.differentiation.symbolic.Function;
import hubert.differentiation.symbolic.TreeNode;
import hubert.differentiation.symbolic.functions.*;
import hubert.differentiation.symbolic.tree.VariableTreeNode;

/**
 * User: koperek
 * Date: 26.02.13
 * Time: 22:46
 */
public class VariableDifferentiationStrategy extends DifferentiationStrategy {

    @Override
    protected Function differentiateSpecific(TreeNode treeNode, String differentiatingVariableName) {
        VariableTreeNode variableTreeNode = (VariableTreeNode) treeNode;
        VariablesValuesContainer valuesContainer = variableTreeNode.getValuesContainer();
        String variableName = variableTreeNode.getVariableName();
        String interdependentVariableName = variableTreeNode.getInterdependentVariableName();

        if (differentiatingVariableName.equals(variableName)) {
            return new Constant(1.0);
        }

        if (differentiatingVariableName.equals(interdependentVariableName)) {
            return new Divide(
                    new Subtract(new Variable(valuesContainer, variableName), new PreviousValueVariable(valuesContainer, variableName)),
                    new Subtract(new Variable(valuesContainer, interdependentVariableName), new PreviousValueVariable(valuesContainer, interdependentVariableName))
            );
        }

        return new Constant(0.0);
    }
}
