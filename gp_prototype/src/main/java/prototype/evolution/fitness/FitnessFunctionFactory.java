package prototype.evolution.fitness;

import org.constretto.annotation.Configuration;
import org.constretto.annotation.Configure;
import org.jgap.gp.GPFitnessFunction;
import prototype.data.DataContainer;
import prototype.differentiation.numeric.NumericalDifferentiationCalculator;
import prototype.differentiation.numeric.NumericalDifferentiationCalculatorFactory;

public class FitnessFunctionFactory {

    private static final NumericalDifferentiationCalculatorFactory DIFFERENTIATION_CALCULATOR_FACTORY = new NumericalDifferentiationCalculatorFactory();

    private NumericalDifferentiationCalculatorFactory.CalculatorType calculatorType;
    private FitnessFunctionType functionType;
    private String variableName;

    public enum FitnessFunctionType {
        DIFF, ABSERR, ABSSQERR, DIFFQ, MEANABSERR
    }

    public GPFitnessFunction createFitnessFunction(DataContainer dataContainer) {
        NumericalDifferentiationCalculator numericalDifferentiationCalculator =
                DIFFERENTIATION_CALCULATOR_FACTORY.createCalculator(calculatorType, dataContainer);

        switch (functionType) {
            case DIFF:
                return new DifferentialFitnessFunction(variableName, dataContainer, numericalDifferentiationCalculator);
            case ABSERR:
                return new AbsoluteErrorFitnessFunction(dataContainer);
            case ABSSQERR:
                return new AbsoluteSquareErrorFitnessFunction(dataContainer);
            case MEANABSERR:
                return new MeanAbsoluteErrorFitnessFunction(dataContainer);
            case DIFFQ:
                return new DifferentialQuotientFitnessFunction(dataContainer, numericalDifferentiationCalculator);
        }

        throw new IllegalArgumentException("Unsupported function type!" + functionType);
    }

    @Configure
    public void setVariableName(@Configuration(value = "fitness.variable.name") String variableName) {
        this.variableName = variableName;
    }

    @Configure
    public void setFunctionType(@Configuration(value = "fitness.type", defaultValue = "ABSERR") String functionType) {
        this.functionType = FitnessFunctionType.valueOf(functionType);
    }

    public void setCalculatorType(NumericalDifferentiationCalculatorFactory.CalculatorType calculatorType) {
        this.calculatorType = calculatorType;
    }

    public void setFunctionType(FitnessFunctionType functionType) {
        this.functionType = functionType;
    }

    @Configure
    public void setCalculatorType(@Configuration(value = "fitness.calculator.type", defaultValue = "ABSOLUTE_ERROR") String calculatorType) {
        this.calculatorType =
                NumericalDifferentiationCalculatorFactory
                        .CalculatorType.valueOf(calculatorType.toUpperCase());
    }
}