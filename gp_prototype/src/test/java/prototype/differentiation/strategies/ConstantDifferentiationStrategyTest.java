package prototype.differentiation.strategies;

import org.junit.Before;
import org.junit.Test;
import prototype.differentiation.Function;
import prototype.differentiation.TreeNode;
import prototype.differentiation.functions.Constant;
import prototype.differentiation.tree.ConstantTreeNode;

import static org.fest.assertions.Assertions.assertThat;

/**
 * User: koperek
 * Date: 28.02.13
 * Time: 23:19
 */
public class ConstantDifferentiationStrategyTest {

    private ConstantDifferentiationStrategy strategy;

    @Before
    public void setUp() throws Exception {
        strategy = new ConstantDifferentiationStrategy();
    }

    @Test
    public void shouldDifferentiateSpecific() throws Exception {

        // Given
        TreeNode treeNode = new ConstantTreeNode(10.0);

        // When
        Function result = strategy.differentiate(treeNode, "x");

        // Then
        assertThat(result).isInstanceOf(Constant.class);
        assertThat(((Constant)result).getNumber()).isEqualTo(0.0);
    }
}
