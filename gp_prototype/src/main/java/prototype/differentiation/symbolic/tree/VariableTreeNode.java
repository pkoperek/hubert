package prototype.differentiation.symbolic.tree;

import prototype.data.VariablesValues;
import prototype.differentiation.symbolic.FunctionType;
import prototype.differentiation.symbolic.TreeNode;

/**
 * User: koperek
 * Date: 28.02.13
 * Time: 21:46
 */
public class VariableTreeNode extends TreeNode {

    public static final String EMPTY_NAME = "";
    private String variableName;
    private final String interdependentVariableName;
    private VariablesValues valuesContainer;

    public VariableTreeNode(VariablesValues valuesContainer, String variableName) {
        this(valuesContainer, variableName, EMPTY_NAME);
    }

    public VariableTreeNode(VariablesValues valuesContainer, String variableName, String interdependentVariableName) {
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

    public VariablesValues getValuesContainer() {
        return valuesContainer;
    }

    public String getInterdependentVariableName() {
        return interdependentVariableName;
    }
}


