package prototype.differentiation.symbolic.strategies;

import org.junit.Before;
import org.junit.Test;
import prototype.differentiation.symbolic.Function;
import prototype.differentiation.symbolic.TreeNode;
import prototype.differentiation.symbolic.functions.Constant;

import static org.fest.assertions.Assertions.assertThat;
import static prototype.differentiation.symbolic.TestTools.aVariableTreeNode;

/**
 * User: koperek
 * Date: 28.02.13
 * Time: 23:12
 */
public class VariableDifferentiationStrategyTest {

    private VariableDifferentiationStrategy strategy;

    @Before
    public void setUp() throws Exception {
        strategy = new VariableDifferentiationStrategy();
    }

    @Test
    public void shouldDifferentiateMultiLetterVariableName() throws Exception {

        // Given
        String variableName = "abcdefghij";
        TreeNode treeNode = aVariableTreeNode(variableName);

        // When
        Function result = strategy.differentiate(treeNode, variableName);

        // Then
        assertThat(result).isInstanceOf(Constant.class);

        Constant constant = (Constant) result;
        assertThat(constant.getNumber()).isEqualTo(1.0);

    }

    @Test
    public void shouldDifferentiateSpecific() throws Exception {

        // Given
        TreeNode treeNode = aVariableTreeNode("x");

        // When
        Function result = strategy.differentiate(treeNode, "x");

        // Then
        assertThat(result).isInstanceOf(Constant.class);

        Constant constant = (Constant) result;
        assertThat(constant.getNumber()).isEqualTo(1.0);
    }
}
