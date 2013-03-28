package prototype.evolution.fitness;

import org.apache.log4j.xml.DOMConfigurator;
import org.fest.assertions.Delta;
import org.jgap.InvalidConfigurationException;
import org.jgap.RandomGenerator;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.function.Add;
import org.jgap.gp.function.Cosine;
import org.jgap.gp.function.Multiply;
import org.jgap.gp.function.Subtract;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPProgram;
import org.jgap.gp.impl.ProgramChromosome;
import org.jgap.gp.terminal.Terminal;
import org.jgap.gp.terminal.Variable;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import prototype.data.container.DataContainer;
import prototype.data.container.DataContainerConfiguration;
import prototype.data.container.DataContainerFactory;
import prototype.differentiation.numeric.NumericalDifferentiationCalculatorFactory;

import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * User: koperek
 * Date: 24.03.13
 * Time: 21:43
 */
public class TimeDifferentialQuotientFitnessFunctionTest {

    private DataContainerFactory dataContainerFactory;

    @BeforeClass
    public static void beforeClass() throws Exception {
        DOMConfigurator.configure("src/test/resources/log4j_ff.xml");
    }

    @Before
    public void setUp() throws Exception {
        dataContainerFactory = new DataContainerFactory();
    }

    @Ignore
    @Test
    public void shouldEvaluateSet1() throws Exception {

        // Given
        DataContainer dataContainer = prepareDataContainer("src/test/resources/monitoring_data.csv");
        GPFitnessFunction fitnessFunction = prepareFitnessFunction(dataContainer);

        GPConfiguration gpConfiguration = prepareGPConfiguration(4);
        GPProgram gpProgram = createExactSolutionSet1(gpConfiguration);

        // When
        double error = fitnessFunction.getFitnessValue(gpProgram);

        // Then
        assertThat(error).isEqualTo(0.0d, Delta.delta(0.0001));
    }

    @Ignore
    @Test
    public void shouldEvaluateSet2() throws Exception {

        // Given
        DataContainer dataContainer = prepareDataContainer("src/test/resources/monitoring_data_2_norm.csv", false, "t");
        GPFitnessFunction fitnessFunction = prepareFitnessFunction(dataContainer);

        GPConfiguration gpConfiguration = prepareGPConfiguration(14);
        GPProgram gpProgramExact = createExactSolutionSet2(gpConfiguration);
        GPProgram gpProgramWrong = createWrongSolutionSet2(gpConfiguration);

        // When
        double exactError = fitnessFunction.getFitnessValue(gpProgramExact);
        double wrongError = fitnessFunction.getFitnessValue(gpProgramWrong);

        System.out.println("Exact: " + exactError + " Wrong: " + wrongError);

        // Then
        assertThat(exactError).isEqualTo(0.0d, Delta.delta(0.0001));
    }

    private GPProgram createWrongSolutionSet2(GPConfiguration gpConfiguration) throws InvalidConfigurationException {
        GPProgram gpProgram = new GPProgram(gpConfiguration, 1);
        ProgramChromosome chromosome = createWrongSolutionChromosome2(gpConfiguration);
        gpProgram.setChromosome(0, chromosome);
        return gpProgram;

    }

    private ProgramChromosome createWrongSolutionChromosome2(GPConfiguration gpConfiguration) throws InvalidConfigurationException {
        ProgramChromosome chromosome = new ProgramChromosome(gpConfiguration);
        chromosome.setGene(0, new Multiply(gpConfiguration, Float.class));
        chromosome.setGene(1, new Variable(gpConfiguration, "t", Float.class));
        chromosome.setGene(2, new Variable(gpConfiguration, "c", Float.class));
        chromosome.redepth();
        return chromosome;

    }

    private GPProgram createExactSolutionSet2(GPConfiguration gpConfiguration) throws InvalidConfigurationException {
        GPProgram gpProgram = new GPProgram(gpConfiguration, 1);
        ProgramChromosome chromosome = createExactSolutionChromosome2(gpConfiguration);
        gpProgram.setChromosome(0, chromosome);
        return gpProgram;
    }

