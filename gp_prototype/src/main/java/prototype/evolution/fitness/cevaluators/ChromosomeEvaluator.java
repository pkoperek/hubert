package prototype.evolution.fitness.cevaluators;

import org.jgap.gp.impl.ProgramChromosome;

/**
 * User: koperek
 * Date: 27.03.13
 * Time: 16:23
 */
public interface ChromosomeEvaluator {
    double evaluate(ProgramChromosome chromosome);
}
