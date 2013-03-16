package prototype.evolution.engine;

import org.apache.log4j.Logger;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.impl.GPPopulation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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

    EvolutionEngine(int iterations, int generationsPerIteration, double targetError, List<EvolutionEngineEventHandler> eventHandlers) {
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

            IGPProgram fittestProgram = genotype.getFittestProgram();

            notifyAfterEvolution(genotype);

            reportPopulation(genotype.getGPPopulation(), i);
            reportFittestProgram(fittestProgram);

            if (fittestProgram.getFitnessValue() < targetError) {
                logger.info("Found a good solution!");
                notifyFinishedEvolution(genotype);
                break;
            }
        }

        genotype.outputSolution(genotype.getAllTimeBest());
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

    private void reportPopulation(GPPopulation gpPopulation, int i) {
        if (logger.isTraceEnabled()) {
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(String.format("population%05d.csv", i)));

                for (IGPProgram program : gpPopulation.getGPPrograms()) {
                    writer.append(program.getFitnessValue() + "," + program.getChromosome(0).size());
                    writer.newLine();
                }

            } catch (IOException e) {
                logger.error("IO error writing population data", e);
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        logger.error("IO error writing population data", e);
                    }
                }
            }
        }
    }

    private void reportFittestProgram(IGPProgram fittestProgram) {
        logger.info(
                "Best fitness: " + fittestProgram.getFitnessValue() +
                        " Depth: " + fittestProgram.getChromosome(0).getDepth(0) +
                        " Complexity: " + fittestProgram.getChromosome(0).size()
        );
        logger.info("Fittest: " + fittestProgram.toStringNorm(0));
    }

}