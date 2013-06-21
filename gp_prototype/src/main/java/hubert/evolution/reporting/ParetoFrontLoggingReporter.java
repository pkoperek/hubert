package hubert.evolution.reporting;

import org.apache.log4j.Logger;
import org.jgap.gp.IGPProgram;
import hubert.evolution.engine.*;

/**
 * User: koperek
 * Date: 27.03.13
 * Time: 21:51
 */
public class ParetoFrontLoggingReporter implements EvolutionEngineEventHandler {

    private static final Logger logger = Logger.getLogger(ParetoFrontLoggingReporter.class);

    @Override
    public void handleEvolutionEngineEvent(EvolutionEngineEvent event) {
        if (EvolutionEngineEventType.AFTER_EVOLUTION.equals(event.getType())) {
            logParetoFront(event.getSource());
        }
    }

    private void logParetoFront(EvolutionEngine source) {
        ParetoFrontTracker paretoFrontTracker = source.getParetoFrontTracker();
        IGPProgram[] programs = paretoFrontTracker.getParetoFront();
        double[] fitnesses = paretoFrontTracker.getFitnesses();

        for (int i = 0; i < programs.length; i++) {
            if (programs[i] != null) {
                logger.info("Size: " + i + " Fitness: " + fitnesses[i] + " Program: " + programs[i].toStringNorm(0));
            }
        }
    }
}
