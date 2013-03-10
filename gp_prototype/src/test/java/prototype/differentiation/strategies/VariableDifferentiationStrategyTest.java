package prototype.differentiation.strategies;

import org.junit.Before;
import org.junit.Test;
import prototype.differentiation.Function;
import prototype.differentiation.TreeNode;
import prototype.differentiation.functions.Constant;
import prototype.differentiation.tree.VariableTreeNode;

import static org.fest.assertions.Assertions.assertThat;
import static prototype.differentiation.TestTools.aVariableTreeNode;

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
