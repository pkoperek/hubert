package prototype.evolution.fitness;

import org.apache.log4j.Logger;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.ProgramChromosome;
import prototype.data.MapVariablesValuesContainer;
import prototype.data.VariablesValuesContainer;
import prototype.data.container.DataContainer;
import prototype.differentiation.numeric.NumericalDifferentiationCalculator;
import prototype.differentiation.symbolic.Function;
import prototype.differentiation.symbolic.TreeNodeFactory;
import prototype.differentiation.symbolic.TreeNodeToFunctionTranslator;
import prototype.differentiation.symbolic.functions.PreviousValueVariable;

import java.util.Arrays;
import java.util.List;

/**
 * User: koperek
 * Date: 17.03.13
 * Time: 22:48
 */
class FindTimeDerivativeFitnessFunction extends GPFitnessFunction {
    private static final Logger LOGGER = Logger.getLogger(TimeDifferentialQuotientFitnessFunction.class);

    private final List<String> variables;
    private final DataContainer dataContainer;
    private final NumericalDifferentiationCalculator numericalDifferentiationCalculator;
    private final String variableName;
    private final String timeVariableName;

    public FindTimeDerivativeFitnessFunction(String variableName, DataContainer dataContainer, NumericalDifferentiationCalculator numericalDifferentiationCalculator) {
        this.numericalDifferentiationCalculator = numericalDifferentiationCalculator;
        this.variables = Arrays.asList(dataContainer.getVariableNames());
        this.dataContainer = dataContainer;
        this.variableName = variableName;
        this.timeVariableName = dataContainer.getTimeVariable();
    }

    @Override
    protected double evaluate(IGPProgram ind) {
        double error = 0.0f;
        long start = System.nanoTime();

        for (int chromosomeIdx = 0; chromosomeIdx < ind.size(); chromosomeIdx++) {
            ProgramChromosome chromosome = ind.getChromosome(chromosomeIdx);
            error += computeChromosomeError(chromosome);
        }

        // mean log error - but not negative; the DeltaGPFitnessEvaluator
        // treats smaller values as better
        if (error != Double.MAX_VALUE) {
            error /= dataContainer.getRowsCount();
        }

        long stop = System.nanoTime();
        LOGGER.debug("Fitness Function Time: " + (stop - start) + " ns Error: " + error);
        return error;
    }

    private double computeChromosomeError(ProgramChromosome chromosome) {
        VariablesValuesContainer variablesValuesContainer = new MapVariablesValuesContainer();
        TreeNodeFactory treeNodeFactory = new TreeNodeFactory(variablesValuesContainer);
        String x = variableName;
        Function function = new TreeNodeToFunctionTranslator().translate(treeNodeFactory.createTreeNode(chromosome));
        variablesValuesContainer.clear();
        double errorForVariable = 0.0f;

        int validDataRows = 0;
        for (int dataRow = 0; dataRow < dataContainer.getRowsCount(); dataRow++) {
            if (numericalDifferentiationCalculator.hasDifferential(x, dataRow)) {
                variablesValuesContainer.clear();
                populateVariableValues(dataRow, variablesValuesContainer);

                double f_x = function.evaluate();

                double deltaX = numericalDifferentiationCalculator.getPartialDerivative(x, timeVariableName, dataRow);

                try {
                    // if any of denominators is 0 - discard data sample
                    if (isNotValidDataSample(f_x, deltaX)) {
                        logInvalidDataSample(x, dataRow, f_x, deltaX);
                    } else {
                        errorForVariable += Math.abs(deltaX - f_x);
                        validDataRows++;
                    }
                } catch (ArithmeticException ex) {
                    LOGGER.error("Problems with computing result from: " + function, ex);
                }
            }
        }

        if (validDataRows == 0) {
            errorForVariable = Double.MAX_VALUE;
        }

        return errorForVariable;
    }

    private void logInvalidDataSample(String x, int dataRow, double dfdx_val, double deltaX) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Discarding data sample: " +
                    " x: " + x +
                    " data row: " + dataRow +
                    " deltaX: " + deltaX +
                    " dfdx: " + dfdx_val);
        }
    }

    private boolean isNotValidDataSample(double dfdx_val, double deltaX) {
        return deltaX == 0.0 || dfdx_val == 0.0 || isNotValidNumber(dfdx_val);
    }

    private boolean isNotValidNumber(double dfdx_val) {
        return Double.isNaN(dfdx_val) || Double.isInfinite(dfdx_val);
    }

    private void populateVariableValues(int dataRow, VariablesValuesContainer variablesValuesContainer) {
        for (String variableName : variables) {
            variablesValuesContainer.setVariableValue(variableName, dataContainer.getValue(variableName, dataRow));
            String previousValueVariableName = variableName + PreviousValueVariable.PREVIOUS_VALUE_VARIABLE_SUFFIX;
            variablesValuesContainer.setVariableValue(previousValueVariableName, dataContainer.getValue(variableName, dataRow - 1));
        }
    }
}
