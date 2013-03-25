package prototype.differentiation.symbolic.simplification;

import prototype.differentiation.symbolic.Function;
import prototype.differentiation.symbolic.functions.Constant;

/**
 * User: koperek
 * Date: 06.03.13
 * Time: 22:17
 */
public abstract class AbstractSimplificationRule implements SimplificationRule {
    private static final double THRESHOLD = 0.000001;

    protected boolean isConstantZero(Function function) {
        if (isConstant(function)) {
            Constant constant = (Constant) function;
            if (Math.abs(constant.getNumber()) < THRESHOLD) {
                return true;
            }
        }

        return false;
    }

    protected boolean isConstant(Function function) {
        return function instanceof Constant;
    }
}
