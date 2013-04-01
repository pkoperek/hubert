package prototype.evolution.engine;

import prototype.evolution.reporting.ParetoFrontFileReporter;
import prototype.evolution.reporting.ParetoFrontLoggingReporter;

public class EvolutionEngineFactory {

    public EvolutionEngine createEvolutionEngine(EvolutionEngineConfiguration configuration) {
        EvolutionEngine.Builder builder = preConfiguredBuilder(configuration);

        switch (configuration.getIterationType()) {
            case DET_CROWDING:
                return builder
                        .withDeterministicCrowdingIterations(configuration.getDeterministicCrowdingConfiguration())
                        .build();
            case REGULAR:
                return builder
                        .withRegularIterations()
                        .build();
        }

        throw new IllegalArgumentException("Unsupported iteration type " + configuration.getIterationType());
    }

    private EvolutionEngine.Builder preConfiguredBuilder(EvolutionEngineConfiguration evolutionEngineConfiguration) {
        EvolutionEngine.Builder builder = EvolutionEngine.Builder.builder();

        if (evolutionEngineConfiguration.isParetoFrontTracking()) {
            builder = builder.withTrackParetoFront();

            if (evolutionEngineConfiguration.isParetoFrontFileReporter()) {
                builder.withEvolutionEngineEventHandler(new ParetoFrontFileReporter(
                        evolutionEngineConfiguration.getParetoFrontReporterFileInterval()));
            }
            if (evolutionEngineConfiguration.isParetoFrontLoggerReporter()) {
                builder.withEvolutionEngineEventHandler(new ParetoFrontLoggingReporter());
            }
        }

        return builder
                .withComputationTime(evolutionEngineConfiguration.getComputationTime())
                .withMaxIterations(evolutionEngineConfiguration.getMaxIterations())
                .withTargetError(evolutionEngineConfiguration.getTargetError());
    }
}