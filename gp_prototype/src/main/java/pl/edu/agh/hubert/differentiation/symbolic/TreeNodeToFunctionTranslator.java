package pl.edu.agh.hubert.differentiation.symbolic;

import pl.edu.agh.hubert.differentiation.symbolic.functions.*;
import pl.edu.agh.hubert.differentiation.symbolic.tree.ConstantTreeNode;
import pl.edu.agh.hubert.differentiation.symbolic.tree.VariableTreeNode;

/**
 * User: koperek
 * Date: 28.02.13
 * Time: 23:51
 */
public class TreeNodeToFunctionTranslator {

    public Function translate(TreeNode treeNode) {
        TreeNode[] children = treeNode.getChildren();
        switch (treeNode.getFunctionType()) {
            case POW:
                return new Pow(translate(children[0]), translate(children[1]));
            case ARCTAN:
                return new ArcTan(translate(children[0]));
            case ARCCOS:
                return new ArcCos(translate(children[0]));
            case ARCSIN:
                return new ArcSin(translate(children[0]));
            case TAN:
                return new Tan(translate(children[0]));
            case LN:
                return new Ln(translate(children[0]));
            case SIN:
                return new Sin(translate(children[0]));
            case COS:
                return new Cos(translate(children[0]));
            case EXP:
                return new Exp(translate(children[0]));
            case ADD:
                return new Add(translate(children[0]), translate(children[1]));
            case SUBTRACT:
                return new Subtract(translate(children[0]), translate(children[1]));
            case MULTIPLY:
                return new Multiply(translate(children[0]), translate(children[1]));
            case DIVIDE:
                return new Divide(translate(children[0]), translate(children[1]));
            case CONSTANT:
                return new Constant(((ConstantTreeNode) treeNode).getNumber());
            case VARIABLE:
                VariableTreeNode variableTreeNode = (VariableTreeNode) treeNode;
                return new Variable(variableTreeNode.getValuesContainer(), variableTreeNode.getVariableName());
        }

        throw new IllegalArgumentException("Not supported function type: " + treeNode.getFunctionType());
    }

}
