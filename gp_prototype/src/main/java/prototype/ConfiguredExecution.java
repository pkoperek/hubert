package prototype;

import org.apache.log4j.xml.DOMConfigurator;
import org.constretto.ConstrettoBuilder;
import org.constretto.ConstrettoConfiguration;
import org.constretto.model.Resource;
import org.jgap.InvalidConfigurationException;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import prototype.configuration.EvolutionEngineFactory;
import prototype.configuration.GenotypeFactory;
import prototype.data.DataContainer;
import prototype.data.DataContainerFactory;
import prototype.evolution.configuration.GPConfigurationFactory;
import prototype.evolution.engine.EvolutionEngine;
import prototype.evolution.fitness.FitnessFunctionFactory;

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
        DataContainer dataContainer = new DataContainerFactory(constrettoConfiguration).getDataContainer();

        // fitness function
        FitnessFunctionFactory fitnessFunctionFactory = constrettoConfiguration.as(FitnessFunctionFactory.class);
        GPFitnessFunction fitnessFunction = fitnessFunctionFactory.createFitnessFunction(dataContainer);

        // configuration
        GPConfigurationFactory configurationFactory = constrettoConfiguration.as(GPConfigurationFactory.class);
        GPConfiguration configuration = configurationFactory.createConfiguration(fitnessFunction);

        // genotype
        GPGenotype genotype = new GenotypeFactory().createGPGenotype(configuration, dataContainer);

        // evolution
        EvolutionEngine evolutionEngine = new EvolutionEngineFactory().createEvolutionEngine(configuration);

        // evolve!!!
        evolutionEngine.genotypeEvolve(genotype);
    }

    private static ConstrettoConfiguration initializeConfiguration(String configurationFilePath) {
        return new ConstrettoBuilder().createPropertiesStore().addResource(Resource.create("file:" + configurationFilePath)).done().getConfiguration();
    }

}