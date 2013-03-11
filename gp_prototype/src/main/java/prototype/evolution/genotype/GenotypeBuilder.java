package prototype.evolution.genotype;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import prototype.evolution.GPConfigurationBuilder;

public class GenotypeBuilder {

    private AbstractGenotypeBuildingStrategy genotypeBuildingStrategy;
    private int maxNodes = GPConfigurationBuilder.DEFAULT_MAX_NODES;
    private GPFitnessFunction fitnessFunction;

    public GenotypeBuilder(GPFitnessFunction fitnessFunction, AbstractGenotypeBuildingStrategy genotypeBuildingStrategy) {
        this.fitnessFunction = fitnessFunction;
        this.genotypeBuildingStrategy = genotypeBuildingStrategy;
    }

    public GenotypeBuilder setMaxNodes(int maxNodes) {
        this.maxNodes = maxNodes;
        return this;
    }

    public static GenotypeBuilder builder(GPFitnessFunction fitnessFunction, AbstractGenotypeBuildingStrategy genotypeBuildingStrategy) {
        return new GenotypeBuilder(fitnessFunction, genotypeBuildingStrategy);
    }

    public GPGenotype build() throws InvalidConfigurationException {
        return createGenotype(
                GPConfigurationBuilder
                        .builder(fitnessFunction)
                        .buildConfiguration()
        );
    }

    private GPGenotype createGenotype(GPConfiguration configuration) throws InvalidConfigurationException {
        Class[] returnTypes = genotypeBuildingStrategy.createReturnTypes();
        Class[][] argTypes = genotypeBuildingStrategy.createADFArgumentTypes();
        CommandGene[][] nodeSets = genotypeBuildingStrategy.createNodeSets(configuration);

        return GPGenotype.randomInitialGenotype(configuration, returnTypes, argTypes, nodeSets, maxNodes, true);
    }

}