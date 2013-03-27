package prototype.evolution.fitness.parsimony;

import org.jgap.gp.impl.ProgramChromosome;
import prototype.evolution.engine.EvolutionCycleAware;

/**
 * User: koperek
 * Date: 27.03.13
 * Time: 16:44
 */
public interface ParsimonyPressure extends EvolutionCycleAware {
    double pressure(double fitness, ProgramChromosome a_subject);
}
