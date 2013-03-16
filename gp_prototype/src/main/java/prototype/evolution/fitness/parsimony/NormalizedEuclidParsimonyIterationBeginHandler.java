package prototype.evolution.fitness.parsimony;

import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPGenotype;
import prototype.evolution.engine.IterationBeginHandler;

/**
 * User: koperek
 * Date: 12.03.13
 * Time: 21:33
 */
public class NormalizedEuclidParsimonyIterationBeginHandler implements IterationBeginHandler {

    private NormalizedEuclidParsimonyPressure parsimonyPressure;

    public NormalizedEuclidParsimonyIterationBeginHandler(NormalizedEuclidParsimonyPressure parsimonyPressure) {
        this.parsimonyPressure = parsimonyPressure;
    }

    @Override
    public void notifyIterationBegin(int iteration, GPGenotype genotype) {
        double maxFitness = 0.0;
        double maxLength = 0;

        for (IGPProgram program : genotype.getGPPopulation().getGPPrograms()) {
            double fitnessValue = program.getFitnessValue();
            double length = program.getChromosome(0).size();

            if (fitnessValue > maxFitness) {
                maxFitness = fitnessValue;
            }

            if (length > maxLength) {
                maxLength = length;
            }
        }

        parsimonyPressure.setMaxLength(maxLength);
        parsimonyPressure.setMaxFitness(maxFitness);
    }

}
