package prototype.differentiation.numeric;

import org.apache.commons.math3.analysis.interpolation.LoessInterpolator;
import prototype.data.DataContainer;
import prototype.data.VariableSeries;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * User: koperek
 * Date: 17.03.13
 * Time: 18:09
 */
class LoessNumericalDifferentiationCalculator extends AbstractNumericalDifferentiationCalculator {
    private Map<String, double[]> loessInterpolations = new HashMap<>();
    private LoessInterpolator loessInterpolator = new LoessInterpolator();

    public LoessNumericalDifferentiationCalculator(DataContainer dataContainer) {
        super(dataContainer);
    }

    @Override
    public boolean hasDifferential(String variable, int row) {
        return row != 0 && row != getDataContainer().getRowsCount() - 1;
    }

    @Override
    public double getDirectionalDerivative(String differentiated, String direction, int secondRow) {
        if (!loessInterpolations.containsKey(differentiated)) {
            VariableSeries xSeries = getDataContainer().getVariableSeries(differentiated);
            loessInterpolations.put(differentiated, interpolateSingleVariableWithImplicitTime(xSeries));
        }

        double[] differentials = loessInterpolations.get(differentiated);
        return (differentials[secondRow + 1] - differentials[secondRow - 1])
                / getDifference(direction, secondRow - 1, secondRow + 1);
    }


    private double[] interpolateSingleVariableWithImplicitTime(VariableSeries xSeries) {
        double[] time = new double[xSeries.getDataRowsCount()];
        for (int i = 0; i < time.length; i++) {
            time[i] = i;
        }

        return loessInterpolator.smooth(time, xSeries.getSeriesArray());
    }

    public double getDifferentialQuotient(String x, String y, int dataRow) {
        String key = getKey(x, y);

        if (!loessInterpolations.containsKey(key)) {
            VariableSeries xSeries = getDataContainer().getVariableSeries(x);
            VariableSeries ySeries = getDataContainer().getVariableSeries(y);

            double[] differentials = interpolateTwoVariables(xSeries, ySeries);

            loessInterpolations.put(key, differentials);
        }

        double[] differentials = loessInterpolations.get(key);
        return differentials[dataRow];
    }

    private double[] interpolateTwoVariables(VariableSeries xSeries, VariableSeries ySeries) {
        Integer[] indexes = prepareIndexesArray(getDataContainer().getRowsCount());

        Arrays.sort(indexes, new VariableValuesComparator(xSeries.getSeriesArray()));

        double[] sortedX = translate(xSeries.getSeriesArray(), indexes);
        double[] sortedY = translate(ySeries.getSeriesArray(), indexes);
        double[] interpolated = loessInterpolator.smooth(sortedX, sortedY);

        double[] unsorted = unsort(interpolated, indexes);
        return unsorted;
    }

    private double[] unsort(double[] interpolated, Integer[] indexes) {
        double[] unsorted = new double[interpolated.length];

        for (int i = 0; i < indexes.length; i++) {
            unsorted[indexes[i]] = interpolated[i];
        }

        return unsorted;
    }

    private double[] translate(double[] variableArray, Integer[] indexes) {
        double[] sorted = new double[variableArray.length];

        for (int i = 0; i < indexes.length; i++) {
            sorted[i] = variableArray[indexes[i]];
        }

        return sorted;
    }

    private Integer[] prepareIndexesArray(int length) {
        Integer[] sorted = new Integer[length];
        for (int i = 0; i < sorted.length; i++) {
            sorted[i] = i;
        }
        return sorted;
    }

    private String getKey(String x, String y) {
        return x + "_" + y;
    }

    private class VariableValuesComparator implements Comparator<Integer> {
        private final double[] variableSeries;

        public VariableValuesComparator(double[] variableSeries) {
            this.variableSeries = variableSeries;
        }

        @Override
        public int compare(Integer left, Integer right) {
            return (int) Math.signum(variableSeries[left] - variableSeries[right]);
        }
    }
}
