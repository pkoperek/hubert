package prototype.differentiation.numeric;

import prototype.data.DataContainer;

/**
 * User: koperek
 * Date: 16.02.13
 * Time: 11:49
 */
public class CentralNumericalDifferentiationCalculator extends NumericalDifferentiationCalculator {

    public CentralNumericalDifferentiationCalculator(DataContainer dataContainer) {
        super(dataContainer);
    }

    @Override
    public boolean hasDifferential(String variable, int row) {
        if (row == 0 || row == getDataContainer().getRowsCount() - 1) {
            return false;
        }

        return true;
    }

    @Override
    public double getDifferential(String variable, int secondRow) {
        if (secondRow == 0) {
            throw new IllegalArgumentException("Can't compute difference of " + secondRow + " and " + (secondRow - 1));
        }

        return getDifference(variable, secondRow - 1, secondRow) / 2.0;
    }
}