package hubert.evolution.fitness;

import org.constretto.annotation.Configuration;
import org.constretto.annotation.Configure;
import hubert.differentiation.numeric.NumericalDifferentiationCalculatorFactory;

public class FitnessFunctionConfiguration {

    public enum FitnessFunctionType {
        TIME_DERIV, ABSERR, ABSSQERR, DIFFQ, MEANABSERR, TLESS_DIFFQ
    }

    public NumericalDifferentiationCalculatorFactory.CalculatorType calculatorType;

    public NumericalDifferentiationCalculatorFactory.CalculatorType getCalculatorType() {
        return calculatorType;
    }

    public FitnessFunctionType functionType;

    public FitnessFunctionType getFunctionType() {
        return functionType;
    }

    public String variableName;

    public String getVariableName() {
        return variableName;
    }

    public FitnessFunctionConfiguration() {
    }

    @Configure
    public void setVariableName(@Configuration(value = "fitness.variable.name", required = false) String variableName) {
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
    public void setCalculatorType(@Configuration(value = "fitness.diffcalculator.type", defaultValue = "CENTRAL") String calculatorType) {
        this.calculatorType =
                NumericalDifferentiationCalculatorFactory
                        .CalculatorType.valueOf(calculatorType.toUpperCase());
    }
}