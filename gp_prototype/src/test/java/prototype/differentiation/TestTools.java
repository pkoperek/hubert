package prototype.differentiation;

import prototype.differentiation.functions.Constant;
import prototype.differentiation.functions.DoubleOperandFunction;
import prototype.differentiation.functions.SingleOperandFunction;
import prototype.differentiation.functions.Variable;
import prototype.differentiation.tree.VariableTreeNode;

import static org.fest.assertions.Assertions.assertThat;

public class TestTools {
    public static void assertTheSameAs(Function expected, Function actual) {

        assertThat(actual).isInstanceOf(expected.getClass());

        if (expected instanceof DoubleOperandFunction) {
            DoubleOperandFunction doubleOperandFunctionToCompare = (DoubleOperandFunction) actual;
            DoubleOperandFunction doubleOperandFunctionTemplate = (DoubleOperandFunction) expected;
            assertTheSameAs(doubleOperandFunctionTemplate.getLeftOperand(), doubleOperandFunctionToCompare.getLeftOperand());
            assertTheSameAs(doubleOperandFunctionTemplate.getRightOperand(), doubleOperandFunctionToCompare.getRightOperand());
        }

        if (expected instanceof SingleOperandFunction) {
            SingleOperandFunction singleOperandFunctionToCompare = (SingleOperandFunction) actual;
            SingleOperandFunction singleOperandFunctionTemplate = (SingleOperandFunction) expected;
            assertTheSameAs(singleOperandFunctionTemplate.getOperand(), singleOperandFunctionToCompare.getOperand());
        }

        if (expected instanceof Variable) {
            Variable templateVariable = (Variable) expected;
            Variable toCompareVariable = (Variable) actual;
            assertThat(toCompareVariable.getVariableName()).isEqualTo(templateVariable.getVariableName());
        }

        if (expected instanceof Constant) {
            Constant templateConstant = (Constant) expected;
            Constant toCompareConstant = (Constant) actual;
            assertThat(toCompareConstant.getNumber()).isEqualTo(templateConstant.getNumber());
        }
    }

    public static Variable aVariable(String variableName) {
        return new Variable(null, variableName);
    }

    public static VariableTreeNode aVariableTreeNode(String variableName) {
        return new VariableTreeNode(null, null, variableName);
    }
}