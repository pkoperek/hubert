package prototype.differentiation.strategies;

import org.junit.Before;
import org.junit.Test;
import prototype.differentiation.Function;
import prototype.differentiation.FunctionType;
import prototype.differentiation.tree.SimpleTreeNode;
import prototype.differentiation.TreeNode;
import prototype.differentiation.functions.Add;
import prototype.differentiation.functions.Constant;
import prototype.differentiation.tree.ConstantTreeNode;
import prototype.differentiation.tree.VariableTreeNode;

import static org.fest.assertions.Assertions.assertThat;
import static prototype.differentiation.TestTools.aVariableTreeNode;

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
