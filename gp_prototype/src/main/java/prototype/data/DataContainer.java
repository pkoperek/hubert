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

    private Map<String, VariablesSeries> data = new HashMap<String, VariablesSeries>();
    private int rowsCount = 0;
    private String[] variableNames;

    public DataContainer(String[] variableNames) {
        this.variableNames = variableNames;

        for (String variableName : variableNames) {
            data.put(variableName, new VariablesSeries(variableName));
        }
    }

    public Number getValue(String variable, int row) {
        VariablesSeries numbers = data.get(variable);
        return numbers.get(row);
    }

    public int getRowsCount() {
        return rowsCount;
    }

    public int getVariablesCount() {
        return variableNames.length;
    }

    public void addValue(String variable, Number value) {
        if (!data.containsKey(variable)) {
            throw new IllegalArgumentException("Variable: " + variable + " not supported!");
        }

        VariablesSeries values = data.get(variable);
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
}
