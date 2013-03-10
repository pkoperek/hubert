package prototype.differentiation.simplification;

import prototype.differentiation.Function;

public interface SimplificationRule
{
    boolean canSimplify(Function function);

    Function apply(Function function);
}
