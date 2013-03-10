package prototype.differentiation.tree;

import prototype.differentiation.FunctionType;
import prototype.differentiation.TreeNode;

/**
 * User: koperek
 * Date: 28.02.13
 * Time: 23:04
 */
public class ConstantTreeNode extends TreeNode {

    private Number number;

    public ConstantTreeNode(Number number) {
        this.number = number;
    }

    public Number getNumber() {
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
