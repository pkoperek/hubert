package prototype.evolution.genotype;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import prototype.evolution.GPConfigurationBuilder;

public class GenotypeBuilder {

    private GPConfiguration configuration;
    private AbstractGenotypeBuildingStrategy genotypeBuildingStrategy;
    private int maxNodes = GPConfigurationBuilder.DEFAULT_MAX_NODES;

    public GenotypeBuilder(AbstractGenotypeBuildingStrategy genotypeBuildingStrategy, GPConfiguration configuration) {
        this.genotypeBuildingStrategy = genotypeBuildingStrategy;
        this.configuration = configuration;
    }

    public GenotypeBuilder setMaxNodes(int maxNodes) {
        this.maxNodes = maxNodes;
        return this;
    }

    public static GenotypeBuilder builder(AbstractGenotypeBuildingStrategy genotypeBuildingStrategy, GPConfiguration configuration) {
        return new GenotypeBuilder(genotypeBuildingStrategy, configuration);
    }

    public GPGenotype build() throws InvalidConfigurationException {
        return createGenotype(configuration);
    }

    private GPGenotype createGenotype(GPConfiguration configuration) throws InvalidConfigurationException {
        Class[] returnTypes = genotypeBuildingStrategy.createReturnTypes();
        Class[][] argTypes = genotypeBuildingStrategy.createADFArgumentTypes();
        CommandGene[][] nodeSets = genotypeBuildingStrategy.createNodeSets(configuration);

        return GPGenotype.randomInitialGenotype(configuration, returnTypes, argTypes, nodeSets, maxNodes, true);
    }

}