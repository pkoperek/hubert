package hubert.evolution.engine.dc;

import org.jgap.gp.impl.GPConfiguration;
import hubert.evolution.engine.*;
import hubert.evolution.engine.common.AllChromosomesCrossover;
import hubert.evolution.engine.common.AllChromosomesMutator;
import hubert.evolution.engine.common.RandomSelector;
import hubert.evolution.engine.common.StaticMutatedGenesFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: koperek
 * Date: 27.03.13
 * Time: 13:13
 */
public class DeterministicCrowdingEvolutionIterationBuilder {

    private int threadsNum = DeterministicCrowdingEvolutionIteration.DEFAULT_THREADS_NUMBER;
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
        this.crossover = new AllChromosomesCrossover(configuration);
    }

    public DeterministicCrowdingEvolutionIterationBuilder withThreads(int threadsNum) {
        this.threadsNum = threadsNum;
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

    public EvolutionIteration build() {
        DeterministicCrowdingEvolutionIteration deterministicCrowdingEvolutionIteration = new DeterministicCrowdingEvolutionIteration();

        deterministicCrowdingEvolutionIteration.setConfiguration(configuration);
        deterministicCrowdingEvolutionIteration.setCrossover(crossover);
        ExecutorService executorService = Executors.newFixedThreadPool(threadsNum);
        deterministicCrowdingEvolutionIteration.setExecutorService(executorService);
        deterministicCrowdingEvolutionIteration.setMutator(mutator);
        deterministicCrowdingEvolutionIteration.setSelector(selector);
        this.tournament = new AgeFitnessParetoTournament(configuration.getRandomGenerator());
        deterministicCrowdingEvolutionIteration.setTournament(tournament);

        return deterministicCrowdingEvolutionIteration;
    }

    public static DeterministicCrowdingEvolutionIterationBuilder from(DeterministicCrowdingConfiguration configuration) {
        DeterministicCrowdingEvolutionIterationBuilder deterministicCrowdingEvolutionIterationBuilder =
                new DeterministicCrowdingEvolutionIterationBuilder(configuration.getGpConfiguration());

        deterministicCrowdingEvolutionIterationBuilder.withThreads(configuration.getThreadsNum());

        return deterministicCrowdingEvolutionIterationBuilder;
    }
}
