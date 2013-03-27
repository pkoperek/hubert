package prototype;

import org.apache.log4j.xml.DOMConfigurator;
import org.constretto.ConstrettoBuilder;
import org.constretto.ConstrettoConfiguration;
import org.constretto.model.Resource;
import org.jgap.InvalidConfigurationException;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import prototype.data.container.DataContainer;
import prototype.data.container.DataContainerConfiguration;
import prototype.data.container.DataContainerFactory;
import prototype.evolution.configuration.GPConfigurationFactory;
import prototype.evolution.engine.EvolutionEngine;
import prototype.evolution.engine.EvolutionEngineConfiguration;
import prototype.evolution.engine.EvolutionEngineFactory;
import prototype.evolution.fitness.FitnessFunctionConfiguration;
import prototype.evolution.fitness.FitnessFunctionFactory;
import prototype.evolution.genotype.GenotypeConfiguration;
import prototype.evolution.genotype.GenotypeFactory;

import java.io.IOException;

/**
 * User: koperek
 * Date: 11.02.13
 * Time: 19:21
 */
public class ConfiguredExecution {

    public static void main(String[] args) throws InvalidConfigurationException, IOException {
        DOMConfigurator.configure("log4j.xml");

        ConstrettoConfiguration constrettoConfiguration = initializeConfiguration(args[0]);

        // data
        DataContainerConfiguration dataContainerConfiguration = constrettoConfiguration.as(DataContainerConfiguration.class);
        DataContainer dataContainer = new DataContainerFactory().getDataContainer(dataContainerConfiguration);

        // fitness function
        FitnessFunctionConfiguration fitnessFunctionConfiguration = constrettoConfiguration.as(FitnessFunctionConfiguration.class);
        GPFitnessFunction fitnessFunction = new FitnessFunctionFactory().createFitnessFunction(fitnessFunctionConfiguration, dataContainer);

        // gpConfiguration
        GPConfigurationFactory configurationFactory = constrettoConfiguration.as(GPConfigurationFactory.class);
        GPConfiguration gpConfiguration = configurationFactory.createConfiguration(fitnessFunction);

        // genotype
        GenotypeConfiguration genotypeConfiguration = constrettoConfiguration.as(GenotypeConfiguration.class);
        genotypeConfiguration.setAllVariableNames(dataContainer.getVariableNames());
        genotypeConfiguration.setGpConfiguration(gpConfiguration);
        GPGenotype genotype = new GenotypeFactory().createGPGenotype(genotypeConfiguration);

        // evolution
        EvolutionEngineConfiguration evolutionEngineConfiguration = constrettoConfiguration.as(EvolutionEngineConfiguration.class);
        evolutionEngineConfiguration.setGpConfiguration(gpConfiguration);
        EvolutionEngine evolutionEngine = new EvolutionEngineFactory().createEvolutionEngine(evolutionEngineConfiguration);

        // evolve!!!
        evolutionEngine.genotypeEvolve(genotype);
    }

    private static ConstrettoConfiguration initializeConfiguration(String configurationFilePath) {
        return new ConstrettoBuilder().createPropertiesStore().addResource(Resource.create("file:" + configurationFilePath)).done().getConfiguration();
    }
}