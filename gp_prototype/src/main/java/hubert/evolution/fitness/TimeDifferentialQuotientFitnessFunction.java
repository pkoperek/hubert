package hubert.evolution.fitness;

import org.apache.log4j.Logger;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.ProgramChromosome;
import hubert.data.MapVariablesValuesContainer;
import hubert.data.Pair;
import hubert.data.PairGenerator;
import hubert.data.VariablesValuesContainer;
import hubert.data.container.DataContainer;
import hubert.differentiation.numeric.NumericalDifferentiationCalculator;
import hubert.differentiation.symbolic.Function;
import hubert.differentiation.symbolic.TreeNode;
import hubert.differentiation.symbolic.TreeNodeFactory;
import hubert.differentiation.symbolic.functions.PreviousValueVariable;

import java.util.Arrays;
import java.util.List;

/**
 * User: koperek
 * Date: 11.02.13
 * Time: 19:22
 */
class TimeDifferentialQuotientFitnessFunction extends GPFitnessFunction {

    private static final Logger LOGGER = Logger.getLogger(TimeDifferentialQuotientFitnessFunction.class);

    private final List<Pair<String>> pairs;
    private final List<String> variables;
    private final DataContainer dataContainer;
    private final NumericalDifferentiationCalculator numericalDifferentiationCalculator;
    private final String timeVariableName;

    public TimeDifferentialQuotientFitnessFunction(DataContainer dataContainer, NumericalDifferentiationCalculator numericalDifferentiationCalculator) {
        this.pairs = new PairGenerator<String>().generatePairs(Arrays.asList(dataContainer.getVariableNames()));
        this.numericalDifferentiationCalculator = numericalDifferentiationCalculator;
        this.variables = Arrays.asList(dataContainer.getVariableNames());
        this.dataContainer = dataContainer;
        this.timeVariableName = dataContainer.getTimeVariable();

        if (this.timeVariableName == null) {
            throw new IllegalArgumentException("No time variable set! Set time variable or use implicit time!");
        }
    }

    @Override
    protected double evaluate(IGPProgram ind) {
        double error = 0.0f;
        long start = System.nanoTime();

        for (int chromosomeIdx = 0; chromosomeIdx < ind.size(); chromosomeIdx++) {
            ProgramChromosome chromosome = ind.getChromosome(chromosomeIdx);
            error += evaluatePairings(chromosome);
        }

        long stop = System.nanoTime();
        LOGGER.debug("Fitness Function Time: " + (stop - start) + " ns Error: " + error);

        return error;
    }

    private double evaluatePairings(ProgramChromosome chromosome) {
        double chromosomeError = 0.0f;

        for (Pair<String> pairing : pairs) {
            double pairingError = computeErrorForVariables(chromosome, pairing);

            if (pairingError > chromosomeError) {
                chromosomeError = pairingError;
            }
        }

        return chromosomeError;
    }

    private double computeErrorForVariables(ProgramChromosome chromosome, Pair<String> pairing) {
        VariablesValuesContainer valuesContainer = new MapVariablesValuesContainer();
        // TODO: pairing trzeba uwzglednic jesli ilosc zmiennych > 2
        TreeNodeFactory treeNodeFactory = new TreeNodeFactory(valuesContainer);
        TreeNode f = treeNodeFactory.createTreeNode(chromosome);

        String x = pairing.getOne();
        String y = pairing.getTwo();

        Function dfdx = f.differentiate(x);
        Function dfdy = f.differentiate(y);

        double pairingError = 0.0f;

        int validDataRows = 0;
        for (int dataRow = 0; dataRow < dataContainer.getRowsCount(); dataRow++) {
            if (numericalDifferentiationCalculator.hasDifferential(x, dataRow)
                    && numericalDifferentiationCalculator.hasDifferential(y, dataRow)) {

                valuesContainer.clear();
                populateVariableValues(dataRow, valuesContainer);

                double dfdx_val = dfdx.evaluate();
                double dfdy_val = dfdy.evaluate();

                double deltaX = numericalDifferentiationCalculator.getPartialDerivative(x, timeVariableName, dataRow);
                double deltaY = numericalDifferentiationCalculator.getPartialDerivative(y, timeVariableName, dataRow);

                deltaX = zero(deltaX);
                deltaY = zero(deltaY);
                dfdx_val = zero(dfdx_val);
                dfdy_val = zero(dfdy_val);

                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace(dfdx_val + " " + dfdy_val + " " + deltaX + " " + deltaY);
                }

                try {
                    // if any of denominators is 0 - discard data sample
                    if (isNotValidDataSample(dfdx_val, dfdy_val, deltaX, deltaY)) {
                        logInvalidDataSample(x, y, dataRow, dfdx_val, deltaY, deltaX, deltaY);
                    } else {
                        double result = Math.abs(deltaX / deltaY) - Math.abs(dfdy_val / dfdx_val);
                        pairingError += Math.log(1 + Math.abs(result));
                        validDataRows++;
                    }
                } catch (ArithmeticException ex) {
                    LOGGER.error("Problems with computing result from: " + dfdx + " | " + dfdy, ex);
                }
            }
        }

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(pairingError + " " + validDataRows + " " + pairingError / (double) validDataRows);
        }

        if (validDataRows == 0) {
            pairingError = Double.MAX_VALUE;
        } else {
            pairingError /= (double) validDataRows;
        }

        return pairingError;
    }

    private double zero(double value) {
        return value < 0.00000001d ? 0.0d : value;
    }

    private void logInvalidDataSample(String x, String y, int dataRow, double dfdx_val, double dfdy_val, double deltaX, double deltaY) {
        if (LOGGER.isDebugEnabled() || LOGGER.isTraceEnabled()) {
            LOGGER.debug("Discarding data sample: " +
                    " x: " + x +
                    " y: " + y +
                    " data row: " + dataRow +
                    " deltaX: " + deltaX +
                    " deltaY: " + deltaY +
                    " dfdx: " + dfdx_val +
                    " dfdy: " + dfdy_val);
        }
    }

    private boolean isNotValidDataSample(double dfdx_val, double dfdy_val, double deltaX, double deltaY) {
        return deltaX == 0.0 || deltaY == 0.0 || dfdx_val == 0.0 || dfdy_val == 0.0 || isNotValidNumber(dfdx_val) || isNotValidNumber(dfdy_val);
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
