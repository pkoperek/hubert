package prototype.data.container;

/**
 * User: koperek
 * Date: 29.03.13
 * Time: 21:24
 */
public class OffsetDataContainer implements DataContainer {

    private final DataContainer dataContainer;
    private final int windowSize;
    private int offset;

    public OffsetDataContainer(DataContainer dataContainer, int windowSize) {
        this.dataContainer = dataContainer;
        this.windowSize = windowSize;
        this.offset = 0;
    }

    @Override
    public double getValue(String variable, int row) {
        return dataContainer.getValue(variable, getOffset() + row);
    }

    @Override
    public int getRowsCount() {
        return windowSize;
    }

    @Override
    public int getVariablesCount() {
        return dataContainer.getVariablesCount();
    }

    @Override
    public String[] getVariableNames() {
        return dataContainer.getVariableNames();
    }

    @Override
    public String getVariableName(int i) {
        return dataContainer.getVariableName(i);
    }

    @Override
    public VariableSeries getVariableSeries(String variableName) {
        VariableSeries variableSeries = dataContainer.getVariableSeries(variableName);
        return variableSeries.getFragment(getOffset(), windowSize);
    }

    @Override
    public String getTimeVariable() {
        return dataContainer.getTimeVariable();
    }

    @Override
    public void addValue(String variable, double value) {
        dataContainer.addValue(variable, value);
    }

    @Override
    public void initializeVariables(String[] variableNames) {
        dataContainer.initializeVariables(variableNames);
    }

    public int getWindowSize() {
        return windowSize;
    }

    public synchronized void incrementOffset() {
        if (offset + 1 + windowSize < dataContainer.getRowsCount()) {
            this.offset++;
        }
    }

    private synchronized int getOffset() {
        return offset;
    }
}
