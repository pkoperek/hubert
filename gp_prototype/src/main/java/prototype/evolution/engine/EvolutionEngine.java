package prototype.evolution.engine;

import org.apache.log4j.Logger;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPGenotype;

import java.util.List;

public class EvolutionEngine {

    public static final int DEFAULT_GENERATIONS_PER_ITERATION = 1;
    public static final double DEFAULT_TARGET_ERROR = 0.0000001f;
    public static final int DEFAULT_MAX_ITERATIONS = 10000;

    private static Logger logger = Logger.getLogger(EvolutionEngine.class);

    private final int iterations;
    private final int generationsPerIteration;
    private final double targetError;
    private final List<EvolutionEngineEventHandler> eventHandlers;

    public EvolutionEngine(int iterations, int generationsPerIteration, double targetError, List<EvolutionEngineEventHandler> eventHandlers) {
        this.iterations = iterations;
        this.generationsPerIteration = generationsPerIteration;
        this.targetError = targetError;
        this.eventHandlers = eventHandlers;
    }

    public void genotypeEvolve(GPGenotype genotype) {
        logger.info("Starting computations");
        for (int i = 0; i < iterations; i++) {
            notifyBeforeEvolution(genotype);

            logger.info("Iteration: " + i + " - evolving...");
            genotype.evolve(generationsPerIteration);

            notifyAfterEvolution(genotype);

            IGPProgram fittestProgram = genotype.getFittestProgram();
            if (fittestProgram.getFitnessValue() < targetError) {
                logger.info("Found a good solution!");
                notifyFinishedEvolution(genotype);
                break;
            }
        }
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

}