package prototype.differentiation.numeric;

import prototype.data.DataContainer;

/**
 * User: koperek
 * Date: 16.02.13
 * Time: 11:49
 */
class BackwardNumericalDifferentiationCalculator extends AbstractNumericalDifferentiationCalculator {

    public BackwardNumericalDifferentiationCalculator(DataContainer dataContainer) {
        super(dataContainer);
    }

    @Override
    public boolean hasDifferential(String variable, int row) {
        if (row == 0) {
            return false;
        }

        return true;
    }

    @Override
    public double getDifferential(String variable, int row) {
        if (row == 0) {
            throw new IllegalArgumentException("Can't compute difference of " + row + " and " + (row - 1));
        }

        return getDifference(variable, row - 1, row) / getTimeDifference(row - 1, row);
    }
}
