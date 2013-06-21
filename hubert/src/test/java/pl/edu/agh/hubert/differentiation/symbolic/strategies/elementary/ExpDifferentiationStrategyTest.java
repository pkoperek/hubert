package pl.edu.agh.hubert.differentiation.symbolic.strategies.elementary;

import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.hubert.differentiation.symbolic.AbstractDifferentiationStrategyTest;
import pl.edu.agh.hubert.differentiation.symbolic.Function;
import pl.edu.agh.hubert.differentiation.symbolic.FunctionType;
import pl.edu.agh.hubert.differentiation.symbolic.TreeNode;
import pl.edu.agh.hubert.differentiation.symbolic.functions.*;
import pl.edu.agh.hubert.differentiation.symbolic.tree.SimpleTreeNode;

import static pl.edu.agh.hubert.differentiation.symbolic.TestTools.aVariable;
import static pl.edu.agh.hubert.differentiation.symbolic.TestTools.aVariableTreeNode;

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
