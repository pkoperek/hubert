package prototype.evolution.engine;

import org.constretto.annotation.Configuration;
import org.constretto.annotation.Configure;
import org.jgap.gp.impl.GPConfiguration;
import prototype.evolution.reporting.ParetoFrontFileReporter;

public class EvolutionEngineFactory {

    private int maxIterations = EvolutionEngine.DEFAULT_MAX_ITERATIONS;
    private double targetError = EvolutionEngine.DEFAULT_TARGET_ERROR;
    private boolean paretoFrontReporter = ParetoFrontFileReporter.DEFAULT_ENABLED;
    private int paretoFrontReporterFileInterval = ParetoFrontFileReporter.DEFAULT_INTERVAL;
    private IterationType iterationType;

    public enum IterationType {
        DET_CROWDING,
        REGULAR
    }

    public EvolutionEngine createEvolutionEngine(GPConfiguration configuration) {
        switch (iterationType) {
            case DET_CROWDING:
                return createDeterministicCrowdingEvolutionEngine(configuration);
            case REGULAR:
                return createRegularEvolutionEngine();
        }

        throw new IllegalArgumentException("Unsupported iteration type " + iterationType);
    }

    public EvolutionEngine createDeterministicCrowdingEvolutionEngine(GPConfiguration configuration) {
        return withParetoFrontReporter(preConfiguredBuilder())
                .withDeterministicCrowdingIterations(configuration)
                .build();
    }

    public EvolutionEngine createRegularEvolutionEngine() {
        return withParetoFrontReporter(preConfiguredBuilder())
                .withRegularIterations()
                .build();
    }

    private EvolutionEngine.Builder withParetoFrontReporter(EvolutionEngine.Builder builder) {
        if (paretoFrontReporter) {
            return builder.withEvolutionEngineEventHandler(new ParetoFrontFileReporter(paretoFrontReporterFileInterval));
        }
        return builder;
    }

    private EvolutionEngine.Builder preConfiguredBuilder() {
        return EvolutionEngine.Builder.builder()
                .withMaxIterations(maxIterations)
                .withTargetError(targetError);
    }

    @Configure
    public void setMaxIterations(
            @Configuration(
                    value = "engine.max.iterations",
                    defaultValue = "" + EvolutionEngine.DEFAULT_MAX_ITERATIONS)
            int maxIterations) {
        this.maxIterations = maxIterations;
    }

    @Configure
    public void setTargetError(
            @Configuration(
                    value = "engine.target.error",
                    defaultValue = "" + EvolutionEngine.DEFAULT_TARGET_ERROR)
            double targetError) {
        this.targetError = targetError;
    }

    @Configure
    public void setParetoFrontReporter(
            @Configuration(
                    value = "engine.reporter.pareto.enabled",
                    defaultValue = "" + ParetoFrontFileReporter.DEFAULT_ENABLED)
            boolean paretoFrontReporter) {
        this.paretoFrontReporter = paretoFrontReporter;
    }

    @Configure
    public void setParetoFrontReporterFileInterval(
            @Configuration(
                    value = "engine.reporter.pareto.interval",
                    defaultValue = "" + ParetoFrontFileReporter.DEFAULT_INTERVAL)
            int paretoFrontReporterFileInterval) {
        this.paretoFrontReporterFileInterval = paretoFrontReporterFileInterval;
    }


    public void setIterationType(IterationType iterationType) {
        this.iterationType = iterationType;
    }

    @Configure
    public void setIterationType(
            @Configuration(
                    value = "engine.iteration.type",
                    defaultValue = "DET_CROWDING")
            String iterationType) {
        this.iterationType = IterationType.valueOf(iterationType.toUpperCase());
    }
}