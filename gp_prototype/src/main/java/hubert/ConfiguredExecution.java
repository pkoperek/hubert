package hubert;

import org.apache.log4j.xml.DOMConfigurator;
import org.constretto.ConstrettoBuilder;
import org.constretto.ConstrettoConfiguration;
import org.constretto.model.Resource;
import org.jgap.InvalidConfigurationException;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import hubert.data.container.DataContainer;
import hubert.data.container.DataContainerConfiguration;
import hubert.data.container.DataContainerFactory;
import hubert.evolution.FitnessVerificator;
import hubert.evolution.configuration.GPConfigurationFactory;
import hubert.evolution.engine.EvolutionEngine;
import hubert.evolution.engine.EvolutionEngineConfiguration;
import hubert.evolution.engine.EvolutionEngineFactory;
import hubert.evolution.fitness.FitnessFunctionConfiguration;
import hubert.evolution.fitness.FitnessFunctionFactory;
import hubert.evolution.genotype.GenotypeConfiguration;
import hubert.evolution.genotype.GenotypeFactory;

import java.io.IOException;

/**
 * User: koperek
 * Date: 11.02.13
 * Time: 19:21
 */
public class ConfiguredExecution {

    public static final String GENERAL_FITNESS_VERIFICATION = "general.fitness.verification";
    private DataContainerFactory dataContainerFactory = new DataContainerFactory();
    private FitnessFunctionFactory fitnessFunctionFactory = new FitnessFunctionFactory();
    private EvolutionEngineFactory evolutionEngineFactory = new EvolutionEngineFactory();
    private GenotypeFactory genotypeFactory = new GenotypeFactory();

    public static void main(String[] args) throws InvalidConfigurationException, IOException {
        DOMConfigurator.configure("log4j.xml");

        new ConfiguredExecution().initialize(args[0]);
    }

    public void initialize(String commandLineArguments) throws IOException, InvalidConfigurationException {
        ConstrettoConfiguration constrettoConfiguration = initializeConfiguration(commandLineArguments);

        // data
        DataContainerConfiguration dataContainerConfiguration = constrettoConfiguration.as(DataContainerConfiguration.class);
        DataContainer dataContainer = dataContainerFactory.getDataContainer(dataContainerConfiguration);

        // fitness function
        FitnessFunctionConfiguration fitnessFunctionConfiguration = constrettoConfiguration.as(FitnessFunctionConfiguration.class);
        GPFitnessFunction fitnessFunction = fitnessFunctionFactory.createFitnessFunction(fitnessFunctionConfiguration, dataContainer);

        // gpConfiguration
        GPConfigurationFactory configurationFactory = constrettoConfiguration.as(GPConfigurationFactory.class);
        GPConfiguration gpConfiguration = configurationFactory.createConfiguration(fitnessFunction);

        // genotype
        GenotypeConfiguration genotypeConfiguration = constrettoConfiguration.as(GenotypeConfiguration.class);
        genotypeConfiguration.setAllVariableNames(dataContainer.getVariableNames());
        genotypeConfiguration.setGpConfiguration(gpConfiguration);
        GPGenotype genotype = genotypeFactory.createGPGenotype(genotypeConfiguration);

        // evolution
        EvolutionEngineConfiguration evolutionEngineConfiguration = constrettoConfiguration.as(EvolutionEngineConfiguration.class);
        evolutionEngineConfiguration.setGpConfiguration(gpConfiguration);
        EvolutionEngine evolutionEngine = evolutionEngineFactory.createEvolutionEngine(evolutionEngineConfiguration);

        // evolve!!!
        evolutionEngine.genotypeEvolve(genotype);
        evolutionEngine.shutdown();

        // fitness verification
        if (shouldVerifyFitness(constrettoConfiguration)) {
            verifyFitness(
                    dataContainerConfiguration,
                    fitnessFunctionConfiguration,
                    evolutionEngine.getParetoFrontTracker().getParetoFront()
            );
        }
    }

    private void verifyFitness(DataContainerConfiguration dataContainerConfiguration, FitnessFunctionConfiguration fitnessFunctionConfiguration, IGPProgram[] paretoFront) throws IOException {
        DataContainer verificationDataContainer = dataContainerFactory.getVerificationDataContainer(dataContainerConfiguration);
        GPFitnessFunction verificationFitnessFunction = fitnessFunctionFactory.createFitnessFunction(fitnessFunctionConfiguration, verificationDataContainer);
        new FitnessVerificator(verificationFitnessFunction).verify(paretoFront);
    }

    private boolean shouldVerifyFitness(ConstrettoConfiguration constrettoConfiguration) {
        if (constrettoConfiguration.hasValue(GENERAL_FITNESS_VERIFICATION)) {
            return constrettoConfiguration.evaluateToBoolean(GENERAL_FITNESS_VERIFICATION);
        }

        return false;
    }

    private ConstrettoConfiguration initializeConfiguration(String configurationFilePath) {
        return new ConstrettoBuilder().createPropertiesStore().addResource(Resource.create("file:" + configurationFilePath)).done().getConfiguration();
    }
}