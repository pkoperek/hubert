package pl.edu.agh.hubert.differentiation.symbolic.strategies;

import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.hubert.differentiation.symbolic.Function;
import pl.edu.agh.hubert.differentiation.symbolic.FunctionType;
import pl.edu.agh.hubert.differentiation.symbolic.TreeNode;
import pl.edu.agh.hubert.differentiation.symbolic.functions.Add;
import pl.edu.agh.hubert.differentiation.symbolic.functions.Constant;
import pl.edu.agh.hubert.differentiation.symbolic.tree.ConstantTreeNode;
import pl.edu.agh.hubert.differentiation.symbolic.tree.SimpleTreeNode;

import static org.fest.assertions.Assertions.assertThat;
import static pl.edu.agh.hubert.differentiation.symbolic.TestTools.aVariableTreeNode;

/**
 * User: koperek
 * Date: 28.02.13
 * Time: 23:23
 */
public class AddDifferentiationStrategyTest {

    private AddDifferentiationStrategy strategy;

    @Before
    public void setUp() throws Exception {
        strategy = new AddDifferentiationStrategy();
    }

    @Test
    public void shouldDifferentiateSpecific() throws Exception {

        // Given
        TreeNode variablePlusConstant = new SimpleTreeNode(
                FunctionType.ADD,
                new TreeNode[]{
                        aVariableTreeNode("x"),
                        new ConstantTreeNode(10.0)});


        // When
        Function result = strategy.differentiate(variablePlusConstant, "x");

        System.out.println(result);

        // Then
        assertThat(result).isInstanceOf(Add.class);
        assertThat(((Add) result).getLeftOperand()).isInstanceOf(Constant.class);
        assertThat(((Add) result).getRightOperand()).isInstanceOf(Constant.class);
        System.out.println(result.evaluate());
    }
}
