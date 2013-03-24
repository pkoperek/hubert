package prototype.evolution.genotype;

import org.constretto.annotation.Configuration;
import org.constretto.annotation.Configure;
import org.jgap.InvalidConfigurationException;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import prototype.data.container.DataContainer;

import java.util.Arrays;

public class GenotypeFactory {

    private int maxNodes = GPGenotypeBuilder.DEFAULT_MAX_NODES;
    private ChromosomeBuildingStrategyFactory.StrategyType chromosomes;

    public GPGenotype createGPGenotype(GPConfiguration configuration, DataContainer dataContainer) throws InvalidConfigurationException {
        ChromosomeBuildingStrategyFactory buildingStrategyFactory = new ChromosomeBuildingStrategyFactory(chromosomes);
        ChromosomeBuildingStrategy buildingStrategy = buildingStrategyFactory.createStrategy(Arrays.asList(dataContainer.getVariableNames()));

        return GPGenotypeBuilder
                .builder(buildingStrategy, configuration)
                .setMaxNodes(maxNodes)
                .build();
    }

    @Configure
    public void setMaxNodes(
            @Configuration(
                    value = "genotype.max.nodes",
                    defaultValue = "" + GPGenotypeBuilder.DEFAULT_MAX_NODES
            )
            int maxNodes) {
        this.maxNodes = maxNodes;
    }

    @Configure
    public void setChromosomes(
            @Configuration(
                    value = "genotype.chromosomes",
                    defaultValue = "SINGLE"
            )
            String chromosomes) {
        this.chromosomes = ChromosomeBuildingStrategyFactory.StrategyType.valueOf(chromosomes);
    }

    public void setChromosomes(ChromosomeBuildingStrategyFactory.StrategyType chromosomes) {
        this.chromosomes = chromosomes;
    }
}