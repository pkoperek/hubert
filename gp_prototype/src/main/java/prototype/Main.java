package prototype;

import org.apache.log4j.xml.DOMConfigurator;
import org.jgap.InvalidConfigurationException;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import prototype.data.container.DataContainer;
import prototype.data.container.DataContainerFactory;
import prototype.differentiation.numeric.NumericalDifferentiationCalculatorFactory;
import prototype.evolution.configuration.GPConfigurationFactory;
import prototype.evolution.engine.EvolutionEngine;
import prototype.evolution.fitness.FitnessFunctionFactory;
import prototype.evolution.genotype.ChromosomeBuildingStrategyFactory;
import prototype.evolution.genotype.GenotypeFactory;
import prototype.evolution.reporting.ParetoFrontFileReporter;

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
        DataContainerFactory dataContainerFactory = new DataContainerFactory();
        dataContainerFactory.setInputFileName(args[0]);
        dataContainerFactory.setImplicitTime(true);
        dataContainerFactory.setTimedData(true);
        DataContainer dataContainer = dataContainerFactory.getDataContainer();

        // fitness function
        FitnessFunctionFactory fitnessFunctionFactory = new FitnessFunctionFactory();
        fitnessFunctionFactory.setCalculatorType(NumericalDifferentiationCalculatorFactory.CalculatorType.CENTRAL);
        fitnessFunctionFactory.setFunctionType(FitnessFunctionFactory.FitnessFunctionType.DIFF);
        fitnessFunctionFactory.setVariableName("sin");
        GPFitnessFunction fitnessFunction = fitnessFunctionFactory.createFitnessFunction(dataContainer);

        // configuration
        GPConfigurationFactory configurationFactory = new GPConfigurationFactory();
        configurationFactory.setPopulationSize(64);
        GPConfiguration configuration = configurationFactory.createConfiguration(fitnessFunction);

        // genotype
        GenotypeFactory genotypeFactory = new GenotypeFactory();
        genotypeFactory.setChromosomes(ChromosomeBuildingStrategyFactory.StrategyType.SINGLE);
        genotypeFactory.setMaxNodes(128);
        GPGenotype genotype = genotypeFactory.createGPGenotype(configuration, dataContainer);

        // evolution
        EvolutionEngine evolutionEngine = EvolutionEngine.Builder.builder()
                .withEvolutionEngineEventHandler(new ParetoFrontFileReporter(50))
                .withMaxIterations(iterations)
                .withDeterministicCrowdingIterations(configuration)
                .build();

        evolutionEngine.genotypeEvolve(genotype);
    }

}