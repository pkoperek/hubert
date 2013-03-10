package prototype;

import prototype.data.DataContainer;

/**
 * User: koperek
 * Date: 16.02.13
 * Time: 11:49
 */
public class DifferencesProvider {

    private DataContainer dataContainer;

    public DifferencesProvider(DataContainer dataContainer) {
        this.dataContainer = dataContainer;
    }

    public Number getDifference(String variable, int firstRow, int secondRow) {
        Number first = dataContainer.getValue(variable, firstRow);
        Number second = dataContainer.getValue(variable, secondRow);

        return first.doubleValue() - second.doubleValue();
    }

    public Number getDifference(String variable, int secondRow) {
        if (secondRow == 0) {
            throw new IllegalArgumentException("Can't compute difference of " + secondRow + " and " + (secondRow - 1));
        }

        return getDifference(variable, secondRow - 1, secondRow);
    }

    public Number getPartialDerivativeEstimation(String variableTop, String variableBottom, int dataRow) {
        Number top = getDifference(variableTop, dataRow);
        Number bottom = getDifference(variableBottom, dataRow);

        return top.doubleValue() / bottom.doubleValue();
    }
}
