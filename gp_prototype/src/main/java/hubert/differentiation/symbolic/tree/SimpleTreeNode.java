package hubert.differentiation.symbolic.tree;

import hubert.differentiation.symbolic.FunctionType;
import hubert.differentiation.symbolic.TreeNode;

/**
 * User: koperek
 * Date: 28.02.13
 * Time: 23:09
 */
public class SimpleTreeNode extends TreeNode {

    private final FunctionType functionType;
    private final TreeNode[] children;

    public SimpleTreeNode(FunctionType functionType, TreeNode[] children) {
        this.functionType = functionType;
        this.children = children;
    }

    @Override
    public FunctionType getFunctionType() {
        return functionType;
    }

    @Override
    public TreeNode[] getChildren() {
        return children;
    }
}
