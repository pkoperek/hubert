package hubert.evolution.fitness.parsimony;

import hubert.evolution.engine.EvolutionCycleAware;
import hubert.evolution.engine.EvolutionEngineEvent;
import hubert.evolution.engine.EvolutionEngineEventHandler;
import hubert.evolution.engine.EvolutionEngineEventType;

/**
 * User: koperek
 * Date: 16.03.13
 * Time: 13:40
 */
public class NotifyingEvolutionHandler implements EvolutionEngineEventHandler {
    private final EvolutionCycleAware evolutionCycleAware;

    public NotifyingEvolutionHandler(EvolutionCycleAware evolutionCycleAware) {
        this.evolutionCycleAware = evolutionCycleAware;
    }

    @Override
    public void handleEvolutionEngineEvent(EvolutionEngineEvent event) {
        if (EvolutionEngineEventType.BEFORE_EVOLUTION.equals(event.getType())) {
            evolutionCycleAware.handleBeforeEvolution(event.getGenotype());
        }

        if (EvolutionEngineEventType.AFTER_EVOLUTION.equals(event.getType())) {
            evolutionCycleAware.handleAfterEvolution(event.getGenotype());
        }
    }
}
