package prototype;

import org.apache.log4j.xml.DOMConfigurator;
import org.jgap.InvalidConfigurationException;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import prototype.data.DataContainer;
import prototype.data.DataContainerFactory;
import prototype.differentiation.numeric.NumericalDifferentiationCalculatorFactory;
import prototype.evolution.configuration.GPConfigurationFactory;
import prototype.evolution.engine.EvolutionEngine;
import prototype.evolution.fitness.FitnessFunctionFactory;
import prototype.evolution.genotype.GPGenotypeBuilder;
import prototype.evolution.genotype.SingleChromosomeBuildingStrategy;
import prototype.evolution.reporting.ParetoFrontFileReporter;

import java.io.IOException;
import java.util.Arrays;

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
        DataContainer dataContainer = new DataContainerFactory(args[0]).getDataContainer();

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
        SingleChromosomeBuildingStrategy buildingStrategy =
                new SingleChromosomeBuildingStrategy(Arrays.asList(dataContainer.getVariableNames()));

        GPGenotype genotype = GPGenotypeBuilder
                .builder(buildingStrategy, configuration)
                .setMaxNodes(128)
                .build();

        // evolution
        EvolutionEngine evolutionEngine = EvolutionEngine.Builder.builder()
                .addEvolutionEngineEventHandlers(new ParetoFrontFileReporter(50))
                .withMaxIterations(iterations)
                .withDeterministicCrowdingIterations(configuration)
                .build();

        evolutionEngine.genotypeEvolve(genotype);
    }

}