package prototype.data;

import java.util.HashMap;
import java.util.Map;

/**
 * User: koperek
 * Date: 06.03.13
 * Time: 22:12
 */
public class MapVariablesValuesContainer implements VariablesValuesContainer {

    private Map<String, Double> variablesValues = new HashMap<>();

    @Override
    public void setVariableValue(String variable, double value) {
        variablesValues.put(variable, value);
    }

    // TODO: optimize
    @Override
    public double getVariableValue(String variable) {
        return variablesValues.get(variable);
    }

    @Override
    public void clear() {
        variablesValues.clear();
    }

}
