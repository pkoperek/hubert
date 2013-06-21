package pl.edu.agh.hubert.evolution.engine.jgap;

import org.jgap.gp.impl.GPGenotype;
import pl.edu.agh.hubert.evolution.engine.EvolutionIteration;

public class GPGenotypeDelegatingEvolutionIteration implements EvolutionIteration {

    @Override
    public void evolve(GPGenotype genotype) {
        genotype.evolve(1);
    }

    @Override
    public void shutdown() {
        // does nothing
    }
}