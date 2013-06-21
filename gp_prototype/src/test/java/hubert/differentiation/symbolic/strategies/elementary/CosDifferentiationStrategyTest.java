package hubert.differentiation.symbolic.strategies.elementary;

import org.junit.Before;
import org.junit.Test;
import hubert.differentiation.symbolic.AbstractDifferentiationStrategyTest;
import hubert.differentiation.symbolic.Function;
import hubert.differentiation.symbolic.FunctionType;
import hubert.differentiation.symbolic.TreeNode;
import hubert.differentiation.symbolic.functions.Add;
import hubert.differentiation.symbolic.functions.Constant;
import hubert.differentiation.symbolic.functions.Multiply;
import hubert.differentiation.symbolic.functions.Sin;
import hubert.differentiation.symbolic.tree.SimpleTreeNode;

import static hubert.differentiation.symbolic.TestTools.aVariable;
import static hubert.differentiation.symbolic.TestTools.aVariableTreeNode;

/**
 * User: koperek
 * Date: 01.03.13
 * Time: 00:18
 */
public class CosDifferentiationStrategyTest extends AbstractDifferentiationStrategyTest {

    @Before
    public void setUp() throws Exception {
        strategy = new CosDifferentiationStrategy();
    }

    @Test
    public void shouldUseChainRule() throws Exception {

        // cos (x+y)
        TreeNode addNode = new SimpleTreeNode(FunctionType.ADD, new TreeNode[]{aVariableTreeNode("x"), aVariableTreeNode("y")});
        TreeNode cosNode = new SimpleTreeNode(FunctionType.COS, new TreeNode[]{addNode});

        Function template = new Multiply(
                new Multiply(
                        new Constant(-1.0),
                        new Sin(
                                new Add(
                                        aVariable("x"),
                                        aVariable("y")
                                )
                        )
                ),
                new Add(
                        new Constant(1.0),
                        new Constant(0.0)
                )
        );

        checkDifferentiation(cosNode, template, "x");
    }

    @Test
    public void shouldDifferentiateSpecific() throws Exception {
        Function template = new Multiply(new Multiply(new Constant(-1.0), new Sin(aVariable("x"))), new Constant(1.0));
        TreeNode treeNode = new SimpleTreeNode(FunctionType.COS, new TreeNode[]{aVariableTreeNode("x")});

        checkDifferentiation(treeNode, template, "x");
    }

}
