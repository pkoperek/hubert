package hubert.differentiation.symbolic.simplification;

import hubert.differentiation.symbolic.Function;

public interface SimplificationRule {
    boolean canSimplify(Function function);

    Function apply(Function function);
}
