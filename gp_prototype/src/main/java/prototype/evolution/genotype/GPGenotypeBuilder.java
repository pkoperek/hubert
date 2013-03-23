package prototype.evolution.genotype;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;

public class GPGenotypeBuilder {

    public static final int DEFAULT_MAX_NODES = 128;        // 128 - maximum number of nodes in equation tree - set according to article

    private GPConfiguration configuration;
    private ChromosomeBuildingStrategy chromosomeBuildingStrategy;
    private int maxNodes = DEFAULT_MAX_NODES;

    public GPGenotypeBuilder(ChromosomeBuildingStrategy chromosomeBuildingStrategy, GPConfiguration configuration) {
        this.chromosomeBuildingStrategy = chromosomeBuildingStrategy;
        this.configuration = configuration;
    }

    public GPGenotypeBuilder setMaxNodes(int maxNodes) {
        this.maxNodes = maxNodes;
        return this;
    }

    public static GPGenotypeBuilder builder(ChromosomeBuildingStrategy chromosomeBuildingStrategy, GPConfiguration configuration) {
        return new GPGenotypeBuilder(chromosomeBuildingStrategy, configuration);
    }

    public GPGenotype build() throws InvalidConfigurationException {
        return createGenotype(configuration);
    }

    private GPGenotype createGenotype(GPConfiguration configuration) throws InvalidConfigurationException {
        Class[] returnTypes = chromosomeBuildingStrategy.createReturnTypes();
        Class[][] argTypes = chromosomeBuildingStrategy.createADFArgumentTypes();
        CommandGene[][] nodeSets = chromosomeBuildingStrategy.createNodeSets(configuration);

        return GPGenotype.randomInitialGenotype(configuration, returnTypes, argTypes, nodeSets, maxNodes, true);
    }

}