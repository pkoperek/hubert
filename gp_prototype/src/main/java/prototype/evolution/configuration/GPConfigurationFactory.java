package prototype.evolution.configuration;

import org.constretto.annotation.Configuration;
import org.constretto.annotation.Configure;
import org.jgap.InvalidConfigurationException;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.impl.GPConfiguration;

import static prototype.evolution.configuration.GPConfigurationBuilder.*;

public class GPConfigurationFactory {

    private int populationSize = GPConfigurationBuilder.DEFAULT_POPULATION_SIZE;
    private int maxInitDepth = GPConfigurationBuilder.DEFAULT_MAX_INIT_DEPTH;
    private double newChromsPercent = GPConfigurationBuilder.DEFAULT_NEW_CHROMOSOMES_PERCENT;
    private float crossoverProbability = GPConfigurationBuilder.DEFAULT_CROSSOVER_PROBABILITY;
    private float mutationProbability = GPConfigurationBuilder.DEFAULT_MUTATION_PROBABILITY;

    public GPConfiguration createConfiguration(GPFitnessFunction fitnessFunction) throws InvalidConfigurationException {
        return
                builder(fitnessFunction)
                        .withNewChromsPercent(newChromsPercent)
                        .withMaxInitDepth(maxInitDepth)
                        .withPopulationSize(populationSize)
                        .withCrossoverProbability(crossoverProbability)
                        .withMutationProbability(mutationProbability)
                        .buildConfiguration();
    }

    @Configure
    public void setPopulationSize(
            @Configuration(
                    value = "general.population.size",
                    defaultValue = "" + DEFAULT_POPULATION_SIZE)
            int populationSize) {
        this.populationSize = populationSize;
    }

    @Configure
    public void setMaxInitDepth(
            @Configuration(
                    value = "general.max.init.depth",
                    defaultValue = "" + DEFAULT_MAX_INIT_DEPTH
            )
            int maxInitDepth) {
        this.maxInitDepth = maxInitDepth;
    }

    @Configure
    public void setNewChromsPercent(
            @Configuration(
                    value = "general.new.chromosomes.percent",
                    defaultValue = "" + DEFAULT_NEW_CHROMOSOMES_PERCENT
            )
            double newChromsPercent) {
        if (newChromsPercent > 1.0) {
            newChromsPercent = newChromsPercent / 100;
        }

        this.newChromsPercent = newChromsPercent;
    }

    @Configure
    public void setCrossoverProbability(
            @Configuration(
                    value = "general.crossover.probability",
                    defaultValue = "" + DEFAULT_CROSSOVER_PROBABILITY
            )
            float crossoverProbability) {
        this.crossoverProbability = crossoverProbability;
    }

    @Configure
    public void setMutationProbability(
            @Configuration(
                    value = "general.mutation.probability",
                    defaultValue = "" + DEFAULT_MUTATION_PROBABILITY
            )
            float mutationProbability) {
        this.mutationProbability = mutationProbability;
    }
}