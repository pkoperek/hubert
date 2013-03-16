package prototype.data;

/**
 * User: koperek
 * Date: 16.02.13
 * Time: 11:49
 */
public class NumericalDifferentiationCalculator {

    private DataContainer dataContainer;

    public NumericalDifferentiationCalculator(DataContainer dataContainer) {
        this.dataContainer = dataContainer;
    }

    public Number getDifference(String variable, int firstRow, int secondRow) {
        Number first = dataContainer.getValue(variable, firstRow);
        Number second = dataContainer.getValue(variable, secondRow);

        return first.doubleValue() - second.doubleValue();
    }

    public boolean hasDifferential(String variable, int row) {
        if (row == 0) {
            return false;
        }

        return true;
    }

    public Number getDifferential(String variable, int secondRow) {
        if (secondRow == 0) {
            throw new IllegalArgumentException("Can't compute difference of " + secondRow + " and " + (secondRow - 1));
        }

        return getDifference(variable, secondRow - 1, secondRow);
    }

    public Number getPartialDerivativeEstimation(String variableTop, String variableBottom, int dataRow) {
        Number top = getDifferential(variableTop, dataRow);
        Number bottom = getDifferential(variableBottom, dataRow);

        return top.doubleValue() / bottom.doubleValue();
    }
}
