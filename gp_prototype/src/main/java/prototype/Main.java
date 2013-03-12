package prototype;

import org.apache.log4j.xml.DOMConfigurator;
import org.jgap.InvalidConfigurationException;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPGenotype;
import prototype.data.DataContainer;
import prototype.data.DataContainerFactory;
import prototype.evolution.GPConfigurationBuilder;
import prototype.evolution.engine.GenotypeEvolutionEngineBuilder;
import prototype.evolution.fitness.EureqaFitnessFunction;
import prototype.evolution.fitness.parsimony.EuclidParsimonyPressure;
import prototype.evolution.genotype.GenotypeBuilder;
import prototype.evolution.genotype.SingleChromosomeBuildingStrategy;

import java.io.IOException;
import java.util.Arrays;

/**
 * User: koperek
 * Date: 11.02.13
 * Time: 19:21
 */
public class Main {

    private static final int ITERATIONS = 500;
    private static final int GENERATIONS_PER_ITERATION = 1;

    public static void main(String[] args) throws InvalidConfigurationException, IOException {
        DOMConfigurator.configure("log4j.xml");

        int iterations;
        try {
            iterations = Integer.parseInt(args[1]);
        } catch (Exception e) {
            iterations = ITERATIONS;
        }

        DataContainer dataContainer = new DataContainerFactory().getDataContainer(args[0]);

        GPFitnessFunction fitnessFunction =
                new EuclidParsimonyPressure(
                        new EureqaFitnessFunction(dataContainer));

        SingleChromosomeBuildingStrategy buildingStrategy =
                new SingleChromosomeBuildingStrategy(Arrays.asList(dataContainer.getVariableNames()));

        GPConfiguration configuration = GPConfigurationBuilder
                .builder(fitnessFunction)
                .buildConfiguration();

        GPGenotype genotype = GenotypeBuilder.builder(buildingStrategy, configuration).build();
        GenotypeEvolutionEngineBuilder.builder().setMaxIterations(iterations).build().genotypeEvolve(genotype);
    }

}