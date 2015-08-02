package pl.edu.agh.hubert.evolution.engine;

import org.jgap.gp.impl.GPGenotype;

/**
 * User: koperek
 * Date: 16.03.13
 * Time: 13:47
 */
public interface EvolutionCycleAware {
    void handleBeforeEvolution(GPGenotype genotype);

    void handleAfterEvolution(GPGenotype genotype);
}