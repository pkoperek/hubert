package prototype.evolution.reporting;

import org.apache.log4j.Logger;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPGenotype;
import prototype.evolution.engine.EvolutionEngineEvent;
import prototype.evolution.engine.EvolutionEngineEventHandler;
import prototype.evolution.engine.EvolutionEngineEventType;

public class FittestIndividualReporter implements EvolutionEngineEventHandler {

    private static Logger logger = Logger.getLogger(FittestIndividualReporter.class);

    private void reportFittestProgram(IGPProgram fittestProgram) {
        logger.info(
                "Best fitness: " + fittestProgram.getFitnessValue() +
                        " Depth: " + fittestProgram.getChromosome(0).getDepth(0) +
                        " Complexity: " + fittestProgram.getChromosome(0).size()
        );
        logger.info("Fittest: " + fittestProgram.toStringNorm(0));
    }

    @Override
    public void handleEvolutionEngineEvent(EvolutionEngineEvent event) {
        if (EvolutionEngineEventType.AFTER_EVOLUTION.equals(event.getType())) {
            reportFittestProgram(event.getGenotype());
        }
    }

    private void reportFittestProgram(GPGenotype genotype) {
        reportFittestProgram(genotype.getFittestProgram());
    }
}