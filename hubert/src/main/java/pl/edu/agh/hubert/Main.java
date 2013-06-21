package pl.edu.agh.hubert;

import org.apache.log4j.xml.DOMConfigurator;
import org.jgap.InvalidConfigurationException;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import pl.edu.agh.hubert.data.container.DataContainer;
import pl.edu.agh.hubert.data.container.DataContainerConfiguration;
import pl.edu.agh.hubert.data.container.DataContainerFactory;
import pl.edu.agh.hubert.differentiation.numeric.NumericalDifferentiationCalculatorFactory;
import pl.edu.agh.hubert.evolution.configuration.GPConfigurationFactory;
import pl.edu.agh.hubert.evolution.engine.EvolutionEngine;
import pl.edu.agh.hubert.evolution.engine.dc.DeterministicCrowdingConfiguration;
import pl.edu.agh.hubert.evolution.fitness.FitnessFunctionConfiguration;
import pl.edu.agh.hubert.evolution.fitness.FitnessFunctionFactory;
import pl.edu.agh.hubert.evolution.genotype.ChromosomeBuildingStrategyFactory;
import pl.edu.agh.hubert.evolution.genotype.GenotypeConfiguration;
import pl.edu.agh.hubert.evolution.genotype.GenotypeFactory;
import pl.edu.agh.hubert.evolution.reporting.ParetoFrontFileReporter;

import java.io.IOException;

/**
 * User: koperek
 * Date: 11.02.13
 * Time: 19:21
 */
public class Main {

    private static final int ITERATIONS = 500;

    public static void main(String[] args) throws InvalidConfigurationException, IOException {
        DOMConfigurator.configure("log4j.xml");

        int iterations;
        try {
            iterations = Integer.parseInt(args[1]);
        } catch (Exception e) {
            iterations = ITERATIONS;
        }

        // data
        DataContainerConfiguration dataContainerConfiguration = new DataContainerConfiguration();
        dataContainerConfiguration.setInputFileName(args[0]);
        dataContainerConfiguration.setImplicitTime(true);
        dataContainerConfiguration.setTimedData(true);
        DataContainer dataContainer = new DataContainerFactory().getDataContainer(dataContainerConfiguration);

        // fitness function
        FitnessFunctionConfiguration fitnessFunctionConfiguration = new FitnessFunctionConfiguration();
        fitnessFunctionConfiguration.setCalculatorType(NumericalDifferentiationCalculatorFactory.CalculatorType.CENTRAL);
        fitnessFunctionConfiguration.setFunctionType(FitnessFunctionConfiguration.FitnessFunctionType.TIME_DERIV);
        fitnessFunctionConfiguration.setVariableName("sin");
        GPFitnessFunction fitnessFunction = new FitnessFunctionFactory().createFitnessFunction(fitnessFunctionConfiguration, dataContainer);

        // gpConfiguration
        GPConfigurationFactory configurationFactory = new GPConfigurationFactory();
        configurationFactory.setPopulationSize(64);
        GPConfiguration gpConfiguration = configurationFactory.createConfiguration(fitnessFunction);

        // genotype
        GenotypeConfiguration genotypeConfiguration = new GenotypeConfiguration();
        genotypeConfiguration.setChromosomes(ChromosomeBuildingStrategyFactory.StrategyType.SINGLE);
        genotypeConfiguration.setMaxNodes(128);
        genotypeConfiguration.setAllVariableNames(dataContainer.getVariableNames());
        genotypeConfiguration.setGpConfiguration(gpConfiguration);
        GPGenotype genotype = new GenotypeFactory().createGPGenotype(genotypeConfiguration);

        // evolution
        DeterministicCrowdingConfiguration deterministicCrowdingConfiguration = new DeterministicCrowdingConfiguration();
        deterministicCrowdingConfiguration.setGpConfiguration(gpConfiguration);
        EvolutionEngine evolutionEngine = EvolutionEngine.Builder.builder()
                .withEvolutionEngineEventHandler(new ParetoFrontFileReporter(50))
                .withMaxIterations(iterations)
                .withDeterministicCrowdingIterations(deterministicCrowdingConfiguration)
                .build();

        evolutionEngine.genotypeEvolve(genotype);
    }

}