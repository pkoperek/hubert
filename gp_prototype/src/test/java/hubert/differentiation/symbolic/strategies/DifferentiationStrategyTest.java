package hubert.differentiation.symbolic.strategies;

import org.junit.Before;
import org.junit.Test;
import hubert.differentiation.symbolic.Function;
import hubert.differentiation.symbolic.FunctionType;
import hubert.differentiation.symbolic.TreeNode;
import hubert.differentiation.symbolic.tree.SimpleTreeNode;

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
