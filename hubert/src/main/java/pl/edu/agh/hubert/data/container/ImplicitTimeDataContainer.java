package pl.edu.agh.hubert.data.container;

/**
 * User: koperek
 * Date: 24.03.13
 * Time: 16:22
 */
class ImplicitTimeDataContainer implements DataContainer {
    public final static String IMPLICIT_TIME_VARIABLE_NAME = "_IMPLICIT_TIME_";

    private final DataContainer delegate;
    private VariableSeries implicitTime;

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
            return getOrCreate();
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

    public VariableSeries getOrCreate() {
        if (implicitTime == null) {
            implicitTime = new VariableSeries(IMPLICIT_TIME_VARIABLE_NAME);
            fillImplicitTime();
        }

        return implicitTime;
    }

    private void fillImplicitTime() {
        for (int i = 0; i < delegate.getRowsCount(); i++) {
            implicitTime.add(i);
        }
    }
}
