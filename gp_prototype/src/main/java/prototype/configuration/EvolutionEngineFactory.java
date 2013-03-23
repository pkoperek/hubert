package prototype.configuration;

import org.jgap.gp.impl.GPConfiguration;
import prototype.evolution.engine.EvolutionEngine;
import prototype.evolution.reporting.ParetoFrontFileReporter;

public class EvolutionEngineFactory {
    public EvolutionEngine createEvolutionEngine(GPConfiguration configuration) {
        return EvolutionEngine.Builder.builder()
                .addEvolutionEngineEventHandlers(new ParetoFrontFileReporter(50))
                .withMaxIterations(500)
                .withDeterministicCrowdingIterations(configuration)
                .build();
    }
}