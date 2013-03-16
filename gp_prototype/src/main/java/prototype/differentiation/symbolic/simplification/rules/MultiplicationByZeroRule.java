package prototype.differentiation.symbolic.simplification.rules;

import prototype.differentiation.symbolic.Function;
import prototype.differentiation.symbolic.functions.Constant;
import prototype.differentiation.symbolic.functions.Multiply;
import prototype.differentiation.symbolic.simplification.AbstractSimplificationRule;

public class MultiplicationByZeroRule extends AbstractSimplificationRule {

    @Override
    public boolean canSimplify(Function function) {
        if (function instanceof Multiply) {
            Multiply multiply = (Multiply) function;
            if (isConstantZero(multiply.getLeftOperand()) || isConstantZero(multiply.getRightOperand())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Function apply(Function function) {
        return new Constant(0.0);
    }
}
