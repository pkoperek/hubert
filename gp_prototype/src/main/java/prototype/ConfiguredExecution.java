package prototype;

import org.apache.log4j.xml.DOMConfigurator;
import org.jgap.InvalidConfigurationException;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import prototype.data.DataContainer;
import prototype.data.DataContainerFactory;
import prototype.differentiation.numeric.CentralNumericalDifferentiationCalculator;
import prototype.differentiation.numeric.NumericalDifferentiationCalculator;
import prototype.evolution.GPConfigurationBuilder;
import prototype.evolution.engine.EvolutionEngine;
import prototype.evolution.fitness.DifferentialFitnessFunction;
import prototype.evolution.fitness.parsimony.CovarianceParsimonyPressure;
import prototype.evolution.fitness.parsimony.EvolutionCycleAware;
import prototype.evolution.fitness.parsimony.NotifyingEvolutionHandler;
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
public class ConfiguredExecution {

    private static final int ITERATIONS = 500;

    public static void main(String[] args) throws InvalidConfigurationException, IOException {
        DOMConfigurator.configure("log4j.xml");

        // data
        DataContainer dataContainer = new DataContainerFactory().getDataContainer(args[0]);

        // fitness function
        NumericalDifferentiationCalculator numericalDifferentiationCalculator = new CentralNumericalDifferentiationCalculator(dataContainer);
        DifferentialFitnessFunction fitnessFunction = new DifferentialFitnessFunction("sin", dataContainer, numericalDifferentiationCalculator);
        EvolutionCycleAware parsimonyPressure = new CovarianceParsimonyPressure(fitnessFunction);

        // configuration
        GPConfiguration configuration = createConfiguration(fitnessFunction);

        // genotype
        SingleChromosomeBuildingStrategy buildingStrategy =
                new SingleChromosomeBuildingStrategy(Arrays.asList(dataContainer.getVariableNames()));

        GPGenotype genotype = GPGenotypeBuilder
                .builder(buildingStrategy, configuration)
                .setMaxNodes(128)
                .build();

        // evolution
        EvolutionEngine evolutionEngine = EvolutionEngine.Builder.builder()
                .addEvolutionEngineEventHandlers(new NotifyingEvolutionHandler(parsimonyPressure))
                .addEvolutionEngineEventHandlers(new ParetoFrontFileReporter(50))
                .withMaxIterations(500)
                .withDeterministicCrowdingIterations(configuration)
                .build();

        evolutionEngine.genotypeEvolve(genotype);
    }

    private static GPConfiguration createConfiguration(DifferentialFitnessFunction fitnessFunction) throws InvalidConfigurationException {
        return GPConfigurationBuilder
                .builder(fitnessFunction)
                .setPopulationSize(64)
                .withDeterministicCrowding()
                        //.withNewChromsPercent(0.25)
                .buildConfiguration();
    }

}