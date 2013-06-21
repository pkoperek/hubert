package pl.edu.agh.hubert.evolution.engine.common;

import org.jgap.gp.IGPProgram;
import pl.edu.agh.hubert.evolution.engine.Mutator;

public class EmptyMutator implements Mutator {
    @Override
    public IGPProgram mutate(IGPProgram toMutate) {
        return toMutate;
    }
}
