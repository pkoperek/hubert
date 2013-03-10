package prototype.differentiation.simplification.rules;

import prototype.differentiation.Function;
import prototype.differentiation.functions.Constant;
import prototype.differentiation.functions.Multiply;
import prototype.differentiation.simplification.SimplificationRule;

public class ConstantMultiplicationRule implements SimplificationRule
{
    @Override
    public boolean canSimplify(Function function)
    {
        if (function instanceof Multiply)
        {
            Multiply multiply = (Multiply) function;

            if (multiply.getLeftOperand() instanceof Constant &&
                multiply.getRightOperand() instanceof Constant)
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public Function apply(Function function)
    {
        return new Constant(function.evaluate());
    }
}
