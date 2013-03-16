package prototype.evolution.engine;

/**
 * User: koperek
 * Date: 12.03.13
 * Time: 00:33
 */
public class GenotypeEvolutionEngineBuilder {

    private int maxIterations = GenotypeEvolutionEngine.DEFAULT_MAX_ITERATIONS;
    private int generationsPerIteration = GenotypeEvolutionEngine.DEFAULT_GENERATIONS_PER_ITERATION;
    private double targetError = GenotypeEvolutionEngine.DEFAULT_TARGET_ERROR;
    private IterationBeginHandler iterationBeginHandler;

    private GenotypeEvolutionEngineBuilder() {
    }

    public static GenotypeEvolutionEngineBuilder builder() {
        return new GenotypeEvolutionEngineBuilder();
    }

    public GenotypeEvolutionEngineBuilder withIterationStartHandler(IterationBeginHandler iterationBeginHandler) {
        this.iterationBeginHandler = iterationBeginHandler;
        return this;
    }

    public GenotypeEvolutionEngineBuilder withMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
        return this;
    }

    public GenotypeEvolutionEngineBuilder withGenerationsPerIteration(int generationsPerIteration) {
        this.generationsPerIteration = generationsPerIteration;
        return this;
    }

    public GenotypeEvolutionEngineBuilder withTargetError(double targetError) {
        this.targetError = targetError;
        return this;
    }

    public GenotypeEvolutionEngine build() {
        GenotypeEvolutionEngine genotypeEvolutionEngine = new GenotypeEvolutionEngine(
                maxIterations, generationsPerIteration, targetError);
        genotypeEvolutionEngine.setIterationBeginHandler(iterationBeginHandler);
        return genotypeEvolutionEngine;
    }

}
