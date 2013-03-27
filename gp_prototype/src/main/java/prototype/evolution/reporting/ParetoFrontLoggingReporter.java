package prototype.evolution.reporting;

import org.apache.log4j.Logger;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.impl.GPPopulation;
import prototype.evolution.engine.EvolutionEngineEvent;
import prototype.evolution.engine.EvolutionEngineEventHandler;
import prototype.evolution.engine.EvolutionEngineEventType;

/**
 * User: koperek
 * Date: 27.03.13
 * Time: 21:51
 */
public class ParetoFrontLoggingReporter implements EvolutionEngineEventHandler {

    private static final Logger logger = Logger.getLogger(ParetoFrontLoggingReporter.class);
    private ParetoFrontGenerator paretoFrontGenerator = new ParetoFrontGenerator(128);

    @Override
    public void handleEvolutionEngineEvent(EvolutionEngineEvent event) {
        if (EvolutionEngineEventType.AFTER_EVOLUTION.equals(event.getType())) {
            handleGenotype(event.getGenotype());
        }
    }

    private void handleGenotype(GPGenotype genotype) {
        GPPopulation gpPopulation = genotype.getGPPopulation();
        IGPProgram[] programs = paretoFrontGenerator.generateParetoFrontPrograms(gpPopulation);

        for (int i = 0; i < programs.length; i++) {
            if (programs[i] != null) {
                logger.info("Size: " + i + " Program: " + programs[i].toStringNorm(0));
            }
        }
    }
}
