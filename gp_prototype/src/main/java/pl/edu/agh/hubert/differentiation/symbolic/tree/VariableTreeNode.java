package pl.edu.agh.hubert.differentiation.symbolic.tree;

import pl.edu.agh.hubert.data.VariablesValuesContainer;
import pl.edu.agh.hubert.differentiation.symbolic.FunctionType;
import pl.edu.agh.hubert.differentiation.symbolic.TreeNode;

/**
 * User: koperek
 * Date: 28.02.13
 * Time: 21:46
 */
public class VariableTreeNode extends TreeNode {

    public static final String EMPTY_NAME = "";
    private String variableName;
    private final String interdependentVariableName;
    private VariablesValuesContainer valuesContainer;

    public VariableTreeNode(VariablesValuesContainer valuesContainer, String variableName) {
        this(valuesContainer, variableName, EMPTY_NAME);
    }

    public VariableTreeNode(VariablesValuesContainer valuesContainer, String variableName, String interdependentVariableName) {
        this.valuesContainer = valuesContainer;
        this.variableName = variableName;
        this.interdependentVariableName = interdependentVariableName;
    }

    public String getVariableName() {
        return variableName;
    }

    @Override
    public FunctionType getFunctionType() {
        return FunctionType.VARIABLE;
    }

    @Override
    public TreeNode[] getChildren() {
        return new TreeNode[0];
    }

    @Override
    public String toString() {
        return "VAR: " + variableName;
    }

    public VariablesValuesContainer getValuesContainer() {
        return valuesContainer;
    }

    public String getInterdependentVariableName() {
        return interdependentVariableName;
    }
}


