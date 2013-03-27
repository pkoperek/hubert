package prototype.evolution.engine.dc;

import org.jgap.gp.impl.GPConfiguration;
import prototype.evolution.engine.*;
import prototype.evolution.engine.common.AllChromosomesCrossover;
import prototype.evolution.engine.common.AllChromosomesMutator;
import prototype.evolution.engine.common.RandomSelector;
import prototype.evolution.engine.common.StaticMutatedGenesFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: koperek
 * Date: 27.03.13
 * Time: 13:13
 */
public class DeterministicCrowdingEvolutionIterationBuilder {

    private ExecutorService executorService;
    private GPConfiguration configuration;
    private Mutator mutator;
    private Selector<Integer> selector;
    private Tournament tournament;
    private Crossover crossover;

    private DeterministicCrowdingEvolutionIterationBuilder(GPConfiguration configuration) {
        this.configuration = configuration;
        this.mutator = new AllChromosomesMutator(
                new StaticMutatedGenesFactory(configuration.getRandomGenerator()),
                configuration.getRandomGenerator());
        this.selector = new RandomSelector<Integer>(configuration.getRandomGenerator());
        this.tournament = new DeterministicCrowdingTournament(new HammingDistance());
        this.crossover = new AllChromosomesCrossover(configuration);
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public DeterministicCrowdingEvolutionIterationBuilder withExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }

    public DeterministicCrowdingEvolutionIterationBuilder withMutator(Mutator mutator) {
        this.mutator = mutator;
        return this;
    }

    public DeterministicCrowdingEvolutionIterationBuilder withSelector(Selector<Integer> selector) {
        this.selector = selector;
        return this;
    }

    public DeterministicCrowdingEvolutionIterationBuilder withTournament(Tournament tournament) {
        this.tournament = tournament;
        return this;
    }

    public DeterministicCrowdingEvolutionIterationBuilder withCrossover(Crossover crossover) {
        this.crossover = crossover;
        return this;
    }

    public static DeterministicCrowdingEvolutionIterationBuilder from(GPConfiguration configuration) {
        return new DeterministicCrowdingEvolutionIterationBuilder(configuration);
    }

    public EvolutionIteration build() {
        DeterministicCrowdingEvolutionIteration deterministicCrowdingEvolutionIteration = new DeterministicCrowdingEvolutionIteration();

        deterministicCrowdingEvolutionIteration.setConfiguration(configuration);
        deterministicCrowdingEvolutionIteration.setCrossover(crossover);
        deterministicCrowdingEvolutionIteration.setExecutorService(executorService);
        deterministicCrowdingEvolutionIteration.setMutator(mutator);
        deterministicCrowdingEvolutionIteration.setSelector(selector);
        deterministicCrowdingEvolutionIteration.setTournament(tournament);

        return deterministicCrowdingEvolutionIteration;
    }
}
