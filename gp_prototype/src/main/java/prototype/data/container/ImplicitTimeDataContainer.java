package prototype.data.container;

import prototype.data.DataContainer;
import prototype.data.VariableSeries;

/**
 * User: koperek
 * Date: 24.03.13
 * Time: 16:22
 */
public class ImplicitTimeDataContainer implements DataContainer {
    public final static String IMPLICIT_TIME_VARIABLE_NAME = "_IMPLICIT_TIME_";

    private final DataContainer delegate;

    public ImplicitTimeDataContainer(DataContainer delegate) {
        this.delegate = delegate;
    }

    @Override
    public double getValue(String variable, int row) {
        if (IMPLICIT_TIME_VARIABLE_NAME.equals(variable)) {
            return row;
        }

        return delegate.getValue(variable, row);
    }

    @Override
    public int getRowsCount() {
        return delegate.getRowsCount();
    }

    @Override
    public int getVariablesCount() {
        return delegate.getVariablesCount();
    }

    @Override
    public String[] getVariableNames() {
        return delegate.getVariableNames();
    }

    @Override
    public String getVariableName(int i) {
        return delegate.getVariableName(i);
    }

    @Override
    public VariableSeries getVariableSeries(String variableName) {
        if (IMPLICIT_TIME_VARIABLE_NAME.equals(variableName)) {
            throw new IllegalArgumentException("Implicit time series should not be requested!");
        }

        return delegate.getVariableSeries(variableName);
    }

    @Override
    public String getTimeVariable() {
        return IMPLICIT_TIME_VARIABLE_NAME;
    }

    @Override
    public void addValue(String variable, double value) {
        delegate.addValue(variable, value);
    }

    @Override
    public void initializeVariables(String[] variableNames) {
        delegate.initializeVariables(variableNames);
    }
}
