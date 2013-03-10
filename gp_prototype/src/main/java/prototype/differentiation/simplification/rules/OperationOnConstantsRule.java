package prototype.differentiation.simplification.rules;

import prototype.differentiation.Function;
import prototype.differentiation.functions.DoubleOperandFunction;
import prototype.differentiation.functions.SingleOperandFunction;
import prototype.differentiation.simplification.AbstractSimplificationRule;
import prototype.differentiation.simplification.SimplificationRule;

/**
 * User: koperek
 * Date: 06.03.13
 * Time: 22:13
 */
public class OperationOnConstantsRule extends AbstractSimplificationRule {


    @Override
    public boolean canSimplify(Function function) {
        if (function instanceof SingleOperandFunction) {
            SingleOperandFunction singleOperandFunction = (SingleOperandFunction) function;
            return isConstant(singleOperandFunction);
        }

        if (function instanceof DoubleOperandFunction) {
            DoubleOperandFunction doubleOperandFunction = (DoubleOperandFunction) function;
            return isConstant(doubleOperandFunction.getRightOperand()) && isConstant(doubleOperandFunction.getLeftOperand());
        }

        return false;
    }

    @Override
    public Function apply(Function function) {

        return function;
    }
}
