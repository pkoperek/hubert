package hubert.evolution.reporting;

import org.jgap.gp.impl.GPGenotype;
import hubert.evolution.engine.EvolutionEngineEvent;
import hubert.evolution.engine.EvolutionEngineEventHandler;
import hubert.evolution.engine.EvolutionEngineEventType;

public class AllTimeBestIndividualReporter implements EvolutionEngineEventHandler {

    private void reportAllTimeBest(GPGenotype genotype) {
        genotype.outputSolution(genotype.getAllTimeBest());
    }

    @Override
    public void handleEvolutionEngineEvent(EvolutionEngineEvent event) {
        if (EvolutionEngineEventType.FINISHED_EVOLUTION.equals(event.getType())) {
            reportAllTimeBest(event.getGenotype());
        }
    }
}