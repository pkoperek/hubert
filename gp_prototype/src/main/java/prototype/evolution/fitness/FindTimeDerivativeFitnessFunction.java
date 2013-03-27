package prototype.evolution.fitness;

import org.apache.log4j.Logger;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.ProgramChromosome;
import prototype.data.VariablesValues;
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
    private final VariablesValues variablesValues = new VariablesValues();
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
        TreeNodeFactory treeNodeFactory = new TreeNodeFactory(variablesValues);
        String x = variableName;
        Function function = new TreeNodeToFunctionTranslator().translate(treeNodeFactory.createTreeNode(chromosome));
        variablesValues.clear();
        double errorForVariable = 0.0f;

        int validDataRows = 0;
        for (int dataRow = 0; dataRow < dataContainer.getRowsCount(); dataRow++) {
            if (numericalDifferentiationCalculator.hasDifferential(x, dataRow)) {
                populateVariableValues(dataRow, variablesValues);

                double f_x = function.evaluate();

                double deltaX = numericalDifferentiationCalculator.getDirectionalDerivative(x, timeVariableName, dataRow);

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

    private void populateVariableValues(int dataRow, VariablesValues variablesValues) {
        for (String variableName : variables) {
            variablesValues.setVariableValue(variableName, dataContainer.getValue(variableName, dataRow));
            String previousValueVariableName = variableName + PreviousValueVariable.PREVIOUS_VALUE_VARIABLE_SUFFIX;
            variablesValues.setVariableValue(previousValueVariableName, dataContainer.getValue(variableName, dataRow - 1));
        }
    }
}