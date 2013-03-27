package prototype.evolution.fitness;

import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.impl.ProgramChromosome;
import prototype.evolution.fitness.cevaluators.ChromosomeEvaluator;
import prototype.evolution.fitness.parsimony.ParsimonyPressure;

/**
 * User: koperek
 * Date: 27.03.13
 * Time: 17:44
 */
public abstract class ChromosomeFitnessFunction extends GPFitnessFunction {
    protected final ChromosomeEvaluator evaluator;
    protected final ParsimonyPressure parsimonyPressure;

    public ChromosomeFitnessFunction(ParsimonyPressure parsimonyPressure, ChromosomeEvaluator evaluator) {
        this.parsimonyPressure = parsimonyPressure;
        this.evaluator = evaluator;
    }

    protected double computeParsimonyPressure(double fitness, ProgramChromosome chromosome) {
        if (parsimonyPressure != null) {
            return parsimonyPressure.pressure(fitness, chromosome);
        }

        return fitness;
    }

    protected double evaluate(ProgramChromosome chromosome) {
        return evaluator.evaluate(chromosome);
    }
}
