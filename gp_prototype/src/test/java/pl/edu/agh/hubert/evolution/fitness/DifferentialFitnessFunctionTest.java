package pl.edu.agh.hubert.evolution.fitness;

import org.fest.assertions.Delta;
import org.jgap.InvalidConfigurationException;
import org.jgap.RandomGenerator;
import org.jgap.gp.function.Cosine;
import org.jgap.gp.function.Multiply;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPProgram;
import org.jgap.gp.impl.ProgramChromosome;
import org.jgap.gp.terminal.Terminal;
import org.jgap.gp.terminal.Variable;
import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.hubert.data.container.DataContainer;
import pl.edu.agh.hubert.data.container.DataContainerConfiguration;
import pl.edu.agh.hubert.data.container.DataContainerFactory;
import pl.edu.agh.hubert.differentiation.numeric.NumericalDifferentiationCalculator;
import pl.edu.agh.hubert.differentiation.numeric.NumericalDifferentiationCalculatorFactory;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * User: koperek
 * Date: 19.03.13
 * Time: 00:15
 */
public class DifferentialFitnessFunctionTest {

    private FindTimeDerivativeFitnessFunction findTimeDerivativeFitnessFunction;
    private DataContainer dataContainer;

    @Before
    public void setUp() throws Exception {
        DataContainerConfiguration dataContainerConfiguration = new DataContainerConfiguration();
        dataContainerConfiguration.setInputFileName("src/test/resources/sin_implicit_time.csv");
        dataContainerConfiguration.setImplicitTime(true);
        dataContainerConfiguration.setTimedData(true);
        dataContainer = new DataContainerFactory().getDataContainer(dataContainerConfiguration);
        NumericalDifferentiationCalculator numericalDifferentiationCalculator =
                new NumericalDifferentiationCalculatorFactory()
                        .createCalculator(NumericalDifferentiationCalculatorFactory.CalculatorType.CENTRAL, dataContainer);
        findTimeDerivativeFitnessFunction = new FindTimeDerivativeFitnessFunction("s", dataContainer, numericalDifferentiationCalculator);
    }

    @Test
    public void shouldAcceptExactSolution() throws Exception {

        // Given
        GPConfiguration gpConfiguration = prepareGPConfiguration();
        GPProgram gpProgram = createExactSolution(gpConfiguration);

        // When
        double error = findTimeDerivativeFitnessFunction.evaluate(gpProgram);

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
