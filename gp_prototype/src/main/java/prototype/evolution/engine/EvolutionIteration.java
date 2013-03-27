package prototype.evolution.engine;

import org.jgap.gp.impl.GPGenotype;

public interface EvolutionIteration {
    int DEFAULT_THREADS_NUMBER = 1;

    void evolve(GPGenotype genotype);
}
