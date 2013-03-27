package prototype.evolution.fitness.processors;

import org.jgap.gp.impl.ProgramChromosome;
import prototype.data.VariablesValuesContainer;
import prototype.differentiation.symbolic.Function;

/**
 * User: koperek
 * Date: 27.03.13
 * Time: 18:00
 */
public interface ChromosomeProcessor<T> {
    Function preprocessChromosome(VariablesValuesContainer valuesContainer, ProgramChromosome chromosome, T context);
}
