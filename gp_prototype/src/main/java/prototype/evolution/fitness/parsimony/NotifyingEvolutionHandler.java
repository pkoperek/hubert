package prototype.evolution.fitness.parsimony;

import prototype.evolution.engine.EvolutionCycleAware;
import prototype.evolution.engine.EvolutionEngineEvent;
import prototype.evolution.engine.EvolutionEngineEventHandler;
import prototype.evolution.engine.EvolutionEngineEventType;

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
