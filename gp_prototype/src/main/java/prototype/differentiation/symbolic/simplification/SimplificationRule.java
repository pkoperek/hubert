package prototype.differentiation.symbolic.simplification;

import prototype.differentiation.symbolic.Function;

public interface SimplificationRule {
    boolean canSimplify(Function function);

    Function apply(Function function);
}
