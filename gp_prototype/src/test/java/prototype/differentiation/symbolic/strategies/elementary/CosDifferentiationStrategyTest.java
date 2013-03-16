package prototype.differentiation.symbolic.strategies.elementary;

import org.junit.Before;
import org.junit.Test;
import prototype.differentiation.symbolic.AbstractDifferentiationStrategyTest;
import prototype.differentiation.symbolic.Function;
import prototype.differentiation.symbolic.FunctionType;
import prototype.differentiation.symbolic.TreeNode;
import prototype.differentiation.symbolic.functions.Add;
import prototype.differentiation.symbolic.functions.Constant;
import prototype.differentiation.symbolic.functions.Multiply;
import prototype.differentiation.symbolic.functions.Sin;
import prototype.differentiation.symbolic.tree.SimpleTreeNode;

import static prototype.differentiation.symbolic.TestTools.aVariable;
import static prototype.differentiation.symbolic.TestTools.aVariableTreeNode;

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
