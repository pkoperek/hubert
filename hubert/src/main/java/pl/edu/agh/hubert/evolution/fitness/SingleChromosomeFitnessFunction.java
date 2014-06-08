package pl.edu.agh.hubert.evolution.fitness;

import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.ProgramChromosome;
import pl.edu.agh.hubert.evolution.fitness.cevaluators.ChromosomeEvaluator;
import pl.edu.agh.hubert.evolution.fitness.parsimony.ParsimonyPressure;

/**
 * User: koperek
 * Date: 27.03.13
 * Time: 16:20
 */
public class SingleChromosomeFitnessFunction extends ChromosomeFitnessFunction {

    public SingleChromosomeFitnessFunction(ChromosomeEvaluator evaluator, ParsimonyPressure parsimonyPressure) {
        super(parsimonyPressure, evaluator);
    }

    @Override
    protected double evaluate(IGPProgram a_subject) {
        double fitness = 0.0d;

        ProgramChromosome chromosome = a_subject.getChromosome(0);
        fitness += evaluate(chromosome);
        fitness = computeParsimonyPressure(fitness, chromosome);

        return fitness;
    }

}