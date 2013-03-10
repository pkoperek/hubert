package prototype.differentiation.strategies;

import org.junit.Before;
import org.junit.Test;
import prototype.differentiation.Function;
import prototype.differentiation.FunctionType;
import prototype.differentiation.tree.SimpleTreeNode;
import prototype.differentiation.TreeNode;
import prototype.differentiation.functions.Constant;
import prototype.differentiation.functions.Subtract;
import prototype.differentiation.tree.ConstantTreeNode;
import prototype.differentiation.tree.VariableTreeNode;

import static org.fest.assertions.Assertions.assertThat;
import static prototype.differentiation.TestTools.aVariableTreeNode;

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
