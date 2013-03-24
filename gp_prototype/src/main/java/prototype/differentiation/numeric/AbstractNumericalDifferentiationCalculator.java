package prototype.differentiation.numeric;

import prototype.data.DataContainer;

/**
 * User: koperek
 * Date: 16.03.13
 * Time: 14:01
 */
abstract class AbstractNumericalDifferentiationCalculator implements NumericalDifferentiationCalculator {

    private final DataContainer dataContainer;

    public AbstractNumericalDifferentiationCalculator(DataContainer dataContainer) {
        this.dataContainer = dataContainer;
    }

    protected DataContainer getDataContainer() {
        return dataContainer;
    }

    protected double getDifference(String variable, int firstRow, int secondRow) {
        double first = dataContainer.getValue(variable, firstRow);
        double second = dataContainer.getValue(variable, secondRow);

        return second - first;
    }

    public double getDifferentialQuotient(String x, String y, int dataRow) {
        return getDifferential(x, dataRow) / getDifferential(y, dataRow);
    }

    protected double getTimeDifference(int firstRow, int secondRow) {
        return dataContainer.getTimeDifference(firstRow, secondRow);
    }
}
