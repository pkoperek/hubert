package hubert.evolution.fitness;

import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.ProgramChromosome;
import hubert.data.MapVariablesValuesContainer;
import hubert.data.VariablesValuesContainer;
import hubert.data.container.DataContainer;
import hubert.differentiation.symbolic.Function;
import hubert.differentiation.symbolic.TreeNode;
import hubert.differentiation.symbolic.TreeNodeFactory;
import hubert.differentiation.symbolic.TreeNodeToFunctionTranslator;

import java.util.Arrays;
import java.util.List;

/**
 * User: koperek
 * Date: 10.03.13
 * Time: 23:32
 */
class AllChromosomesAbsoluteSquareErrorFitnessFunction extends GPFitnessFunction {

    private List<String> variablesNames;
    private final DataContainer dataContainer;

    public AllChromosomesAbsoluteSquareErrorFitnessFunction(DataContainer dataContainer) {
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
        VariablesValuesContainer variablesValuesContainer = new MapVariablesValuesContainer();
        double chromosomeError = 0.0;
        TreeNode chromosomeAsTree = new TreeNodeFactory(variablesValuesContainer).createTreeNode(chromosome);
        Function chromosomeAsFunction = new TreeNodeToFunctionTranslator().translate(chromosomeAsTree);

        for (int i = 0; i < dataContainer.getRowsCount(); i++) {
            populateVariableValues(i, variablesValuesContainer);

            double chromosomeValueAtPointI = chromosomeAsFunction.evaluate();
            double dataPoint = dataContainer.getValue(chromosomeVariableName, i);

            double err = dataPoint - chromosomeValueAtPointI;
            chromosomeError += err * err;
        }
        return chromosomeError;
    }

    private void populateVariableValues(int dataRow, VariablesValuesContainer variablesValuesContainer) {
        for (String variableName : variablesNames) {
            variablesValuesContainer.setVariableValue(variableName, dataContainer.getValue(variableName, dataRow));
        }
    }

    protected DataContainer getDataContainer() {
        return dataContainer;
    }

}
