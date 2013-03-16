package prototype.differentiation.symbolic.simplification.rules;

import prototype.differentiation.symbolic.Function;
import prototype.differentiation.symbolic.functions.Add;
import prototype.differentiation.symbolic.simplification.AbstractSimplificationRule;

public class AddingZeroRule extends AbstractSimplificationRule {
    @Override
    public boolean canSimplify(Function function) {
        if (function instanceof Add) {
            Add add = (Add) function;
            if (isConstantZero(add.getLeftOperand()) || isConstantZero(add.getRightOperand())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Function apply(Function function) {
        Add add = (Add) function;
        return isConstantZero(add.getLeftOperand()) ? add.getRightOperand() : add.getLeftOperand();
    }
}
