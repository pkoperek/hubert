package hubert.evolution.fitness;

import org.jgap.gp.GPFitnessFunction;
import hubert.data.container.DataContainer;
import hubert.differentiation.numeric.NumericalDifferentiationCalculator;
import hubert.differentiation.numeric.NumericalDifferentiationCalculatorFactory;

public class FitnessFunctionFactory {

    private static final NumericalDifferentiationCalculatorFactory DIFFERENTIATION_CALCULATOR_FACTORY = new NumericalDifferentiationCalculatorFactory();

    public GPFitnessFunction createFitnessFunction(FitnessFunctionConfiguration fitnessFunctionConfiguration, DataContainer dataContainer) {
        NumericalDifferentiationCalculator numericalDifferentiationCalculator =
                DIFFERENTIATION_CALCULATOR_FACTORY.createCalculator(fitnessFunctionConfiguration.getCalculatorType(), dataContainer);

        switch (fitnessFunctionConfiguration.getFunctionType()) {
            case TIME_DERIV:
                return new FindTimeDerivativeFitnessFunction(fitnessFunctionConfiguration.getVariableName(), dataContainer, numericalDifferentiationCalculator);
            case ABSERR:
                return new AllChromosomesAbsoluteErrorFitnessFunction(dataContainer);
            case ABSSQERR:
                return new AllChromosomesAbsoluteSquareErrorFitnessFunction(dataContainer);
            case MEANABSERR:
                return new AllChromosomesMeanAbsoluteErrorFitnessFunction(dataContainer);
            case DIFFQ:
                return new TimeDifferentialQuotientFitnessFunction(dataContainer, numericalDifferentiationCalculator);
            case TLESS_DIFFQ:
                return new TimelessDifferentialQuotientFitnessFunction(dataContainer, numericalDifferentiationCalculator);
        }

        throw new IllegalArgumentException("Unsupported function type!" + fitnessFunctionConfiguration.getFunctionType());
    }

}