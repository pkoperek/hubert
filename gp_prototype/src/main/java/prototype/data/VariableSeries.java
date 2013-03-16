package prototype.data;

import java.util.ArrayList;
import java.util.List;

/**
 * User: koperek
 * Date: 16.02.13
 * Time: 11:49
 */
public class VariableSeries {
    private String name;

    private List<Number> values = new ArrayList<Number>();

    public VariableSeries(String name) {
        this.name = name;
    }

    public void add(Number value) {
        values.add(value);
    }

    public int getDataRowsCount() {
        return values.size();
    }

    public Number get(int row) {
        return values.get(row);
    }
}
