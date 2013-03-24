package prototype.differentiation.numeric;

import prototype.data.DataContainer;

/**
 * User: koperek
 * Date: 16.02.13
 * Time: 11:49
 */
class ForwardNumericalDifferentiationCalculator extends AbstractNumericalDifferentiationCalculator {

    public ForwardNumericalDifferentiationCalculator(DataContainer dataContainer) {
        super(dataContainer);
    }

    @Override
    public boolean hasDifferential(String variable, int row) {
        if (row == getDataContainer().getRowsCount() - 1) {
            return false;
        }

        return true;
    }

    @Override
    public double getDirectionalDerivative(String differentiated, String direction, int row) {
        if (row == 0) {
            throw new IllegalArgumentException("Can't compute difference of " + row + " and " + (row - 1));
        }

        return getDifference(differentiated, row, row + 1) / getDifference(direction, row, row + 1);
    }
}
