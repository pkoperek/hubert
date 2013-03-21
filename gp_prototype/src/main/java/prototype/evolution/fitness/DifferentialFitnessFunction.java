package prototype.evolution.fitness;

import org.apache.log4j.Logger;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.ProgramChromosome;
import prototype.data.DataContainer;
import prototype.data.VariablesValues;
import prototype.differentiation.numeric.NumericalDifferentiationCalculator;
import prototype.differentiation.symbolic.*;
import prototype.differentiation.symbolic.functions.PreviousValueVariable;
import prototype.differentiation.symbolic.tree.SimpleTreeNode;
import prototype.differentiation.symbolic.tree.VariableTreeNode;

import java.util.Arrays;
import java.util.List;

/**
 * User: koperek
 * Date: 17.03.13
 * Time: 22:48
 */
public class DifferentialFitnessFunction extends GPFitnessFunction {
    private static final Logger LOGGER = Logger.getLogger(DifferentialQuotientFitnessFunction.class);

    private final List<String> variables;
    private final DataContainer dataContainer;
    private final NumericalDifferentiationCalculator numericalDifferentiationCalculator;
    private VariablesValues variablesValues = new VariablesValues();

    public DifferentialFitnessFunction(DataContainer dataContainer, NumericalDifferentiationCalculator numericalDifferentiationCalculator) {
        this.numericalDifferentiationCalculator = numericalDifferentiationCalculator;
        this.variables = Arrays.asList(dataContainer.getVariableNames());
        this.dataContainer = dataContainer;
    }

    @Override
    protected double evaluate(IGPProgram ind) {
        double error = 0.0f;
        long start = System.nanoTime();

        for (int chromosomeIdx = 0; chromosomeIdx < ind.size(); chromosomeIdx++) {
            ProgramChromosome chromosome = ind.getChromosome(chromosomeIdx);
            String variableName = dataContainer.getVariableName(chromosomeIdx);
            error += evaluateChromosome(chromosome, variableName);
        }

        // mean log error - but not negative; the DeltaGPFitnessEvaluator
        // treats smaller values as better
        if (error != Double.MAX_VALUE) {
            error /= dataContainer.getRowsCount();
        }

        long stop = System.nanoTime();
        LOGGER.debug("Fitness Function Time: " + (stop - start) + " ns Error: " + error);
        if (error < 0.000001) {
            error = 0.0d;
        }

        return error;
    }

    private double evaluateChromosome(ProgramChromosome chromosome, String variable) {
        TreeNodeFactory treeNodeFactory = new TreeNodeFactory(variablesValues);
        return computeErrorForVariable(treeNodeFactory.createTreeNode(chromosome), variable);
    }

    private double computeErrorForVariable(TreeNode f, String x) {
        Function function = new TreeNodeToFunctionTranslator().translate(f);
        variablesValues.clear();
        double errorForVariable = 0.0f;

        int validDataRows = 0;
        for (int dataRow = 0; dataRow < dataContainer.getRowsCount(); dataRow++) {
            if (numericalDifferentiationCalculator.hasDifferential(x, dataRow)) {
                populateVariableValues(dataRow, variablesValues);

                double f_x = function.evaluate();

                double deltaX = numericalDifferentiationCalculator.getDifferential(x, dataRow);

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

    private TreeNode reverseEquation(TreeNode chromosomeAsTree, String firstVariableName, String interdependentVariableName, VariablesValues variablesValues) {
        return new SimpleTreeNode(
                FunctionType.SUBTRACT,
                new TreeNode[]{
                        chromosomeAsTree,
                        new VariableTreeNode(variablesValues, firstVariableName, interdependentVariableName)
                }
        );
    }

    private void populateVariableValues(int dataRow, VariablesValues variablesValues) {
        for (String variableName : variables) {
            variablesValues.setVariableValue(variableName, dataContainer.getValue(variableName, dataRow));
            String previousValueVariableName = variableName + PreviousValueVariable.PREVIOUS_VALUE_VARIABLE_SUFFIX;
            variablesValues.setVariableValue(previousValueVariableName, dataContainer.getValue(variableName, dataRow - 1));
        }
    }
}
