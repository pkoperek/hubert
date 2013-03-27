package prototype.evolution.engine;

import prototype.evolution.reporting.ParetoFrontFileReporter;

public class EvolutionEngineFactory {

    public EvolutionEngine createEvolutionEngine(EvolutionEngineConfiguration configuration) {
        EvolutionEngine.Builder builder = preConfiguredBuilder(configuration);

        switch (configuration.getIterationType()) {
            case DET_CROWDING:
                return builder
                        .withDeterministicCrowdingIterations(configuration.getGpConfiguration())
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
        if (evolutionEngineConfiguration.isParetoFrontReporter()) {
            builder.withEvolutionEngineEventHandler(new ParetoFrontFileReporter(
                    evolutionEngineConfiguration.getParetoFrontReporterFileInterval()));
        }
        return builder
                .withMaxIterations(evolutionEngineConfiguration.getMaxIterations())
                .withTargetError(evolutionEngineConfiguration.getTargetError());
    }
}