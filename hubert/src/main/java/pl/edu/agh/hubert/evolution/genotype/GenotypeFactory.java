package pl.edu.agh.hubert.evolution.genotype;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.impl.GPGenotype;

public class GenotypeFactory {

    public GPGenotype createGPGenotype(GenotypeConfiguration genotypeConfiguration) throws InvalidConfigurationException {
        ChromosomeBuildingStrategyFactory buildingStrategyFactory = new ChromosomeBuildingStrategyFactory(genotypeConfiguration.getSolutionChromosomes());
        ChromosomeBuildingStrategy buildingStrategy = buildingStrategyFactory.createStrategy(genotypeConfiguration.getFiteredVariableNames());

        return GPGenotypeBuilder
                .builder(buildingStrategy, genotypeConfiguration.getGpConfiguration())
                .setMaxNodes(genotypeConfiguration.getMaxNodes())
                .build();
    }


}