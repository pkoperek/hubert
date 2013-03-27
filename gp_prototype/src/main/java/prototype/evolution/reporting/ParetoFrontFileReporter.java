package prototype.evolution.reporting;

import org.jgap.gp.impl.GPPopulation;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * User: koperek
 * Date: 16.03.13
 * Time: 18:01
 */
public class ParetoFrontFileReporter extends FilePopulationReporter {
    public static final boolean DEFAULT_ENABLED = true;
    public static final int DEFAULT_INTERVAL = 50;
    private final ParetoFrontGenerator paretoFrontGenerator = new ParetoFrontGenerator(128);

    public ParetoFrontFileReporter() {
        this(1);
    }

    public ParetoFrontFileReporter(final int modulo) {
        super("pareto", modulo);
    }

    protected void writePopulationData(GPPopulation gpPopulation, BufferedWriter writer) throws IOException {
        double[] fitnesses = paretoFrontGenerator.generateParetoFrontFitness(gpPopulation);

        for (int i = 0; i < fitnesses.length; i++) {
            if (fitnesses[i] != Double.MAX_VALUE) {
                writer.append(fitnesses[i] + "," + i);
                writer.newLine();
            }
        }

    }

}
