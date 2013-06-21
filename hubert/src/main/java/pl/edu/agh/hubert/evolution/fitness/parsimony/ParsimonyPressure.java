package pl.edu.agh.hubert.evolution.fitness.parsimony;

import org.jgap.gp.impl.ProgramChromosome;
import pl.edu.agh.hubert.evolution.engine.EvolutionCycleAware;

/**
 * User: koperek
 * Date: 27.03.13
 * Time: 16:44
 */
public interface ParsimonyPressure extends EvolutionCycleAware {
    double pressure(double fitness, ProgramChromosome a_subject);
}
