package hubert.data.container;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * User: koperek
 * Date: 14.02.13
 * Time: 19:39
 */
class DefaultDataContainer implements DataContainer {

    private final Map<String, VariableSeries> data = new HashMap<String, VariableSeries>();
    private int rowsCount = 0;
    private String[] variableNames;
    private final String timeVariable;

    public DefaultDataContainer(String timeVariable) {
        this.timeVariable = timeVariable;
    }

    @Override
    public void initializeVariables(String[] variableNames) {
        this.variableNames = variableNames;

        data.clear();

        for (String variableName : variableNames) {
            data.put(variableName, new VariableSeries(variableName));
        }
    }

    @Override
    public double getValue(String variable, int row) {
        assumeVariablePresent(variable);

        VariableSeries numbers = data.get(variable);
        return numbers.get(row);
    }

    private void assumeVariablePresent(String variable) {
        if (!data.containsKey(variable)) {
            throw new IllegalArgumentException("Variable: " + variable + " not supported!");
        }
    }

    @Override
    public int getRowsCount() {
        return rowsCount;
    }

    @Override
    public int getVariablesCount() {
        return variableNames.length;
    }

    @Override
    public void addValue(String variable, double value) {
        assumeVariablePresent(variable);

        VariableSeries values = data.get(variable);
        values.add(value);

        if (values.getDataRowsCount() > rowsCount) {
            rowsCount = values.getDataRowsCount();
        }
    }

    @Override
    public String[] getVariableNames() {
        return Arrays.copyOf(variableNames, variableNames.length);
    }

    @Override
    public String getVariableName(int i) {
        return variableNames[i];
    }

    @Override
    public VariableSeries getVariableSeries(String variableName) {
        return data.get(variableName);
    }

    @Override
    public String getTimeVariable() {
        return timeVariable;
    }
}
