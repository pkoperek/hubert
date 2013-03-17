package prototype.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * User: koperek
 * Date: 14.02.13
 * Time: 19:39
 */
public class DataContainer {

    private Map<String, VariableSeries> data = new HashMap<String, VariableSeries>();
    private int rowsCount = 0;
    private String[] variableNames;

    public DataContainer(String[] variableNames) {
        this.variableNames = variableNames;

        for (String variableName : variableNames) {
            data.put(variableName, new VariableSeries(variableName));
        }
    }

    public double getValue(String variable, int row) {
        VariableSeries numbers = data.get(variable);
        return numbers.get(row);
    }

    public int getRowsCount() {
        return rowsCount;
    }

    public int getVariablesCount() {
        return variableNames.length;
    }

    public void addValue(String variable, double value) {
        if (!data.containsKey(variable)) {
            throw new IllegalArgumentException("Variable: " + variable + " not supported!");
        }

        VariableSeries values = data.get(variable);
        values.add(value);

        if (values.getDataRowsCount() > rowsCount) {
            rowsCount = values.getDataRowsCount();
        }
    }

    public String[] getVariableNames() {
        return Arrays.copyOf(variableNames, variableNames.length);
    }

    public String getVariableName(int i) {
        return variableNames[i];
    }

    public VariableSeries getVariableSeries(String variableName) {
        return data.get(variableName);
    }
}
