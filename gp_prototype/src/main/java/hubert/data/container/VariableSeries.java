package hubert.data.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: koperek
 * Date: 16.02.13
 * Time: 11:49
 */
public class VariableSeries {

    private final String name;
    private final List<Double> values = new ArrayList<>();

    public VariableSeries(String name) {
        this.name = name;
    }

    public void add(double value) {
        values.add(value);
    }

    public void addAll(Collection<Double> values) {
        this.values.addAll(values);
    }

    public int getDataRowsCount() {
        return values.size();
    }

    public double get(int row) {
        return values.get(row);
    }

    public String getName() {
        return name;
    }

    public double[] getSeriesArray() {
        double[] serie = new double[values.size()];

        for (int i = 0; i < values.size(); i++) {
            serie[i] = values.get(i);
        }

        return serie;
    }

    public VariableSeries getFragment(int offset, int size) {
        VariableSeries fragmentVariableSeries = new VariableSeries(this.name);
        fragmentVariableSeries.addAll(values.subList(offset, offset + size));
        return fragmentVariableSeries;
    }
}
