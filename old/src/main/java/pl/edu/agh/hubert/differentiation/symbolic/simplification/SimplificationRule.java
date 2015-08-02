package pl.edu.agh.hubert.differentiation.symbolic.simplification;

import pl.edu.agh.hubert.differentiation.symbolic.Function;

public interface SimplificationRule {
    boolean canSimplify(Function function);

    Function apply(Function function);
}
