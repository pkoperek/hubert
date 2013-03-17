package prototype.differentiation.numeric;

import org.apache.commons.math3.analysis.interpolation.LoessInterpolator;
import prototype.data.DataContainer;
import prototype.data.VariableSeries;

import java.util.HashMap;
import java.util.Map;

/**
 * User: koperek
 * Date: 17.03.13
 * Time: 18:09
 */
public class LoessNumericalDifferentiationCalculator extends NumericalDifferentiationCalculator {
    private Map<String, double[]> loessInterpolations = new HashMap<>();
    private LoessInterpolator loessInterpolator = new LoessInterpolator();

    public LoessNumericalDifferentiationCalculator(DataContainer dataContainer) {
        super(dataContainer);
    }

    @Override
    public boolean hasDifferential(String variable, int row) {
        return row != 0;
    }

    @Override
    public double getDifferential(String variable, int secondRow) {
        if (!loessInterpolations.containsKey(variable)) {
            VariableSeries xSeries = getDataContainer().getVariableSeries(variable);
            double[] time = new double[xSeries.getDataRowsCount()];
            for (int i = 0; i < time.length; i++) {
                time[i] = i;
            }

            double[] differentials = loessInterpolator.smooth(time, xSeries.getSeriesArray());

            loessInterpolations.put(variable, differentials);
        }

        double[] differentials = loessInterpolations.get(variable);
        return differentials[secondRow];
    }

    @Override
    public double getDifferentialQuotient(String x, String y, int dataRow) {
        String key = getKey(x, y);

        // TODO: sorting of arrays BEFORE putting data into interpolator
        if (!loessInterpolations.containsKey(key)) {
            VariableSeries xSeries = getDataContainer().getVariableSeries(x);
            VariableSeries ySeries = getDataContainer().getVariableSeries(y);

            double[] xSeriesArray = xSeries.getSeriesArray();
            double[] ySeriesArray = ySeries.getSeriesArray();

            double[] differentials = loessInterpolator.smooth(xSeriesArray, ySeriesArray);

            loessInterpolations.put(key, differentials);
        }

        double[] differentials = loessInterpolations.get(key);
        return differentials[dataRow];
    }

    private String getKey(String x, String y) {
        return x + "_" + y;
    }
}
