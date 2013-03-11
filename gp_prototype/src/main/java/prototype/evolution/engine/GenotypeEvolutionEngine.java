package prototype.evolution.engine;

import org.apache.log4j.Logger;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPGenotype;

public class GenotypeEvolutionEngine {

    public static final int DEFAULT_GENERATIONS_PER_ITERATION = 1;
    public static final double DEFAULT_TARGET_ERROR = 0.0000001f;
    public static final int DEFAULT_MAX_ITERATIONS = 10000;

    private static Logger logger = Logger.getLogger(GenotypeEvolutionEngine.class);

    private int iterations;
    private int generationsPerIteration;
    private double targetError;

    GenotypeEvolutionEngine(int iterations, int generationsPerIteration, double targetError) {
        this.iterations = iterations;
        this.generationsPerIteration = generationsPerIteration;
        this.targetError = targetError;
    }

    public void genotypeEvolve(GPGenotype genotype) {
        logger.info("Starting computations");
        for (int i = 0; i < iterations; i++) {
            logger.info("Iteration: " + i + " - evolving...");
            genotype.evolve(generationsPerIteration);

            IGPProgram fittestProgram = genotype.getFittestProgram();

            reportFittestProgram(fittestProgram);

            if (fittestProgram.getFitnessValue() < targetError) {
                logger.info("Found a good solution!");
                break;
            }
        }

        genotype.outputSolution(genotype.getAllTimeBest());
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