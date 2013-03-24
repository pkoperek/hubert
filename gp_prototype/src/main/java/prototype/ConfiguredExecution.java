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
import prototype.data.container.DataContainerFactory;
import prototype.evolution.configuration.GPConfigurationFactory;
import prototype.evolution.engine.EvolutionEngine;
import prototype.evolution.engine.EvolutionEngineFactory;
import prototype.evolution.fitness.FitnessFunctionFactory;
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
        DataContainerFactory dataContainerFactory = constrettoConfiguration.as(DataContainerFactory.class);
        DataContainer dataContainer = dataContainerFactory.getDataContainer();

        // fitness function
        FitnessFunctionFactory fitnessFunctionFactory = constrettoConfiguration.as(FitnessFunctionFactory.class);
        GPFitnessFunction fitnessFunction = fitnessFunctionFactory.createFitnessFunction(dataContainer);

        // configuration
        GPConfigurationFactory configurationFactory = constrettoConfiguration.as(GPConfigurationFactory.class);
        GPConfiguration configuration = configurationFactory.createConfiguration(fitnessFunction);

        // genotype
        GenotypeFactory genotypeFactory = constrettoConfiguration.as(GenotypeFactory.class);
        GPGenotype genotype = genotypeFactory.createGPGenotype(configuration, dataContainer);

        // evolution
        EvolutionEngineFactory evolutionEngineFactory = constrettoConfiguration.as(EvolutionEngineFactory.class);
        EvolutionEngine evolutionEngine = evolutionEngineFactory.createDeterministicCrowdingEvolutionEngine(configuration);

        // evolve!!!
        evolutionEngine.genotypeEvolve(genotype);
    }

    private static ConstrettoConfiguration initializeConfiguration(String configurationFilePath) {
        return new ConstrettoBuilder().createPropertiesStore().addResource(Resource.create("file:" + configurationFilePath)).done().getConfiguration();
    }

}