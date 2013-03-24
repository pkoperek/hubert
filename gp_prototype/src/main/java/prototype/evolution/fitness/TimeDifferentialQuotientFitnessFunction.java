package prototype.evolution.fitness;

import org.apache.log4j.Logger;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.ProgramChromosome;
import prototype.data.Pair;
import prototype.data.PairGenerator;
import prototype.data.VariablesValues;
import prototype.data.container.DataContainer;
import prototype.differentiation.numeric.NumericalDifferentiationCalculator;
import prototype.differentiation.symbolic.Function;
import prototype.differentiation.symbolic.TreeNode;
import prototype.differentiation.symbolic.TreeNodeFactory;
import prototype.differentiation.symbolic.functions.PreviousValueVariable;

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
    private VariablesValues variablesValues = new VariablesValues();

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

        // mean log error - but not negative; the DeltaGPFitnessEvaluator
        // treats smaller values as better
        if (error != Double.MAX_VALUE) {
            error /= dataContainer.getRowsCount();
        }

        long stop = System.nanoTime();
        LOGGER.debug("Fitness Function Time: " + (stop - start) + " ns Error: " + error);

        return error;
    }

    private double evaluatePairings(ProgramChromosome chromosome) {
        double chromosomeError = 0.0f;

        for (Pair<String> pairing : pairs) {
            double pairingError = evaluatePairing(chromosome, pairing);

            if (pairingError > chromosomeError) {
                chromosomeError = pairingError;
            }
        }

        return chromosomeError;
    }

    private double evaluatePairing(ProgramChromosome chromosome, Pair<String> pairing) {
        // we need to assume variables are dependent - if all are independent there are no relations in data!
        TreeNodeFactory treeNodeFactory = new TreeNodeFactory(variablesValues, pairing);
        return computeErrorForVariables(treeNodeFactory.createTreeNode(chromosome), pairing.getOne(), pairing.getTwo());
    }

    private double computeErrorForVariables(TreeNode f, String x, String y) {
        Function dfdx = f.differentiate(x);
        Function dfdy = f.differentiate(y);

        double pairingError = 0.0f;

        variablesValues.clear();

        int validDataRows = 0;
        for (int dataRow = 0; dataRow < dataContainer.getRowsCount(); dataRow++) {
            if (numericalDifferentiationCalculator.hasDifferential(x, dataRow)
                    && numericalDifferentiationCalculator.hasDifferential(y, dataRow)) {

                populateVariableValues(dataRow, variablesValues);

                double dfdx_val = dfdx.evaluate();
                double dfdy_val = dfdy.evaluate();

                double deltaX = numericalDifferentiationCalculator.getDirectionalDerivative(x, timeVariableName, dataRow);
                double deltaY = numericalDifferentiationCalculator.getDirectionalDerivative(y, timeVariableName, dataRow);

                try {
                    // if any of denominators is 0 - discard data sample
                    if (isNotValidDataSample(dfdx_val, dfdy_val, deltaX, deltaY)) {
                        logInvalidDataSample(x, y, dataRow, dfdx_val, deltaY, deltaX, deltaY);
                    } else {
                        double result = (deltaX / deltaY) - (dfdy_val / dfdx_val);
                        pairingError += Math.log(1 + Math.abs(result));
                        validDataRows++;
                    }
                } catch (ArithmeticException ex) {
                    LOGGER.error("Problems with computing result from: " + dfdx + " | " + dfdy, ex);
                }
            }
        }

        if (validDataRows == 0) {
            pairingError = Double.MAX_VALUE;
        }

        return pairingError;
    }

    private void logInvalidDataSample(String x, String y, int dataRow, double dfdx_val, double dfdy_val, double deltaX, double deltaY) {
        if (LOGGER.isDebugEnabled()) {
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

    private void populateVariableValues(int dataRow, VariablesValues variablesValues) {
        for (String variableName : variables) {
            variablesValues.setVariableValue(variableName, dataContainer.getValue(variableName, dataRow));
            String previousValueVariableName = variableName + PreviousValueVariable.PREVIOUS_VALUE_VARIABLE_SUFFIX;
            variablesValues.setVariableValue(previousValueVariableName, dataContainer.getValue(variableName, dataRow - 1));
        }
    }
}
