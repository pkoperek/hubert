package hubert.evolution.reporting;

import hubert.evolution.engine.EvolutionEngineEvent;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * User: koperek
 * Date: 16.03.13
 * Time: 18:01
 */
// TODO: REFACTOR PARETO FRONT REPORTING/TRACKING
public class ParetoFrontFileReporter extends FileReporter {
    public static final boolean DEFAULT_ENABLED = true;
    public static final int DEFAULT_INTERVAL = 50;

    public ParetoFrontFileReporter() {
        this(1);
    }

    public ParetoFrontFileReporter(final int modulo) {
        super("pareto", modulo);
    }

    @Override
    protected void writeEvent(EvolutionEngineEvent event, BufferedWriter writer) throws IOException {
        double[] fitnesses = event.getSource().getParetoFrontTracker().getFitnesses();

        for (int i = 0; i < fitnesses.length; i++) {
            if (fitnesses[i] != Double.MAX_VALUE) {
                writer.append(fitnesses[i] + "," + i);
                writer.newLine();
            }
        }

    }

}
