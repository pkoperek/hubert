package hubert.differentiation.symbolic.simplification.rules;

import hubert.differentiation.symbolic.Function;
import hubert.differentiation.symbolic.functions.Constant;
import hubert.differentiation.symbolic.functions.Multiply;
import hubert.differentiation.symbolic.simplification.AbstractSimplificationRule;

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
