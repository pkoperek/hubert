package pl.edu.agh.hubert.evolution.engine;

import org.jgap.gp.IGPProgram;

public interface Mutator {
    IGPProgram mutate(IGPProgram toMutate);
}
