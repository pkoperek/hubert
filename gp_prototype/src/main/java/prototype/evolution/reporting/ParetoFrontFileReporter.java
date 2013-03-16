package prototype.evolution.reporting;

import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPPopulation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * User: koperek
 * Date: 16.03.13
 * Time: 18:01
 */
public class ParetoFrontFileReporter extends FilePopulationReporter {
    private double[] fitnesses = new double[128]; // 128 - max size of single solution

    public ParetoFrontFileReporter() {
        this(1);
    }

    public ParetoFrontFileReporter(final int modulo) {
        super("pareto", modulo);
    }

    protected void writePopulationData(GPPopulation gpPopulation, BufferedWriter writer) throws IOException {
        Arrays.fill(fitnesses, Double.MAX_VALUE);

        generateParetoFront(gpPopulation);

        for (int i = 0; i < fitnesses.length; i++) {
            if (fitnesses[i] != Double.MAX_VALUE) {
                writer.append(i + "," + fitnesses[i]);
                writer.newLine();
            }
        }

    }

    private void generateParetoFront(GPPopulation gpPopulation) {
        for (IGPProgram program : gpPopulation.getGPPrograms()) {
            double fitness = program.getFitnessValue();
            int size = program.getChromosome(0).size();

            if (fitnesses[size] > fitness) {
                fitnesses[size] = fitness;
            }
        }
    }
}
