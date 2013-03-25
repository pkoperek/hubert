package prototype.data;

import java.util.HashMap;
import java.util.Map;

/**
 * User: koperek
 * Date: 06.03.13
 * Time: 22:12
 */
public class VariablesValues {

    private Map<String, Double> variablesValues = new HashMap<>();

    public void setVariableValue(String variable, double value) {
        variablesValues.put(variable, value);
    }

    // TODO: optimize
    public double getVariableValue(String variable) {
        return variablesValues.get(variable);
    }

    public void clear() {
        variablesValues.clear();
    }

}
