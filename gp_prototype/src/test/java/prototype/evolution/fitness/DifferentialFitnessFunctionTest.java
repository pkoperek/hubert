package prototype.evolution.fitness;

import org.jgap.gp.function.Sine;
import org.jgap.gp.function.Subtract;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.GPProgram;
import org.jgap.gp.impl.ProgramChromosome;
import org.jgap.gp.terminal.Variable;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import prototype.data.DataContainer;
import prototype.data.DataContainerFactory;
import prototype.differentiation.numeric.NumericalDifferentiationCalculator;
import prototype.differentiation.numeric.NumericalDifferentiationCalculatorFactory;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * User: koperek
 * Date: 19.03.13
 * Time: 00:15
 */
public class DifferentialFitnessFunctionTest {

    private DifferentialFitnessFunction differentialFitnessFunction;
    private DataContainer dataContainer;

    @Before
    public void setUp() throws Exception {

        dataContainer = new DataContainerFactory("src/test/resources/sin_implicit_time.csv").getDataContainer();
        NumericalDifferentiationCalculator numericalDifferentiationCalculator =
                new NumericalDifferentiationCalculatorFactory()
                        .createCalculator(NumericalDifferentiationCalculatorFactory.CalculatorType.CENTRAL, dataContainer);
        differentialFitnessFunction = new DifferentialFitnessFunction("sin", dataContainer, numericalDifferentiationCalculator);
    }

    @Ignore
    @Test
    public void shouldAcceptExactSolution() throws Exception {

        // Given
        GPConfiguration gpConfiguration = mock(GPConfiguration.class);
        GPProgram gpProgram = new GPProgram(gpConfiguration, 1);
        given(gpConfiguration.getPopulationSize()).willReturn(4);
        ProgramChromosome chromosome = new ProgramChromosome(gpConfiguration);
        chromosome.setGene(0, new Subtract(gpConfiguration, Float.class));
        chromosome.setGene(1, new Sine(gpConfiguration, Float.class));
        chromosome.setGene(2, new Variable(gpConfiguration, "x", Float.class));
        chromosome.setGene(3, new Variable(gpConfiguration, "sin", Float.class));
        chromosome.redepth();

        gpProgram.setChromosome(0, chromosome);

        // When
        differentialFitnessFunction.evaluate(gpProgram);

        // Then

    }
}
