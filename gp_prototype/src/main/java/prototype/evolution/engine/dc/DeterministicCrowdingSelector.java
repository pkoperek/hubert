package prototype.evolution.engine.dc;

import org.jgap.event.GeneticEvent;
import org.jgap.event.GeneticEventListener;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.INaturalGPSelector;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;

import java.util.ArrayList;
import java.util.List;

// deterministic crowding selector
@Deprecated
public class DeterministicCrowdingSelector implements INaturalGPSelector {

    private GPConfiguration configuration;
    private List<Integer> freeIndexes;

    public DeterministicCrowdingSelector(GPConfiguration a_configuration) {
        this.configuration = a_configuration;
        this.configuration.getEventManager()
                .addEventListener(
                        GeneticEvent.GPGENOTYPE_EVOLVED_EVENT,
                        new GeneticEventListener() {

                            @Override
                            public void geneticEventFired(GeneticEvent a_firedEvent) {
                                DeterministicCrowdingSelector.this.resetFreeIndexes();
                            }
                        }
                );
        this.resetFreeIndexes();
    }

    private void resetFreeIndexes() {
        freeIndexes = new ArrayList<>();
        for (int i = 0; i < configuration.getPopulationSize(); i++) {
            freeIndexes.add(i);
        }
    }

    @Override
    public IGPProgram select(GPGenotype a_genotype) {
        int freeItem = configuration.getRandomGenerator().nextInt(freeIndexes.size());
        int selectedGenotype = freeIndexes.remove(freeItem);
        return a_genotype.getGPPopulation().getGPProgram(selectedGenotype);
    }


}
