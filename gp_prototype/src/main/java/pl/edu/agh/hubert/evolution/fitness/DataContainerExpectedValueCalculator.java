package pl.edu.agh.hubert.evolution.fitness;

import pl.edu.agh.hubert.data.container.DataContainer;

/**
 * User: koperek
 * Date: 27.03.13
 * Time: 17:20
 */
public class DataContainerExpectedValueCalculator implements RawDataExpectedValueCalculator {
    private final DataContainer dataContainer;

    public DataContainerExpectedValueCalculator(DataContainer dataContainer) {
        this.dataContainer = dataContainer;
    }

    @Override
    public boolean hasValue(int row) {
        return true;
    }

    @Override
    public double computeExpected(String variable, int row) {
        return dataContainer.getValue(variable, row);
    }
}
