package pl.edu.agh.hubert.differentiation.symbolic.simplification.rules;

import pl.edu.agh.hubert.differentiation.symbolic.Function;
import pl.edu.agh.hubert.differentiation.symbolic.functions.Constant;
import pl.edu.agh.hubert.differentiation.symbolic.functions.Multiply;
import pl.edu.agh.hubert.differentiation.symbolic.simplification.AbstractSimplificationRule;

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
