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

    private final Map<String, VariableSeries> data = new HashMap<String, VariableSeries>();
    private int rowsCount = 0;
    private String[] variableNames;
    private final String timeVariable;
    private final boolean implicitTime;

    public DataContainer(String timeVariable, boolean implicitTime) {
        this.timeVariable = timeVariable;
        this.implicitTime = implicitTime;
    }

    public void initializeVariables(String[] variableNames) {
        this.variableNames = variableNames;

        data.clear();

        for (String variableName : variableNames) {
            data.put(variableName, new VariableSeries(variableName));
        }
    }

    public double getTimeDifference(int firstRow, int secondRow) {
        if (hasImplicitTime()) {
            return secondRow - firstRow;
        }

        if (timeVariable == null) {
            throw new IllegalArgumentException("Time variable name not set! Please set it with data.variable.time or use data.implicit.time!");
        }

        return getValue(timeVariable, secondRow) - getValue(timeVariable, firstRow);
    }

    private boolean hasImplicitTime() {
        return implicitTime;
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
