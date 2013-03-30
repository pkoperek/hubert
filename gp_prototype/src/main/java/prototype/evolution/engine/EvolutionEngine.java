package prototype.evolution.engine;

import org.apache.log4j.Logger;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPGenotype;
import prototype.evolution.engine.dc.DeterministicCrowdingConfiguration;
import prototype.evolution.engine.dc.DeterministicCrowdingEvolutionIterationBuilder;
import prototype.evolution.engine.jgap.GPGenotypeDelegatingEvolutionIteration;
import prototype.evolution.reporting.AllTimeBestIndividualReporter;
import prototype.evolution.reporting.FittestIndividualReporter;

import java.util.ArrayList;
import java.util.List;

public class EvolutionEngine {

    public static final double DEFAULT_TARGET_ERROR = 0.0000001f;
    public static final int DEFAULT_MAX_ITERATIONS = 1000000;

    private static Logger logger = Logger.getLogger(EvolutionEngine.class);

    private final long finishTimeStamp;
    private final int iterations;
    private final double targetError;
    private final List<EvolutionEngineEventHandler> eventHandlers;
    private final EvolutionIteration evolutionIteration;

    EvolutionEngine(long finishTimeStamp, int iterations, double targetError, EvolutionIteration evolutionIteration, List<EvolutionEngineEventHandler> eventHandlers) {
        this.finishTimeStamp = finishTimeStamp;
        this.iterations = iterations;
        this.targetError = targetError;
        this.evolutionIteration = evolutionIteration;
        this.eventHandlers = eventHandlers;
    }

    public void genotypeEvolve(GPGenotype genotype) {
        logger.info("Starting computations");
        for (int i = 0; i < iterations; i++) {
            notifyBeforeEvolution(genotype);

            logger.info("Iteration: " + i + " - evolving...");
            evolutionIteration.evolve(genotype);

            notifyAfterEvolution(genotype);

            if (timeTargetMet()) {
                logger.info("Time out - finishing");
                break;
            }

            if (fitnessTargetMet(genotype.getFittestProgram())) {
                logger.info("Found a good solution!");
                notifyFinishedEvolution(genotype);
                break;
            }
        }
    }

    private boolean timeTargetMet() {
        long currentTimestamp = System.currentTimeMillis();
        logger.info("Current: " + currentTimestamp + " finish: " + finishTimeStamp);
        return finishTimeStamp < 0 ? false : currentTimestamp >= finishTimeStamp;
    }

    private boolean fitnessTargetMet(IGPProgram fittestProgram) {
        return fittestProgram.getFitnessValue() < targetError;
    }

    private void notifyFinishedEvolution(GPGenotype genotype) {
        notifyEvolutionEngineEventHandlers(new EvolutionEngineEvent(EvolutionEngineEventType.FINISHED_EVOLUTION, genotype));
    }

    private void notifyAfterEvolution(GPGenotype genotype) {
        notifyEvolutionEngineEventHandlers(new EvolutionEngineEvent(EvolutionEngineEventType.AFTER_EVOLUTION, genotype));
    }

    private void notifyBeforeEvolution(GPGenotype genotype) {
        notifyEvolutionEngineEventHandlers(new EvolutionEngineEvent(EvolutionEngineEventType.BEFORE_EVOLUTION, genotype));
    }

    private void notifyEvolutionEngineEventHandlers(EvolutionEngineEvent event) {
        for (EvolutionEngineEventHandler handler : eventHandlers) {
            handler.handleEvolutionEngineEvent(event);
        }
    }

    public void shutdown() {
        evolutionIteration.shutdown();
    }

    public static class Builder implements CommonStep, RegularStep, DeterministicCrowdingStep {

        private int maxIterations = EvolutionEngine.DEFAULT_MAX_ITERATIONS;
        private double targetError = EvolutionEngine.DEFAULT_TARGET_ERROR;
        private List<EvolutionEngineEventHandler> evolutionEngineEventHandlers = new ArrayList<>();
        private EvolutionIteration evolutionIteration;
        private long computationTimeInMs;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        @Override
        public Builder withComputationTime(long computationTimeInMs) {
            this.computationTimeInMs = computationTimeInMs;
            return this;
        }

        @Override
        public Builder withEvolutionEngineEventHandler(EvolutionEngineEventHandler evolutionEngineEventHandler) {
            this.evolutionEngineEventHandlers.add(evolutionEngineEventHandler);
            return this;
        }

        @Override
        public Builder withMaxIterations(int maxIterations) {
            this.maxIterations = maxIterations;
            return this;
        }

        @Override
        public Builder withTargetError(double targetError) {
            this.targetError = targetError;
            return this;
        }

        @Override
        public DeterministicCrowdingStep withDeterministicCrowdingIterations(DeterministicCrowdingConfiguration configuration) {
            evolutionIteration = DeterministicCrowdingEvolutionIterationBuilder
                    .from(configuration)
                    .build();
            return this;
        }

        @Override
        public RegularStep withRegularIterations() {
            evolutionIteration = new GPGenotypeDelegatingEvolutionIteration();
            return this;
        }

        @Override
        public EvolutionEngine build() {
            fillDefaultReporters();

            long finishComputationTimestamp = computeTargetTimestamp();

            EvolutionEngine evolutionEngine = new EvolutionEngine(
                    finishComputationTimestamp,
                    maxIterations,
                    targetError,
                    evolutionIteration,
                    evolutionEngineEventHandlers);

            return evolutionEngine;
        }

        private long computeTargetTimestamp() {
            if (computationTimeInMs < 0) {
                return computationTimeInMs;
            }

            long currentTimestamp = System.currentTimeMillis();
            long targetTimestamp = currentTimestamp + computationTimeInMs;
            logger.info("Target timestamp: " + currentTimestamp + " target: " + targetTimestamp);
            return targetTimestamp;
        }

        private void fillDefaultReporters() {
            evolutionEngineEventHandlers.add(new AllTimeBestIndividualReporter());
            evolutionEngineEventHandlers.add(new FittestIndividualReporter());
        }

    }

    public interface CommonStep {
        public Builder withComputationTime(long computationTimeInMs);

        Builder withEvolutionEngineEventHandler(EvolutionEngineEventHandler evolutionEngineEventHandler);

        Builder withMaxIterations(int maxIterations);

        Builder withTargetError(double targetError);

        DeterministicCrowdingStep withDeterministicCrowdingIterations(DeterministicCrowdingConfiguration configuration);

        RegularStep withRegularIterations();
    }

    public interface DeterministicCrowdingStep {
        EvolutionEngine build();
    }

    public interface RegularStep {
        EvolutionEngine build();
    }

}