    private ProgramChromosome createExactSolutionChromosome2(GPConfiguration gpConfiguration) throws InvalidConfigurationException {
        // 3.115*t + cos(3.08138 + 2.88161*t) - 3,37825*c
        Terminal a = createTerminalWithValue(gpConfiguration, 3.115f);
        Terminal b = createTerminalWithValue(gpConfiguration, 3.08138f);
        Terminal c = createTerminalWithValue(gpConfiguration, 2.88161f);
        Terminal d = createTerminalWithValue(gpConfiguration, 3.37825f);

        ProgramChromosome chromosome = new ProgramChromosome(gpConfiguration);
        chromosome.setGene(0, new Subtract(gpConfiguration, Float.class));
        chromosome.setGene(1, new Add(gpConfiguration, Float.class));
        chromosome.setGene(2, new Multiply(gpConfiguration, Float.class));
        chromosome.setGene(3, a);
        chromosome.setGene(4, new Variable(gpConfiguration, "t", Float.class));
        chromosome.setGene(5, new Cosine(gpConfiguration, Float.class));
        chromosome.setGene(6, new Add(gpConfiguration, Float.class));
        chromosome.setGene(7, b);
        chromosome.setGene(8, new Multiply(gpConfiguration, Float.class));
        chromosome.setGene(9, c);
        chromosome.setGene(10, new Variable(gpConfiguration, "t", Float.class));
        chromosome.setGene(11, new Multiply(gpConfiguration, Float.class));
        chromosome.setGene(12, d);
        chromosome.setGene(13, new Variable(gpConfiguration, "c", Float.class));
        chromosome.redepth();
        return chromosome;
    }

    private Terminal createTerminalWithValue(GPConfiguration gpConfiguration, float value) throws InvalidConfigurationException {
        Terminal terminal = new Terminal(gpConfiguration, Float.class, 0.0d, 10.0d, false);
        terminal.setValue(value);
        return terminal;
    }

    private GPFitnessFunction prepareFitnessFunction(DataContainer dataContainer) {
        FitnessFunctionConfiguration fitnessFunctionConfiguration = new FitnessFunctionConfiguration();
        fitnessFunctionConfiguration.setFunctionType(FitnessFunctionConfiguration.FitnessFunctionType.DIFFQ);
        fitnessFunctionConfiguration.setCalculatorType(NumericalDifferentiationCalculatorFactory.CalculatorType.CENTRAL);
        return new FitnessFunctionFactory().createFitnessFunction(fitnessFunctionConfiguration, dataContainer);
    }

    private DataContainer prepareDataContainer(String inputFileName) throws IOException {
        return prepareDataContainer(inputFileName, true, null);
    }

    private DataContainer prepareDataContainer(String inputFileName, boolean implicitTime, String timeVariable) throws IOException {
        DataContainerConfiguration dataContainerConfiguration = new DataContainerConfiguration();
        dataContainerConfiguration.setInputFileName(inputFileName);
        dataContainerConfiguration.setImplicitTime(implicitTime);
        dataContainerConfiguration.setTimeVariable(timeVariable);
        dataContainerConfiguration.setTimedData(true);
        return dataContainerFactory.getDataContainer(dataContainerConfiguration);
    }

    private GPProgram createExactSolutionSet1(GPConfiguration gpConfiguration) throws InvalidConfigurationException {
        GPProgram gpProgram = new GPProgram(gpConfiguration, 1);
        ProgramChromosome chromosome = createExactSolutionChromosome(gpConfiguration);
        gpProgram.setChromosome(0, chromosome);
        return gpProgram;
    }

    private ProgramChromosome createExactSolutionChromosome(GPConfiguration gpConfiguration) throws InvalidConfigurationException {
        Terminal constant = createTerminalWithValue(gpConfiguration, 0.0628318531f);

        ProgramChromosome chromosome = new ProgramChromosome(gpConfiguration);
        chromosome.setGene(0, new Multiply(gpConfiguration, Float.class));
        chromosome.setGene(1, new Cosine(gpConfiguration, Float.class));
        chromosome.setGene(2, new Variable(gpConfiguration, "x", Float.class));
        chromosome.setGene(3, constant);
        chromosome.redepth();
        return chromosome;
    }

    private GPConfiguration prepareGPConfiguration(int nodesMax) {
        GPConfiguration gpConfiguration = mock(GPConfiguration.class);
        RandomGenerator randomGenerator = mock(RandomGenerator.class);
        given(gpConfiguration.getRandomGenerator()).willReturn(randomGenerator);
        given(gpConfiguration.getPopulationSize()).willReturn(nodesMax);
        return gpConfiguration;
    }
}
