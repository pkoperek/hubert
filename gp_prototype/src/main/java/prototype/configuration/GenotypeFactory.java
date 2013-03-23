package prototype.configuration;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import prototype.data.DataContainer;
import prototype.evolution.genotype.ChromosomeBuildingStrategy;
import prototype.evolution.genotype.GPGenotypeBuilder;
import prototype.evolution.genotype.SingleChromosomeBuildingStrategy;

import java.util.Arrays;

public class GenotypeFactory {
    public GPGenotype createGPGenotype(GPConfiguration configuration, DataContainer dataContainer) throws InvalidConfigurationException {
        ChromosomeBuildingStrategy buildingStrategy = new SingleChromosomeBuildingStrategy(Arrays.asList(dataContainer.getVariableNames()));
        return GPGenotypeBuilder
                .builder(buildingStrategy, configuration)
                .setMaxNodes(128)
                .build();
    }

}