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
    private DifferencesProvider differencesProvider;

    public EureqaFitnessFunction(DataContainer dataContainer) {
        this.variables = Arrays.asList(dataContainer.getVariableNames());
        this.dataContainer = dataContainer;
        this.pairs = new PairGenerator<String>().generatePairs(variables);
        this.differencesProvider = new DifferencesProvider(dataContainer);
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
        error /= dataContainer.rowsCount();

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
        String firstVariableName = pairing.getOne();
        String secondVariableName = pairing.getTwo();

        TreeNodeFactory treeNodeFactory = new TreeNodeFactory(variablesValues, pairing);
        TreeNode chromosomeAsTree = treeNodeFactory.createTreeNode(chromosome);
        TreeNode equationReversed = reverseEquation(chromosomeAsTree, firstVariableName, secondVariableName, variablesValues);

        Function firstDifferentiated = equationReversed.differentiate(firstVariableName);
        Function secondDifferentiated = equationReversed.differentiate(secondVariableName);

        double pairingError = 0.0f;
        for (int dataRow = 1; dataRow < dataContainer.rowsCount(); dataRow++) {
            populateVariableValues(dataRow, variablesValues);

            double firstDifferentiatedValue = firstDifferentiated.evaluate();
            double secondDifferentiatedValue = secondDifferentiated.evaluate();

            double firstDifference = differencesProvider.getDifference(firstVariableName, dataRow).doubleValue();
            double secondDifference = differencesProvider.getDifference(secondVariableName, dataRow).doubleValue();

            try {
                // if any of denominators is 0 - discard data sample
                if (secondDifference == 0.0 || firstDifferentiatedValue == 0.0) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Discarding data sample: " +
                                secondVariableName + " " +
                                dataRow + " " +
                                firstDifferentiated + " " +
                                secondDifference + " " +
                                firstDifferentiatedValue);
                    }
                    pairingError += 1.0f;
                    continue;
                }

                if (Double.isNaN(firstDifferentiatedValue) || Double.isInfinite(firstDifferentiatedValue) ||
                        Double.isNaN(secondDifferentiatedValue) || Double.isInfinite(secondDifferentiatedValue)) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Discarding data sample: " +
                                dataRow + " " +
                                firstDifferentiatedValue + " " +
                                secondDifferentiatedValue);
                    }
                    pairingError += 1.0f;
                    continue;
                }

                double result = (firstDifference / secondDifference) - (secondDifferentiatedValue / firstDifferentiatedValue);
                pairingError += Math.log(1 + Math.abs(result));
            } catch (ArithmeticException ex) {
                LOGGER.error("Problems with computing result from: " + firstDifferentiated + " | " + secondDifferentiated, ex);
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
