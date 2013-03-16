package prototype.differentiation.symbolic.simplification.rules;

import prototype.differentiation.symbolic.Function;
import prototype.differentiation.symbolic.functions.DoubleOperandFunction;
import prototype.differentiation.symbolic.functions.SingleOperandFunction;
import prototype.differentiation.symbolic.simplification.AbstractSimplificationRule;

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
