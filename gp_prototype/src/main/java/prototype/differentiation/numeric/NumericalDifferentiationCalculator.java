package prototype.differentiation.numeric;

import prototype.data.DataContainer;

/**
 * User: koperek
 * Date: 16.03.13
 * Time: 14:01
 */
public abstract class NumericalDifferentiationCalculator {

    private final DataContainer dataContainer;

    public NumericalDifferentiationCalculator(DataContainer dataContainer) {
        this.dataContainer = dataContainer;
    }

    public abstract boolean hasDifferential(String variable, int row);

    public abstract Number getDifferential(String variable, int secondRow);

    protected DataContainer getDataContainer() {
        return dataContainer;
    }

    protected Number getDifference(String variable, int firstRow, int secondRow) {
        Number first = dataContainer.getValue(variable, firstRow);
        Number second = dataContainer.getValue(variable, secondRow);

        return first.doubleValue() - second.doubleValue();
    }
}
