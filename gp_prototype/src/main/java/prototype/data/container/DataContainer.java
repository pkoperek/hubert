package prototype.data.container;

import prototype.data.VariableSeries;

/**
 * User: koperek
 * Date: 24.03.13
 * Time: 16:24
 */
public interface DataContainer {
    double getValue(String variable, int row);

    int getRowsCount();

    int getVariablesCount();

    String[] getVariableNames();

    String getVariableName(int i);

    VariableSeries getVariableSeries(String variableName);

    String getTimeVariable();

    void addValue(String variable, double value);

    void initializeVariables(String[] variableNames);
}
