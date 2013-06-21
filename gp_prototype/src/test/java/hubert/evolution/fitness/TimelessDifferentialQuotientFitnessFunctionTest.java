package hubert.evolution.fitness;

import org.fest.assertions.Delta;
import org.jgap.InvalidConfigurationException;
import org.jgap.RandomGenerator;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.function.Add;
import org.jgap.gp.function.Multiply;
import org.jgap.gp.function.Subtract;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPProgram;
import org.jgap.gp.impl.ProgramChromosome;
import org.jgap.gp.terminal.Terminal;
import org.jgap.gp.terminal.Variable;
import org.junit.Before;
import org.junit.Test;
import hubert.data.container.DataContainer;
import hubert.data.container.DataContainerConfiguration;
import hubert.data.container.DataContainerFactory;
import hubert.differentiation.numeric.NumericalDifferentiationCalculatorFactory;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * User: koperek
 * Date: 24.03.13
 * Time: 22:01
 */
public class TimelessDifferentialQuotientFitnessFunctionTest {
    private DataContainer dataContainer;
    private GPFitnessFunction fitnessFunction;

    @Before
    public void setUp() throws Exception {
        DataContainerConfiguration dataContainerConfiguration = new DataContainerConfiguration();
        dataContainerConfiguration.setInputFileName("src/test/resources/circle.csv");
        dataContainerConfiguration.setImplicitTime(true);
        dataContainerConfiguration.setTimedData(true);
        dataContainer = new DataContainerFactory().getDataContainer(dataContainerConfiguration);

        FitnessFunctionConfiguration fitnessFunctionConfiguration = new FitnessFunctionConfiguration();
        fitnessFunctionConfiguration.setFunctionType(FitnessFunctionConfiguration.FitnessFunctionType.TLESS_DIFFQ);
        fitnessFunctionConfiguration.setCalculatorType(NumericalDifferentiationCalculatorFactory.CalculatorType.CENTRAL);
        fitnessFunction = new FitnessFunctionFactory().createFitnessFunction(fitnessFunctionConfiguration, dataContainer);
    }

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

        ProgramChromosome chromosome = new ProgramChromosome(gpConfiguration);
        chromosome.setGene(0, new Subtract(gpConfiguration, Float.class));
        chromosome.setGene(1, new Add(gpConfiguration, Float.class));
        chromosome.setGene(2, new Multiply(gpConfiguration, Float.class));
        chromosome.setGene(3, new Variable(gpConfiguration, "x", Float.class));
        chromosome.setGene(4, new Variable(gpConfiguration, "x", Float.class));
        chromosome.setGene(5, new Multiply(gpConfiguration, Float.class));
        chromosome.setGene(6, new Variable(gpConfiguration, "y", Float.class));
        chromosome.setGene(7, new Variable(gpConfiguration, "y", Float.class));
        chromosome.setGene(8, createConstant(gpConfiguration, -1.0f));
        chromosome.redepth();
        return chromosome;
    }

    private Terminal createConstant(GPConfiguration gpConfiguration, float a_value) throws InvalidConfigurationException {
        Terminal constant = new Terminal(gpConfiguration, Float.class, 0.0d, 10.0d, false);
        constant.setValue(a_value); // 2 * PI / 100
        return constant;
    }

    private GPConfiguration prepareGPConfiguration() {
        GPConfiguration gpConfiguration = mock(GPConfiguration.class);
        RandomGenerator randomGenerator = mock(RandomGenerator.class);
        given(gpConfiguration.getRandomGenerator()).willReturn(randomGenerator);
        given(gpConfiguration.getPopulationSize()).willReturn(9);
        return gpConfiguration;
    }

}
