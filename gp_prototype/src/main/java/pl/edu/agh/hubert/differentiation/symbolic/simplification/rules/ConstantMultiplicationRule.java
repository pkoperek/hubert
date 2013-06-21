package pl.edu.agh.hubert.differentiation.symbolic.simplification.rules;

import pl.edu.agh.hubert.differentiation.symbolic.Function;
import pl.edu.agh.hubert.differentiation.symbolic.functions.Constant;
import pl.edu.agh.hubert.differentiation.symbolic.functions.Multiply;
import pl.edu.agh.hubert.differentiation.symbolic.simplification.SimplificationRule;

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
