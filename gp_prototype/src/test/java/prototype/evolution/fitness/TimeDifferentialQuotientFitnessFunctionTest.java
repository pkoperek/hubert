package prototype.evolution.fitness;

import org.fest.assertions.Delta;
import org.jgap.InvalidConfigurationException;
import org.jgap.RandomGenerator;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.function.Cosine;
import org.jgap.gp.function.Multiply;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPProgram;
import org.jgap.gp.impl.ProgramChromosome;
import org.jgap.gp.terminal.Terminal;
import org.jgap.gp.terminal.Variable;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import prototype.data.container.DataContainer;
import prototype.data.container.DataContainerFactory;
import prototype.differentiation.numeric.NumericalDifferentiationCalculatorFactory;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * User: koperek
 * Date: 24.03.13
 * Time: 21:43
 */
public class TimeDifferentialQuotientFitnessFunctionTest {

    private DataContainer dataContainer;
    private GPFitnessFunction fitnessFunction;

    @Before
    public void setUp() throws Exception {
        DataContainerFactory dataContainerFactory = new DataContainerFactory();
        dataContainerFactory.setInputFileName("src/test/resources/monitoring_data.csv");
        dataContainerFactory.setImplicitTime(true);
        dataContainerFactory.setTimedData(true);
        dataContainer = dataContainerFactory.getDataContainer();

        FitnessFunctionFactory fitnessFunctionFactory = new FitnessFunctionFactory();
        fitnessFunctionFactory.setFunctionType(FitnessFunctionFactory.FitnessFunctionType.DIFFQ);
        fitnessFunctionFactory.setCalculatorType(NumericalDifferentiationCalculatorFactory.CalculatorType.LOESS);
        fitnessFunction = fitnessFunctionFactory.createFitnessFunction(dataContainer);
    }

    @Ignore
    @Test
    public void shouldEvaluate() throws Exception {

        // Given
        GPConfiguration gpConfiguration = prepareGPConfiguration();
        GPProgram gpProgram = createExactSolution(gpConfiguration);

        // When
        double error = fitnessFunction.getFitnessValue(gpProgram);

        // Then
        assertThat(error).isEqualTo(0.0d, Delta.delta(0.0001));

    }

    private GPProgram createExactSolution(GPConfiguration gpConfiguration) throws InvalidConfigurationException {
        GPProgram gpProgram = new GPProgram(gpConfiguration, 1);
        ProgramChromosome chromosome = createExactSolutionChromosome(gpConfiguration);
        gpProgram.setChromosome(0, chromosome);
        return gpProgram;
    }

    private ProgramChromosome createExactSolutionChromosome(GPConfiguration gpConfiguration) throws InvalidConfigurationException {
        Terminal constant = new Terminal(gpConfiguration, Float.class, 0.0d, 10.0d, false);
        constant.setValue(0.0628318531f); // 2 * PI / 100

        ProgramChromosome chromosome = new ProgramChromosome(gpConfiguration);
        chromosome.setGene(0, new Multiply(gpConfiguration, Float.class));
        chromosome.setGene(1, new Cosine(gpConfiguration, Float.class));
        chromosome.setGene(2, new Variable(gpConfiguration, "x", Float.class));
        chromosome.setGene(3, constant);
        chromosome.redepth();
        return chromosome;
    }

    private GPConfiguration prepareGPConfiguration() {
        GPConfiguration gpConfiguration = mock(GPConfiguration.class);
        RandomGenerator randomGenerator = mock(RandomGenerator.class);
        given(gpConfiguration.getRandomGenerator()).willReturn(randomGenerator);
        given(gpConfiguration.getPopulationSize()).willReturn(4);
        return gpConfiguration;
    }
}
