package prototype.evolution.engine;

import org.apache.log4j.Logger;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.impl.GPPopulation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GenotypeEvolutionEngine {

    public static final int DEFAULT_GENERATIONS_PER_ITERATION = 1;
    public static final double DEFAULT_TARGET_ERROR = 0.0000001f;
    public static final int DEFAULT_MAX_ITERATIONS = 10000;

    private static Logger logger = Logger.getLogger(GenotypeEvolutionEngine.class);

    private int iterations;
    private int generationsPerIteration;
    private double targetError;
    private IterationBeginHandler iterationBeginHandler;

    GenotypeEvolutionEngine(int iterations, int generationsPerIteration, double targetError) {
        this.iterations = iterations;
        this.generationsPerIteration = generationsPerIteration;
        this.targetError = targetError;
    }

    void setIterationBeginHandler(IterationBeginHandler iterationBeginHandler) {
        this.iterationBeginHandler = iterationBeginHandler;
    }

    public void genotypeEvolve(GPGenotype genotype) {
        logger.info("Starting computations");
        for (int i = 0; i < iterations; i++) {
            if (iterationBeginHandler != null) {
                iterationBeginHandler.notifyIterationBegin(i, genotype);
            }

            logger.info("Iteration: " + i + " - evolving...");
            genotype.evolve(generationsPerIteration);

            IGPProgram fittestProgram = genotype.getFittestProgram();

            reportPopulation(genotype.getGPPopulation(), i);
            reportFittestProgram(fittestProgram);

            if (fittestProgram.getFitnessValue() < targetError) {
                logger.info("Found a good solution!");
                break;
            }
        }

        genotype.outputSolution(genotype.getAllTimeBest());
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