package prototype.evolution.fitness;

import org.apache.log4j.Logger;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.ProgramChromosome;
import prototype.data.DataContainer;
import prototype.data.Pair;
import prototype.data.PairGenerator;
import prototype.data.VariablesValues;
import prototype.differentiation.numeric.NumericalDifferentiationCalculator;
import prototype.differentiation.symbolic.Function;
import prototype.differentiation.symbolic.FunctionType;
import prototype.differentiation.symbolic.TreeNode;
import prototype.differentiation.symbolic.TreeNodeFactory;
import prototype.differentiation.symbolic.functions.PreviousValueVariable;
import prototype.differentiation.symbolic.tree.SimpleTreeNode;
import prototype.differentiation.symbolic.tree.VariableTreeNode;

import java.util.Arrays;
import java.util.List;

/**
 * User: koperek
 * Date: 11.02.13
 * Time: 19:22
 */
public class EureqaFitnessFunction extends GPFitnessFunction {

    private static final Logger LOGGER = Logger.getLogger(EureqaFitnessFunction.class);

    private final List<Pair<String>> pairs;
    private final List<String> variables;
    private final DataContainer dataContainer;
    private final NumericalDifferentiationCalculator numericalDifferentiationCalculator;
    private VariablesValues variablesValues = new VariablesValues();

    public EureqaFitnessFunction(DataContainer dataContainer, NumericalDifferentiationCalculator numericalDifferentiationCalculator) {
        this.pairs = new PairGenerator<String>().generatePairs(Arrays.asList(dataContainer.getVariableNames()));
        this.numericalDifferentiationCalculator = numericalDifferentiationCalculator;
        this.variables = Arrays.asList(dataContainer.getVariableNames());
        this.dataContainer = dataContainer;
    }

    @Override
    protected double evaluate(IGPProgram ind) {
        double error = 0.0f;
        long start = System.nanoTime();

        for (int chromosomeIdx = 0; chromosomeIdx < ind.size(); chromosomeIdx++) {
            String chromosomeVariableName = dataContainer.getVariableName(chromosomeIdx);
            ProgramChromosome chromosome = ind.getChromosome(chromosomeIdx);

            error += evaluatePairings(chromosomeVariableName, chromosome);
        }

        // mean log error - but not negative; the DeltaGPFitnessEvaluator
        // treats smaller values as better
        error /= dataContainer.getRowsCount();

        long stop = System.nanoTime();
        LOGGER.debug("Fitness Function Time: " + (stop - start) + " ns Error: " + error);
        if (error < 0.000001) {
            error = 0.0d;
        }

        return error;
    }

    private double evaluatePairings(String chromosomeVariableName, ProgramChromosome chromosome) {
        double chromosomeError = 0.0f;

        for (Pair<String> pairing : pairs) {
            double pairingError = evaluateWithAndWithoutDependencies(chromosome, pairing);

            if (pairingError > chromosomeError) {
                chromosomeError = pairingError;
            }
        }

        return chromosomeError;
    }

    private double evaluateWithAndWithoutDependencies(ProgramChromosome chromosome, Pair<String> pairing) {
        TreeNodeFactory withDependencies = new TreeNodeFactory(variablesValues, pairing);
        TreeNodeFactory withoutDependencies = new TreeNodeFactory(variablesValues);

        double pairingErrorWithDeps = evaluatePairing(withDependencies.createTreeNode(chromosome), pairing.getOne(), pairing.getTwo());
        double pairingErrorWithoutDeps = evaluatePairing(withoutDependencies.createTreeNode(chromosome), pairing.getOne(), pairing.getTwo());

        return pairingErrorWithDeps > pairingErrorWithoutDeps ? pairingErrorWithDeps : pairingErrorWithoutDeps;
    }

    private double evaluatePairing(TreeNode f, String x, String y) {
        Function dfdx = f.differentiate(x);
        Function dfdy = f.differentiate(y);

        double pairingError = 0.0f;

        variablesValues.clear();
        for (int dataRow = 0; dataRow < dataContainer.getRowsCount(); dataRow++) {
            if (numericalDifferentiationCalculator.hasDifferential(x, dataRow)
                    && numericalDifferentiationCalculator.hasDifferential(y, dataRow)) {
                populateVariableValues(dataRow, variablesValues);

                double dfdx_val = dfdx.evaluate();
                double dfdy_val = dfdy.evaluate();

                double deltaX = numericalDifferentiationCalculator.getDifferential(x, dataRow).doubleValue();
                double deltaY = numericalDifferentiationCalculator.getDifferential(y, dataRow).doubleValue();

                try {
                    // if any of denominators is 0 - discard data sample
                    if (isNotValidDataSample(dfdx_val, dfdy_val, deltaY)) {
                        logInvalidDataSample(x, y, dataRow, dfdx_val, deltaY);
                        pairingError += 2.0f;
                        continue;
                    }

                    double result = (deltaX / deltaY) - (dfdy_val / dfdx_val);
                    pairingError += Math.log(1 + Math.abs(result));
                } catch (ArithmeticException ex) {
                    LOGGER.error("Problems with computing result from: " + dfdx + " | " + dfdy, ex);
                }
            }
        }
        return pairingError;
    }

    private void logInvalidDataSample(String x, String y, int dataRow, double dfdx_val, double deltaY) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Discarding data sample: " +
                    " x:" + x +
                    " y: " + y +
                    " data row: " + dataRow +
                    " deltaY: " + deltaY +
                    " dfdx: " + dfdx_val +
                    " dfdy: " + dfdx_val);
        }
    }

    private boolean isNotValidDataSample(double dfdx_val, double dfdy_val, double deltaY) {
        return deltaY == 0.0 || dfdx_val == 0.0 || isNotValidNumber(dfdx_val) || isNotValidNumber(dfdy_val);
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
