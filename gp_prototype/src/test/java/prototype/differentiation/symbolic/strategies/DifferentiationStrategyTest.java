package prototype.differentiation.symbolic.strategies;

import org.junit.Before;
import org.junit.Test;
import prototype.differentiation.symbolic.Function;
import prototype.differentiation.symbolic.FunctionType;
import prototype.differentiation.symbolic.TreeNode;
import prototype.differentiation.symbolic.tree.SimpleTreeNode;

/**
 * User: koperek
 * Date: 28.02.13
 * Time: 23:33
 */
public class DifferentiationStrategyTest {

    private DifferentiationStrategy strategy;

    @Before
    public void setUp() throws Exception {
        strategy = new MockDifferentiationStrategy();
    }

    @Test
    public void shouldDifferentiate() throws Exception {

        // Given
        TreeNode treeNode = new SimpleTreeNode(FunctionType.VARIABLE, new TreeNode[0]);

        // When
        strategy.differentiate(treeNode, "x");

        // Then
        // no exception

    }

    class MockDifferentiationStrategy extends DifferentiationStrategy {

        @Override
        protected Function differentiateSpecific(TreeNode treeNode, String variable) {
            return null;
        }
    }
}