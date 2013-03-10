package prototype.differentiation.simplification.rules;

import prototype.differentiation.Function;
import prototype.differentiation.functions.Constant;
import prototype.differentiation.functions.Multiply;
import prototype.differentiation.simplification.AbstractSimplificationRule;
import prototype.differentiation.simplification.SimplificationRule;

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
