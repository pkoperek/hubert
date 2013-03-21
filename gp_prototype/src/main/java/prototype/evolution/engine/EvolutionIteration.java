package prototype.evolution.engine;

import org.jgap.gp.impl.GPGenotype;

public interface EvolutionIteration {
    void evolve(GPGenotype genotype);
}
