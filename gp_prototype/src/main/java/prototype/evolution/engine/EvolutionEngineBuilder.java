package prototype.evolution.engine;

import prototype.evolution.reporting.AllTimeBestIndividualReporter;
import prototype.evolution.reporting.FittestIndividualReporter;

import java.util.ArrayList;
import java.util.List;

/**
 * User: koperek
 * Date: 12.03.13
 * Time: 00:33
 */
public class EvolutionEngineBuilder {

    private int maxIterations = EvolutionEngine.DEFAULT_MAX_ITERATIONS;
    private int generationsPerIteration = EvolutionEngine.DEFAULT_GENERATIONS_PER_ITERATION;
    private double targetError = EvolutionEngine.DEFAULT_TARGET_ERROR;
    private List<EvolutionEngineEventHandler> evolutionEngineEventHandlers = new ArrayList<>();

    private EvolutionEngineBuilder() {
    }

    public static EvolutionEngineBuilder builder() {
        return new EvolutionEngineBuilder();
    }

    public EvolutionEngineBuilder addEvolutionEngineEventHandlers(EvolutionEngineEventHandler evolutionEngineEventHandler) {
        this.evolutionEngineEventHandlers.add(evolutionEngineEventHandler);
        return this;
    }

    public EvolutionEngineBuilder withMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
        return this;
    }

    public EvolutionEngineBuilder withGenerationsPerIteration(int generationsPerIteration) {
        this.generationsPerIteration = generationsPerIteration;
        return this;
    }

    public EvolutionEngineBuilder withTargetError(double targetError) {
        this.targetError = targetError;
        return this;
    }

    public EvolutionEngine build() {
        fillDefaultReporters();

        EvolutionEngine evolutionEngine = new EvolutionEngine(
                maxIterations, generationsPerIteration, targetError, evolutionEngineEventHandlers);
        return evolutionEngine;
    }

    private void fillDefaultReporters() {
        evolutionEngineEventHandlers.add(new AllTimeBestIndividualReporter());
        evolutionEngineEventHandlers.add(new FittestIndividualReporter());
    }

}
