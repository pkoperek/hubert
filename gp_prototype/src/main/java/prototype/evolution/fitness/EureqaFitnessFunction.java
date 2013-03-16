package prototype.evolution.fitness;

import org.apache.log4j.Logger;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.ProgramChromosome;
import prototype.data.*;
import prototype.differentiation.*;
import prototype.differentiation.functions.PreviousValueVariable;
import prototype.differentiation.tree.SimpleTreeNode;
import prototype.differentiation.tree.VariableTreeNode;

import java.util.Arrays;
import java.util.List;

/**
 * User: koperek
 * Date: 11.02.13
 * Time: 19:22
 */
public class EureqaFitnessFunction extends GPFitnessFunction {

    private static final Logger LOGGER = Logger.getLogger(EureqaFitnessFunction.class);

    private TreeNodeToFunctionTranslator treeNodeToFunctionTranslator = new TreeNodeToFunctionTranslator();
    private List<Pair<String>> pairs;
    private List<String> variables;
    private final DataContainer dataContainer;
    private NumericalDifferentiationCalculator numericalDifferentiationCalculator;

    public EureqaFitnessFunction(DataContainer dataContainer) {
        this.variables = Arrays.asList(dataContainer.getVariableNames());
        this.dataContainer = dataContainer;
        this.pairs = new PairGenerator<String>().generatePairs(variables);
        this.numericalDifferentiationCalculator = new NumericalDifferentiationCalculator(dataContainer);
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
            double pairingError = evaluatePairing(chromosome, pairing);

            if (pairingError > chromosomeError) {
                chromosomeError = pairingError;
            }
        }

        return chromosomeError;
    }

    private double evaluatePairing(ProgramChromosome chromosome, Pair<String> pairing) {
        VariablesValues variablesValues = new VariablesValues();
        String x = pairing.getOne();
        String y = pairing.getTwo();

        TreeNodeFactory treeNodeFactory = new TreeNodeFactory(variablesValues, pairing);
        TreeNode f = treeNodeFactory.createTreeNode(chromosome);

        Function dfdx = f.differentiate(x);
        Function dfdy = f.differentiate(y);

        double pairingError = 0.0f;
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
                    if (deltaY == 0.0 || dfdx_val == 0.0) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("Discarding data sample: " +
                                    y + " " +
                                    dataRow + " " +
                                    dfdx + " " +
                                    deltaY + " " +
                                    dfdx_val);
                        }
                        pairingError += 2.0f;
                        continue;
                    }

                    if (Double.isNaN(dfdx_val) || Double.isInfinite(dfdx_val) ||
                            Double.isNaN(dfdy_val) || Double.isInfinite(dfdy_val)) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("Discarding data sample: " +
                                    dataRow + " " +
                                    dfdx_val + " " +
                                    dfdy_val);
                        }
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
