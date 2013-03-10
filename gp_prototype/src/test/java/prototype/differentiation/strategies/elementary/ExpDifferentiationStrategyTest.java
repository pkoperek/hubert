package prototype.differentiation.strategies.elementary;

import org.junit.Before;
import org.junit.Test;
import prototype.differentiation.*;
import prototype.differentiation.functions.*;
import prototype.differentiation.tree.SimpleTreeNode;
import prototype.differentiation.tree.VariableTreeNode;

import static prototype.differentiation.TestTools.aVariable;
import static prototype.differentiation.TestTools.aVariable;
import static prototype.differentiation.TestTools.aVariableTreeNode;

/**
 * User: koperek
 * Date: 01.03.13
 * Time: 22:35
 */
public class ExpDifferentiationStrategyTest extends AbstractDifferentiationStrategyTest {

    @Before
    public void setUp() throws Exception {
        strategy = new ExpDifferentiationStrategy();
    }

    @Test
    public void shouldDifferentiateEx() throws Exception {
        // e^x
        Function expected = new Multiply(new Exp(aVariable("x")), new Constant(1.0));
        TreeNode toDifferentiate = new SimpleTreeNode(FunctionType.EXP, new TreeNode[]{aVariableTreeNode("x")});

        checkDifferentiation(toDifferentiate, expected, "x");
    }

    @Test
    public void shouldDifferentiateExSinX() throws Exception {
        // e^sin(x)
        Function expected = new Multiply(
                new Exp(
                        new Sin(
                                aVariable("x")
                        )
                ),
                new Multiply(
                        new Cos(aVariable("x")),
                        new Constant(1.0)
                )
        );

        TreeNode sinNode = new SimpleTreeNode(FunctionType.SIN, new TreeNode[]{aVariableTreeNode("x")});
        TreeNode toDifferentiate = new SimpleTreeNode(
                FunctionType.EXP,
                new TreeNode[]{sinNode});

        checkDifferentiation(toDifferentiate, expected, "x");
    }


}
