package prototype.evolution.fitness.parsimony;

import org.jgap.gp.impl.ProgramChromosome;

/**
 * User: koperek
 * Date: 27.03.13
 * Time: 16:44
 */
public interface ParsimonyPressure {
    double pressure(double fitness, ProgramChromosome a_subject);
}
