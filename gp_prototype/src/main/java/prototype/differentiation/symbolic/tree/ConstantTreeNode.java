package prototype.differentiation.symbolic.tree;

import prototype.differentiation.symbolic.FunctionType;
import prototype.differentiation.symbolic.TreeNode;

/**
 * User: koperek
 * Date: 28.02.13
 * Time: 23:04
 */
public class ConstantTreeNode extends TreeNode {

    private double number;

    public ConstantTreeNode(double number) {
        this.number = number;
    }

    public double getNumber() {
        return number;
    }

    @Override
    public FunctionType getFunctionType() {
        return FunctionType.CONSTANT;
    }

    @Override
    public TreeNode[] getChildren() {
        return new TreeNode[0];
    }

    @Override
    public String toString() {
        return "CONSTANT: " + number;
    }
}
