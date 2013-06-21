package hubert.evolution.engine;

import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPPopulation;

import java.util.Arrays;

/**
 * User: koperek
 * Date: 28.03.13
 * Time: 12:00
 */
public class ParetoFrontTracker {
    private final double[] fitnesses;
    private final IGPProgram[] fittestPrograms;

    public ParetoFrontTracker(int size) {
        fittestPrograms = new IGPProgram[size];
        fitnesses = new double[size];
        Arrays.fill(fitnesses, Double.MAX_VALUE);
    }

    public void trackPopulation(GPPopulation population) {
        for (IGPProgram program : population.getGPPrograms()) {
            double fitness = program.getFitnessValue();
            int size = program.getChromosome(0).size();

            if (fitnesses[size] > fitness) {
                fitnesses[size] = fitness;
                fittestPrograms[size] = program;
            }
        }

        for (int i = 0; i < fitnesses.length; i++) {
            if (fitnesses[i] != Double.MAX_VALUE) {
                for (int j = i + 1; j < fitnesses.length; j++) {
                    if (fitnesses[j] != Double.MAX_VALUE && fitnesses[j] > fitnesses[i]) {
                        fitnesses[j] = Double.MAX_VALUE;
                        fittestPrograms[j] = null;
                    }
                }
            }
        }
    }

    public double[] getFitnesses() {
        return Arrays.copyOf(fitnesses, fitnesses.length);
    }

    public IGPProgram[] getParetoFront() {
        return Arrays.copyOf(fittestPrograms, fittestPrograms.length);
    }
}
