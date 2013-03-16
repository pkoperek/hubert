package prototype.evolution.reporting;

import org.jgap.gp.impl.GPGenotype;
import prototype.evolution.engine.EvolutionEngineEvent;
import prototype.evolution.engine.EvolutionEngineEventHandler;
import prototype.evolution.engine.EvolutionEngineEventType;

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