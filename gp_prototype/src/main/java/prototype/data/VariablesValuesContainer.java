package prototype.data;

/**
 * User: koperek
 * Date: 27.03.13
 * Time: 11:33
 */
public interface VariablesValuesContainer {
    void setVariableValue(String variable, double value);

    // TODO: optimize
    double getVariableValue(String variable);

    void clear();
}
