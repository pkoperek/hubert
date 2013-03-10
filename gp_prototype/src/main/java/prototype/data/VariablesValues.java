package prototype.data;

import java.util.HashMap;
import java.util.Map;

/**
 * User: koperek
 * Date: 06.03.13
 * Time: 22:12
 */
public class VariablesValues {

    private Map<String, Number> variablesValues = new HashMap<>();

    public void setVariableValue(String variable, Number value) {
        variablesValues.put(variable, value);
    }

    public Number getVariableValue(String variable) {
        return variablesValues.get(variable);
    }

    public void clear() {
        variablesValues.clear();
    }

}
