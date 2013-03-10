package prototype.differentiation.strategies;

import org.junit.Before;
import org.junit.Test;
import prototype.VariablesValues;
import prototype.differentiation.*;
import prototype.differentiation.functions.Add;
import prototype.differentiation.functions.Multiply;
import prototype.differentiation.tree.ConstantTreeNode;
import prototype.differentiation.tree.SimpleTreeNode;
import prototype.differentiation.tree.VariableTreeNode;

import static org.fest.assertions.Assertions.assertThat;
import static prototype.differentiation.TestTools.aVariableTreeNode;

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
        VariablesValues values = new VariablesValues();
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
