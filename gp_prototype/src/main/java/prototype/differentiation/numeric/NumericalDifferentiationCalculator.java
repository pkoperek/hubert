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

    public abstract double getDifferential(String variable, int secondRow);

    protected DataContainer getDataContainer() {
        return dataContainer;
    }

    protected double getDifference(String variable, int firstRow, int secondRow) {
        double first = dataContainer.getValue(variable, firstRow);
        double second = dataContainer.getValue(variable, secondRow);

        return first - second;
    }

    public double getDifferentialQuotient(String x, String y, int dataRow) {
        return getDifferential(x, dataRow) / getDifferential(y, dataRow);
    }
}
