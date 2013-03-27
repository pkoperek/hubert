package prototype.differentiation.symbolic.strategies;

import org.junit.Before;
import org.junit.Test;
import prototype.data.MapVariablesValuesContainer;
import prototype.data.VariablesValuesContainer;
import prototype.differentiation.symbolic.Function;
import prototype.differentiation.symbolic.FunctionType;
import prototype.differentiation.symbolic.TreeNode;
import prototype.differentiation.symbolic.TreeNodeToFunctionTranslator;
import prototype.differentiation.symbolic.functions.Add;
import prototype.differentiation.symbolic.functions.Multiply;
import prototype.differentiation.symbolic.tree.ConstantTreeNode;
import prototype.differentiation.symbolic.tree.SimpleTreeNode;
import prototype.differentiation.symbolic.tree.VariableTreeNode;

import static org.fest.assertions.Assertions.assertThat;

/**
 * User: koperek
 * Date: 01.03.13
 * Time: 00:00
 */
public class MultiplyDifferentiationStrategyTest {

    private static final TreeNodeToFunctionTranslator TRANSLATOR = new TreeNodeToFunctionTranslator();
    private MultiplyDifferentiationStrategy strategy;

    @Before
    public void setUp() throws Exception {
        strategy = new MultiplyDifferentiationStrategy();
    }

    @Test
    public void shouldDifferentiateVariableConstantMultiplication() throws Exception {

        // Given
        VariablesValuesContainer values = new MapVariablesValuesContainer();
        values.setVariableValue("x", 4);
        TreeNode treeNode = new SimpleTreeNode(
                FunctionType.MULTIPLY,
                new TreeNode[]{
                        new VariableTreeNode(values, "x"),
                        new ConstantTreeNode(20.0),
                });

        System.out.println(TRANSLATOR.translate(treeNode).toString());

        // When
        Function result = strategy.differentiate(treeNode, "x");

        // Then
        System.out.println(result);
        assertThat(result.evaluate()).isEqualTo(20.0);
        assertAMMStructure(result);

    }

    @Test
    public void shouldDifferentiateConstantsMultiplication() throws Exception {

        // Given
        TreeNode treeNode = new SimpleTreeNode(
                FunctionType.MULTIPLY,
                new TreeNode[]{
                        new ConstantTreeNode(10.0),
                        new ConstantTreeNode(20.0),
                });

        System.out.println(TRANSLATOR.translate(treeNode).toString());

        // When
        Function result = strategy.differentiate(treeNode, "x");

        // Then
        System.out.println(result);
        assertThat(result.evaluate()).isEqualTo(0.0);
        assertAMMStructure(result);
    }

    private void assertAMMStructure(Function result) {
        assertThat(result).isInstanceOf(Add.class);
        Add addFunctionResult = (Add) result;
        assertThat(addFunctionResult.getLeftOperand()).isInstanceOf(Multiply.class);
        assertThat(addFunctionResult.getRightOperand()).isInstanceOf(Multiply.class);
    }
}
