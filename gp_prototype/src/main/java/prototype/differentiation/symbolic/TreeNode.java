package prototype.differentiation.symbolic;

import prototype.differentiation.symbolic.strategies.DifferentiationStrategy;

/**
 * User: koperek
 * Date: 25.02.13
 * Time: 21:11
 */
public abstract class TreeNode {

    public abstract FunctionType getFunctionType();

    public abstract TreeNode[] getChildren();

    public Function differentiate(String variable) {
        FunctionType functionType = getFunctionType();
        DifferentiationStrategy differentiationStrategy = functionType.getDifferentiationStrategy();
        return differentiationStrategy.differentiate(this, variable);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getFunctionType());
        stringBuilder.append(" {");
        for (TreeNode treeNode : getChildren()) {
            stringBuilder.append(treeNode.toString());
            stringBuilder.append(" ");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
