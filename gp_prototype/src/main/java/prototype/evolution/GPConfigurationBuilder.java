package prototype.evolution;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.impl.DeltaGPFitnessEvaluator;
import org.jgap.gp.impl.GPConfiguration;
import prototype.data.DataContainer;
import prototype.evolution.fitness.AbsoluteErrorFitnessFunction;

public class GPConfigurationBuilder {

    public static final int DEFAULT_MAX_INIT_DEPTH = 8; // 8 levels should be enough to contain 128 elements
    public static final int DEFAULT_POPULATION_SIZE = 2048; // 2048 according to article
    public static final float DEFAULT_CROSSOVER_PROBABILITY = 0.75f; // setting according to article
    public static final int DEFAULT_MAX_NODES = 128;        // 128 - maximum number of nodes in equation tree - set according to article
    public static final float DEFAULT_MUTATION_PROBABILITY = 0.01f; // setting according to article

    private int maxInitDepth = DEFAULT_MAX_INIT_DEPTH;
    private int populationSize = DEFAULT_POPULATION_SIZE;
    private float crossoverProbability = DEFAULT_CROSSOVER_PROBABILITY;
    private float mutationProbability = DEFAULT_MUTATION_PROBABILITY;
    private final DataContainer dataContainer;

    private GPConfigurationBuilder(DataContainer dataContainer) {
        this.dataContainer = dataContainer;
    }

    public static GPConfigurationBuilder builder(DataContainer dataContainer) {
        return new GPConfigurationBuilder(dataContainer);
    }

    public GPConfigurationBuilder setMaxInitDepth(int maxInitDepth) {
        this.maxInitDepth = maxInitDepth;
        return this;
    }

    public GPConfigurationBuilder setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
        return this;
    }

    public GPConfigurationBuilder setCrossoverProbability(float crossoverProbability) {
        this.crossoverProbability = crossoverProbability;
        return this;
    }

    public GPConfigurationBuilder setMutationProbability(float mutationProbability) {
        this.mutationProbability = mutationProbability;
        return this;
    }

    public GPConfiguration buildConfiguration() throws InvalidConfigurationException {
        GPConfiguration config = new GPConfiguration();
        config.setMaxInitDepth(maxInitDepth);
        config.setPopulationSize(populationSize);
        config.setCrossoverProb(crossoverProbability);
        config.setMutationProb(mutationProbability);
        config.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
//        config.setFitnessFunction(new PrototypeFitnessFunction(dataContainer));
        config.setFitnessFunction(new AbsoluteErrorFitnessFunction(dataContainer));
        return config;
    }
}