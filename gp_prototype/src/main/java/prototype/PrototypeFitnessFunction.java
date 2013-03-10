package prototype;

import org.apache.log4j.Logger;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.ProgramChromosome;
import org.jgap.gp.terminal.Variable;
import prototype.differentiation.Function;
import prototype.differentiation.TreeNodeFactory;
import prototype.differentiation.functions.PreviousValueVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * User: koperek
 * Date: 11.02.13
 * Time: 19:22
 */
public class PrototypeFitnessFunction extends GPFitnessFunction {

    private static final Logger LOGGER = Logger.getLogger(PrototypeFitnessFunction.class);

    private List<Pair<String>> pairs;
    private Variable[] variables;
    private final DataContainer dataContainer;
    private DifferencesProvider differencesProvider;

    public PrototypeFitnessFunction(Variable[] variables, DataContainer dataContainer) {
        this.variables = variables;
        this.dataContainer = dataContainer;
        this.pairs = new PairGenerator<String>().generatePairs(toVariableNames(variables));
        this.differencesProvider = new DifferencesProvider(dataContainer);
    }

    private List<String> toVariableNames(Variable[] variables) {
        List<String> variableNames = new ArrayList<>();

        for (Variable variable : variables) {
            variableNames.add(variable.getName());
        }

        return variableNames;
    }

    @Override
    protected double evaluate(IGPProgram ind) {
        double error = 0.0f;
        ProgramChromosome chromosome = ind.getChromosome(0);
        VariablesValues variablesValues = new VariablesValues();

        long start = System.nanoTime();

        // pick a pairing
        for (Pair<String> pairing : pairs) {
            String firstVariableName = pairing.getOne();
            String secondVariableName = pairing.getTwo();

            TreeNodeFactory treeNodeFactory = new TreeNodeFactory(variablesValues, pairing);
            Function firstDifferentiated = treeNodeFactory.createTreeNode(chromosome).differentiate(firstVariableName);
            Function secondDifferentiated = treeNodeFactory.createTreeNode(chromosome).differentiate(secondVariableName);

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

            if (pairingError > error) {
                error = pairingError;
            }
        }

        // mean log error - but not negative; the DeltaGPFitnessEvaluator
        // treats smaller values as better
        error /= dataContainer.rowsCount();

//        // if this individual discarded all rows for all pairings - throw it out...
//        if (processedDataRows == 0) {
//            error = Double.MAX_VALUE;
//        }

        long stop = System.nanoTime();
        LOGGER.debug("Fitness Function Time: " + (stop - start) + " ns Error: " + error);
        if (error < 0.000001) {
            error = 0.0d;
        }

        return error;
    }

    private void populateVariableValues(int dataRow, VariablesValues variablesValues) {
        for (Variable variable : variables) {
            String variableName = variable.getName();
            variablesValues.setVariableValue(variableName, dataContainer.getValue(variableName, dataRow));
            String previousValueVariableName = variable.getName() + PreviousValueVariable.PREVIOUS_VALUE_VARIABLE_SUFFIX;
            variablesValues.setVariableValue(previousValueVariableName, dataContainer.getValue(variableName, dataRow - 1));
        }
    }
}
