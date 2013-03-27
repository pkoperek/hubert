package prototype.evolution.reporting;

import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPPopulation;

import java.util.Arrays;

public class ParetoFrontGenerator {
    private final double[] fitnesses;
    private final IGPProgram[] fittestPrograms;

    public ParetoFrontGenerator(int size) {
        fittestPrograms = new IGPProgram[size];
        fitnesses = new double[size];
    }

    public double[] generateParetoFrontFitness(GPPopulation gpPopulation) {
        computeParetoFront(gpPopulation);
        return fitnesses;
    }

    private void computeParetoFront(GPPopulation gpPopulation) {
        Arrays.fill(fitnesses, Double.MAX_VALUE);
        for (IGPProgram program : gpPopulation.getGPPrograms()) {
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

    public IGPProgram[] generateParetoFrontPrograms(GPPopulation gpPopulation) {
        computeParetoFront(gpPopulation);
        return fittestPrograms;
    }
}