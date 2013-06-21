package pl.edu.agh.hubert.differentiation.numeric;

import org.apache.commons.math3.analysis.interpolation.LoessInterpolator;
import pl.edu.agh.hubert.data.container.DataContainer;
import pl.edu.agh.hubert.data.container.VariableSeries;

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
    public double getPartialDerivative(String differentiated, String direction, int row) {
        String key = getKey(differentiated, direction);

        if (!loessInterpolations.containsKey(key)) {
            VariableSeries ySeries = getDataContainer().getVariableSeries(differentiated);
            VariableSeries xSeries = getDataContainer().getVariableSeries(direction);

            double[] differentials = interpolateTwoVariables(xSeries, ySeries);

            loessInterpolations.put(key, differentials);
        }

        double[] differentials = loessInterpolations.get(key);
        return differentials[row];
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
