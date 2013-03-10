package prototype.evolution;

import org.apache.log4j.Logger;
import org.jgap.gp.impl.GPGenotype;

public class GenotypeEvolutionEngine {

    public static final int DEFAULT_GENERATIONS_PER_ITERATION = 1;
    private static Logger logger = Logger.getLogger(GenotypeEvolutionEngine.class);
    private static final double DEFAULT_TARGET_ERROR = 0.0000001f;
    private static final int DEFAULT_MAX_ITERATIONS = 10000;

    private int iterations;
    private int generationsPerIteration;
    private double targetError;

    public GenotypeEvolutionEngine(int iterations, int generationsPerIteration, double targetError) {
        this.iterations = iterations;
        this.generationsPerIteration = generationsPerIteration;
        this.targetError = targetError;
    }

    public GenotypeEvolutionEngine(int iterations) {
        this(iterations, DEFAULT_GENERATIONS_PER_ITERATION);
    }

    public GenotypeEvolutionEngine(int iterations, int generationsPerIteration) {
        this(iterations, generationsPerIteration, DEFAULT_TARGET_ERROR);
    }

    public GenotypeEvolutionEngine(double targetError) {
        this(DEFAULT_MAX_ITERATIONS, 1, targetError);
    }

    public void genotypeEvolve(GPGenotype genotype) {
        logger.info("Starting computations");
        for (int i = 0; i < iterations; i++) {
            logger.info("Iteration: " + i + " - evolving...");
            genotype.evolve(generationsPerIteration);

            double bestFitnessValue = genotype.getFittestProgram().getFitnessValue();

            logger.info("Best fitness: " + bestFitnessValue);
            logger.info("Fittest: " + genotype.getFittestProgram().toStringNorm(0));

            if (bestFitnessValue < targetError) {
                logger.info("Found a good solution!");
                break;
            }
        }

        genotype.outputSolution(genotype.getAllTimeBest());
    }
}