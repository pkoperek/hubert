package prototype;

import org.apache.log4j.xml.DOMConfigurator;
import org.jgap.InvalidConfigurationException;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import prototype.data.DataContainer;
import prototype.data.DataContainerFactory;
import prototype.differentiation.numeric.LoessNumericalDifferentiationCalculator;
import prototype.differentiation.numeric.NumericalDifferentiationCalculator;
import prototype.evolution.GPConfigurationBuilder;
import prototype.evolution.engine.EvolutionEngineBuilder;
import prototype.evolution.fitness.DifferentialFitnessFunction;
import prototype.evolution.fitness.parsimony.CovarianceParsimonyPressure;
import prototype.evolution.fitness.parsimony.NotifyingEvolutionHandler;
import prototype.evolution.genotype.GenotypeBuilder;
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
        DataContainer dataContainer = new DataContainerFactory().getDataContainer(args[0]);

        // fitness function
        NumericalDifferentiationCalculator numericalDifferentiationCalculator = new LoessNumericalDifferentiationCalculator(dataContainer);
        DifferentialFitnessFunction fitnessFunction = new DifferentialFitnessFunction(dataContainer, numericalDifferentiationCalculator);
        CovarianceParsimonyPressure parsimonyPressure = new CovarianceParsimonyPressure(fitnessFunction);

        // configuration
        GPConfiguration configuration = GPConfigurationBuilder
                .builder(fitnessFunction)
                .buildConfiguration();

        // genotype
        SingleChromosomeBuildingStrategy buildingStrategy =
                new SingleChromosomeBuildingStrategy(Arrays.asList(dataContainer.getVariableNames()));

        GPGenotype genotype = GenotypeBuilder.builder(buildingStrategy, configuration).build();

        // evolution
        EvolutionEngineBuilder.builder()
                .addEvolutionEngineEventHandlers(new NotifyingEvolutionHandler(parsimonyPressure))
                .addEvolutionEngineEventHandlers(new ParetoFrontFileReporter(50))
                        //.withMaxIterations(iterations)
                .build()
                .genotypeEvolve(genotype);
    }

}