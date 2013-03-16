package prototype.evolution.fitness;

import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.ProgramChromosome;
import prototype.data.DataContainer;
import prototype.data.VariablesValues;
import prototype.differentiation.Function;
import prototype.differentiation.TreeNode;
import prototype.differentiation.TreeNodeFactory;
import prototype.differentiation.TreeNodeToFunctionTranslator;
import prototype.differentiation.functions.PreviousValueVariable;

import java.util.Arrays;
import java.util.List;

/**
 * User: koperek
 * Date: 10.03.13
 * Time: 23:32
 */
public class AbsoluteSquareErrorFitnessFunction extends GPFitnessFunction {

    private List<String> variablesNames;
    private final DataContainer dataContainer;

    public AbsoluteSquareErrorFitnessFunction(DataContainer dataContainer) {
        this.variablesNames = Arrays.asList(dataContainer.getVariableNames());
        this.dataContainer = dataContainer;
    }

    @Override
    protected double evaluate(IGPProgram a_subject) {
        double error = 0.0;

        for (int chromosomeIdx = 0; chromosomeIdx < a_subject.size(); chromosomeIdx++) {
            String chromosomeVariableName = variablesNames.get(chromosomeIdx);
            error += evaluateChromosome(a_subject.getChromosome(chromosomeIdx), chromosomeVariableName);
        }

        return error;
    }

    private double evaluateChromosome(ProgramChromosome chromosome, String chromosomeVariableName) {
        VariablesValues variablesValues = new VariablesValues();
        double chromosomeError = 0.0;
        TreeNode chromosomeAsTree = new TreeNodeFactory(variablesValues).createTreeNode(chromosome);
        Function chromosomeAsFunction = new TreeNodeToFunctionTranslator().translate(chromosomeAsTree);

        for (int i = 0; i < dataContainer.getRowsCount(); i++) {
            populateVariableValues(i, variablesValues);

            double chromosomeValueAtPointI = chromosomeAsFunction.evaluate();
            double dataPoint = dataContainer.getValue(chromosomeVariableName, i).doubleValue();

            double err = dataPoint - chromosomeValueAtPointI;
            chromosomeError += err * err;
        }
        return chromosomeError;
    }

    private void populateVariableValues(int dataRow, VariablesValues variablesValues) {
        for (String variableName : variablesNames) {
            variablesValues.setVariableValue(variableName, dataContainer.getValue(variableName, dataRow));
            String previousValueVariableName = variableName + PreviousValueVariable.PREVIOUS_VALUE_VARIABLE_SUFFIX;
            variablesValues.setVariableValue(previousValueVariableName, dataContainer.getValue(variableName, dataRow - 1));
        }
    }

    protected DataContainer getDataContainer() {
        return dataContainer;
    }

}