package hubert.evolution.engine.common;

import org.jgap.gp.IGPProgram;
import hubert.evolution.engine.Mutator;

public class EmptyMutator implements Mutator {
    @Override
    public IGPProgram mutate(IGPProgram toMutate) {
        return toMutate;
    }
}
