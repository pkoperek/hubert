package pl.edu.agh.hubert.evolution.reporting;

import org.jgap.gp.impl.GPGenotype;
import pl.edu.agh.hubert.evolution.engine.EvolutionEngineEvent;
import pl.edu.agh.hubert.evolution.engine.EvolutionEngineEventHandler;
import pl.edu.agh.hubert.evolution.engine.EvolutionEngineEventType;

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