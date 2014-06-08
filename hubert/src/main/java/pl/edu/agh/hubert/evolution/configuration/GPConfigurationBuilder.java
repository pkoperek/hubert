package pl.edu.agh.hubert.evolution.configuration;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.impl.DeltaGPFitnessEvaluator;
import org.jgap.gp.impl.GPConfiguration;

class GPConfigurationBuilder {

    public static final int DEFAULT_MAX_INIT_DEPTH = 6; // 8 levels should be enough to contain 128 elements; lets not make it too complicated
    public static final int DEFAULT_POPULATION_SIZE = 128; // 2048 according to article
    public static final float DEFAULT_CROSSOVER_PROBABILITY = 0.75f; // setting according to article 0.75
    public static final float DEFAULT_MUTATION_PROBABILITY = 0.05f; // setting according to article 0.01

    private int maxInitDepth = DEFAULT_MAX_INIT_DEPTH;
    public static final double DEFAULT_NEW_CHROMOSOMES_PERCENT = 0.3d;
    private int populationSize = DEFAULT_POPULATION_SIZE;
    private float crossoverProbability = DEFAULT_CROSSOVER_PROBABILITY;
    private float mutationProbability = DEFAULT_MUTATION_PROBABILITY;

    private GPFitnessFunction fitnessFunction;
    private DeltaGPFitnessEvaluator fitnessEvaluator = new DeltaGPFitnessEvaluator();
    private double newChromsPercent = DEFAULT_NEW_CHROMOSOMES_PERCENT; // by default 30%

    public GPConfigurationBuilder withNewChromsPercent(double newChromsPercent) {
        this.newChromsPercent = newChromsPercent;
        return this;
    }

    private GPConfigurationBuilder(GPFitnessFunction fitnessFunction) {
        this.fitnessFunction = fitnessFunction;
    }

    public static GPConfigurationBuilder builder(GPFitnessFunction fitnessFunction) {
        return new GPConfigurationBuilder(fitnessFunction);
    }

    public GPConfigurationBuilder setFitnessEvaluator(DeltaGPFitnessEvaluator fitnessEvaluator) {
        this.fitnessEvaluator = fitnessEvaluator;
        return this;
    }

    public GPConfigurationBuilder withMaxInitDepth(int maxInitDepth) {
        this.maxInitDepth = maxInitDepth;
        return this;
    }

    public GPConfigurationBuilder withPopulationSize(int populationSize) {
        this.populationSize = populationSize;
        return this;
    }

    public GPConfigurationBuilder withCrossoverProbability(float crossoverProbability) {
        this.crossoverProbability = crossoverProbability;
        return this;
    }

    public GPConfigurationBuilder withMutationProbability(float mutationProbability) {
        this.mutationProbability = mutationProbability;
        return this;
    }

    public GPConfiguration buildConfiguration() throws InvalidConfigurationException {
        GPConfiguration config = new GPConfiguration();

        config.setMaxInitDepth(maxInitDepth);
        config.setPopulationSize(populationSize);
        config.setCrossoverProb(crossoverProbability);
        config.setMutationProb(mutationProbability);
        config.setGPFitnessEvaluator(fitnessEvaluator);
        config.setFitnessFunction(fitnessFunction);
        config.setNewChromsPercent(newChromsPercent);

        return config;
    }
}