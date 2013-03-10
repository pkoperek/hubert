package prototype.differentiation.strategies.elementary;

import org.junit.Before;
import org.junit.Test;
import prototype.differentiation.*;
import prototype.differentiation.functions.*;
import prototype.differentiation.AbstractDifferentiationStrategyTest;
import prototype.differentiation.tree.SimpleTreeNode;
import prototype.differentiation.tree.VariableTreeNode;

import static junit.framework.Assert.fail;
import static org.fest.assertions.Assertions.assertThat;
import static prototype.differentiation.TestTools.aVariable;
import static prototype.differentiation.TestTools.aVariableTreeNode;

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
