package pl.edu.agh.hubert.differentiation.symbolic.strategies;

import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.hubert.differentiation.symbolic.Function;
import pl.edu.agh.hubert.differentiation.symbolic.FunctionType;
import pl.edu.agh.hubert.differentiation.symbolic.TreeNode;
import pl.edu.agh.hubert.differentiation.symbolic.functions.Constant;
import pl.edu.agh.hubert.differentiation.symbolic.functions.Subtract;
import pl.edu.agh.hubert.differentiation.symbolic.tree.ConstantTreeNode;
import pl.edu.agh.hubert.differentiation.symbolic.tree.SimpleTreeNode;

import static org.fest.assertions.Assertions.assertThat;
import static pl.edu.agh.hubert.differentiation.symbolic.TestTools.aVariableTreeNode;

/**
 * User: koperek
 * Date: 28.02.13
 * Time: 23:29
 */
public class SubtractDifferentiationStrategyTest {
    private SubtractDifferentiationStrategy strategy;

    @Before
    public void setUp() throws Exception {
        strategy = new SubtractDifferentiationStrategy();
    }

    @Test
    public void shouldDifferentiateSpecific() throws Exception {

        // Given
        TreeNode variablePlusConstant = new SimpleTreeNode(
                FunctionType.SUBTRACT,
                new TreeNode[]{
                        aVariableTreeNode("x"),
                        new ConstantTreeNode(10.0)});


        // When
        Function result = strategy.differentiate(variablePlusConstant, "x");

        System.out.println(result);

        // Then
        assertThat(result).isInstanceOf(Subtract.class);
        assertThat(((Subtract) result).getLeftOperand()).isInstanceOf(Constant.class);
        assertThat(((Subtract) result).getRightOperand()).isInstanceOf(Constant.class);
        assertThat(result.evaluate()).isEqualTo(1.0);
        System.out.println(result.evaluate());
    }
}
