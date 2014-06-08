package pl.edu.agh.hubert.evolution.engine;

import pl.edu.agh.hubert.evolution.reporting.ParetoFrontFileReporter;
import pl.edu.agh.hubert.evolution.reporting.ParetoFrontLoggingReporter;

public class EvolutionEngineFactory {

    public EvolutionEngine createEvolutionEngine(EvolutionEngineConfiguration configuration) {
        EvolutionEngine.Builder builder = preConfiguredBuilder(configuration);

        return builder
                .withDeterministicCrowdingIterations(configuration.getDeterministicCrowdingConfiguration())
                .build();
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

        if (evolutionEngineConfiguration.isStopWhenTargetErrorMet()) {
            builder = builder.withTargetError(evolutionEngineConfiguration.getTargetError());
        }

        return builder
                .withComputationTime(evolutionEngineConfiguration.getComputationTime())
                .withMaxIterations(evolutionEngineConfiguration.getMaxIterations());
    }
}