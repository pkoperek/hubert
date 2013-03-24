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
    public double getDirectionalDerivative(String differentiated, String direction, int row) {
        if (row == 0) {
            throw new IllegalArgumentException("Can't compute difference of " + row + " and " + (row - 1));
        }

        return getDifference(differentiated, row - 1, row) / getDifference(direction, row - 1, row);
    }

}
