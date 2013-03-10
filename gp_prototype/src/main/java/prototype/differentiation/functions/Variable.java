package prototype.differentiation.functions;

import prototype.data.VariablesValues;
import prototype.differentiation.Function;

/**
 * User: koperek
 * Date: 23.02.13
 * Time: 23:29
 */
public class Variable extends Function {

    private final VariablesValues valuesContainer;
    private String variableName;

    public Variable(VariablesValues valuesContainer, String variableName) {
        this.valuesContainer = valuesContainer;
        this.variableName = variableName;
    }

    @Override
    public double evaluate() {
        Number variableValue = valuesContainer.getVariableValue(variableName);
        return variableValue.doubleValue();
    }

    @Override
    public Function clone() {
        return new Variable(valuesContainer, new String(variableName));
    }

    @Override
    public String toString() {
        return variableName;
    }

    public String getVariableName() {
        return variableName;
    }
}
