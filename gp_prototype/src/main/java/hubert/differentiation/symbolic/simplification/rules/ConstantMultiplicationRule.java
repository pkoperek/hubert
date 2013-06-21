package hubert.differentiation.symbolic.simplification.rules;

import hubert.differentiation.symbolic.Function;
import hubert.differentiation.symbolic.functions.Constant;
import hubert.differentiation.symbolic.functions.Multiply;
import hubert.differentiation.symbolic.simplification.SimplificationRule;

public class ConstantMultiplicationRule implements SimplificationRule {
    @Override
    public boolean canSimplify(Function function) {
        if (function instanceof Multiply) {
            Multiply multiply = (Multiply) function;

            if (multiply.getLeftOperand() instanceof Constant &&
                    multiply.getRightOperand() instanceof Constant) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Function apply(Function function) {
        return new Constant(function.evaluate());
    }
}
