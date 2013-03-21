package prototype.evolution.engine.jgap;

import org.jgap.gp.impl.GPGenotype;
import prototype.evolution.engine.EvolutionIteration;

public class GPGenotypeDelegatingEvolutionIteration implements EvolutionIteration {

    @Override
    public void evolve(GPGenotype genotype) {
        genotype.evolve(1);
    }
}